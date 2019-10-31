package com.frameDesign.baselib.view.animation

import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationSet
import com.frameDesign.baselib.view.internal.AnimAction
import com.frameDesign.baselib.view.internal.AnimListener
import com.frameDesign.baselib.view.animation.MAX_DURATION
import com.frameDesign.baselib.view.animation.MIN_DURATION
import com.frameDesign.baselib.utils.expand.limit

/**
 * @desc
 * @author liyong
 * @date 2018/10/17
 */
class AnimationInternal(var anim: Animation) :
    AnimAction {

    private var mHandler: Handler? = null
    private var mListener: AnimListener? = null

    private val mCheckAnimEnd = object : Runnable {
        override fun run() {
            if (!mEndLock && anim.hasEnded()) {
                mEndLock = true

                mListener?.onComplete()
            }

            if (!anim.hasEnded()) {
                mHandler?.postDelayed(this, 14)
            } else {
                mHandler = null
            }
        }
    }

    init {
        if (anim !is AnimationSet) {
            val set = AnimationSet(true)

            set.addAnimation(anim)

            anim = set
        }

        initAnim(set = anim as AnimationSet)
    }

    private var mEndLock: Boolean = false

    private fun initAnim(set: AnimationSet) {
        set.isFillEnabled = true
        set.fillAfter = false
        set.fillBefore = true

        if (set.hasStarted()) {
            set.cancel()
            set.reset()
        }

        set.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                animation?.cancel()
            }

            override fun onAnimationEnd(animation: Animation?) {
                if (!mEndLock) {
                    mEndLock = true
                    mListener?.onComplete()
                }
                animation?.setAnimationListener(null)
            }

            override fun onAnimationStart(animation: Animation?) {
                mListener?.onStart()

                initAnimStatusCallback()
            }
        })
    }

    private fun initAnimStatusCallback() {
        if (mHandler == null) {
            mHandler = Handler()
        }

        mHandler?.postDelayed(mCheckAnimEnd, 50)
    }

    override var duration: Long
        get() = anim.duration
        set(value) {
            anim.duration = value.limit(
                    min = MIN_DURATION,
                    max = MAX_DURATION
            )
        }

    override val running: Boolean
        get() = anim.hasStarted() && !anim.hasEnded()

    override val startDelay: Long
        get() = anim.startOffset

    override fun start() {
        if (!anim.hasStarted()) {
            anim.start()
        }
    }

    override fun cancel() {
        if (!anim.hasEnded()) {
            anim.cancel()
        }
    }

    override fun setListener(listener: AnimListener) {
        mListener = listener
    }
}