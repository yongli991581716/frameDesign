package com.frameDesign.baselib.view.adapter

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.frameDesign.baselib.R
import com.frameDesign.baselib.view.holder.BaseHolder
import com.frameDesign.commonlib.expand.dp2px
import com.frameDesign.commonlib.expand.doOnClick

/**
 * @desc  重试适配器
 * ..
 * @author liyong
 * @date 2018/10/17
 */
abstract class RetryAdapter(state: Int) : BaseAdapter<BaseHolder>(state) {

    private val translateY = 48.dp2px()

    override fun doViewConvert(holder: BaseHolder) {
        var btnRetry = holder.getView<View>(R.id.btn_status_retry)

        btnRetry doOnClick {
            onRetryEvent(holder.getState())
        }
    }

    override fun createViewExitAnim(holder: BaseHolder, root: ViewGroup): Any? {
//        val set = AnimationSet(true)
//
//        val tv_status_msg: TextView = holder.getView(R.id.tv_status_msg)
//        val ll_status_root: View = holder.getView(R.id.ll_status_root)
//        val iv_status_icon: ImageView = holder.getView(R.id.iv_status_icon)
//        val btn_status_retry: TextView = holder.getView(R.id.btn_status_retry)
//
//        val top_out = AnimationUtils.loadAnimation(holder.context, R.anim.anim_status_top_out)
//        val center_out = AnimationUtils.loadAnimation(holder.context, R.anim.anim_status_center_out)
//        val bottom_out = AnimationUtils.loadAnimation(holder.context, R.anim.anim_status_bottom_out)
//        val root_out = AnimationUtils.loadAnimation(holder.context, R.anim.anim_status_center_out)
//
//        tv_status_msg.animation = center_out
//        iv_status_icon.animation = top_out
//        ll_status_root.animation = root_out
//        btn_status_retry.animation = bottom_out
//
//        set.addAnimation(top_out)
//        set.addAnimation(center_out)
//        set.addAnimation(bottom_out)
//        set.addAnimation(root_out)
//
//        set.interpolator = AccelerateDecelerateInterpolator()
//        set.duration = 300
//
//        return set

//        return createAnim(holder, 1F, 0F)

        return null
    }

    private var mAnimator: ValueAnimator? = null

    override fun createViewEnterAnim(holder: BaseHolder, root: ViewGroup): Any? {
        return createAnim(holder, 1F, 0F)
    }

    private fun createAnim(holder: BaseHolder, from: Float, to: Float): Any? {
        val tv_status_msg: TextView = holder.getView(R.id.tv_status_msg)
//        val ll_status_root: View = holder.getView(R.id.ll_status_root)
        val iv_status_icon: ImageView = holder.getView(R.id.iv_status_icon)
        val btn_status_retry: TextView = holder.getView(R.id.btn_status_retry)

        val animator = ValueAnimator.ofFloat(from, to)

        if (mAnimator != null) {
            mAnimator!!.cancel()
        }

        mAnimator = animator

        animator.duration = 500
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            val fl = it.animatedFraction
//            val alpha = 0.2F + fl * 0.8F
            tv_status_msg.alpha = fl
            iv_status_icon.alpha = fl
//            ll_status_root.alpha = fl
            btn_status_retry.alpha = fl

            iv_status_icon.translationY = -(1 - fl) * translateY
            btn_status_retry.translationY = (1 - fl) * translateY
        }

        return animator
    }

    abstract fun onRetryEvent(state: Int)

    override fun doViewRecycle(holder: BaseHolder) {

    }

}
