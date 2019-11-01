package com.frameDesign.baselib.view.animation

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.frameDesign.baselib.R
import com.frameDesign.commonlib.expand.dp2px
import org.jetbrains.anko.findOptional

/**
 * BaseAdapter 空数据界面进入动画
 *
 * @author liyong
 * @time 2018/11/7
 */
class EmptyViewAnim : (View) -> Unit {

    private val mOffsetY = 56.dp2px()
    private var mAnimator: ValueAnimator? = null

    override fun invoke(emptyRoot: View) {
        val topView = emptyRoot.findOptional<View>(R.id.iv_status_icon) ?: return
        val botView = emptyRoot.findOptional<View>(R.id.tv_status_msg) ?: return

        mAnimator?.cancel()

        mAnimator = ValueAnimator.ofFloat(0F, 1F)
        mAnimator?.addUpdateListener {
            val fl = it.animatedFraction

            topView.alpha = fl
            botView.alpha = fl

            topView.translationY = -(1 - fl) * mOffsetY
            botView.translationY = (1 - fl) * mOffsetY
        }

        mAnimator?.doOnEnd {
            mAnimator?.removeAllUpdateListeners()
        }

        mAnimator?.duration = 300
        mAnimator?.interpolator =
            DecelerateInterpolator()
        mAnimator?.start()
    }

}