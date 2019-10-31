package com.frameDesign.baselib.view.animation

import android.animation.Animator
import android.os.Handler
import com.frameDesign.baselib.view.internal.AnimAction
import com.frameDesign.baselib.utils.expand.limit
import com.frameDesign.baselib.view.internal.AnimListener

/**
 * @desc
 * @author liyong
 * @date 2018/10/17
 */
class AnimatorInternal(val anim: Animator) : AnimAction {

    private var mEndLock: Boolean = false
    private var mHandler: Handler? = null
    private var mListener: AnimListener? = null

    init {
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {
                anim.removeListener(this)
                animation?.end()
                if (!mEndLock) {
                    mEndLock = true

                    mListener?.onComplete()
                    mListener = null
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                anim.removeListener(this)
                animation?.end()
                if (!mEndLock) {
                    mEndLock = true

                    mListener?.onComplete()
                    mListener = null
                }
            }

            override fun onAnimationRepeat(animation: Animator?) {
                // 默认取消重复动画
                animation?.end()
            }

            override fun onAnimationStart(animation: Animator?) {
                anim.removeListener(this)
                mListener?.onStart()

                initAnimStatusCallback()
            }
        })
    }

    private val mCheckAnimEnd = object : Runnable {
        override fun run() {
            if (!mEndLock && !anim.isRunning) {
                mEndLock = true

                mListener?.onComplete()
                mListener = null
            }

            if (anim.isRunning()) {
                mHandler?.postDelayed(this, 14)
            } else {
                mHandler = null
            }
        }
    }

    private fun initAnimStatusCallback() {
        if (mHandler == null) {
            mHandler = Handler()
        }

        mHandler?.postDelayed(mCheckAnimEnd, 200)
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
        get() = anim.isRunning

    override val startDelay: Long
        get() = anim.startDelay

    override fun start() {
        anim.start()
    }

    override fun cancel() {
        anim.end()
    }

    override fun setListener(listener: AnimListener) {
        mListener = listener
    }
}