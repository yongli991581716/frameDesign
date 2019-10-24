package com.frameDesign.commonlib.uitls

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.frameDesign.commonlib.CommHelper

/**
 *  适配屏幕状态栏
 * @author liyong
 * @date  2017/12/21.
 */
@SuppressLint("StaticFieldLeak")
object FitHelper {
    private var ctx = CommHelper.mCtx

    /**
     * 适配屏幕
     * @param rootLayout View 根布局
     * @param contentLayout View 内容布局
     */
    fun fitStatusBar(rootLayout: View?, vararg contentLayout: View?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }

        //只有流海屏需要处理
//        val dis = (rootLayout ?: contentLayout[0])?.rootWindowInsets?.displayCutout
//        if (dis?.safeInsetTop != null && dis.safeInsetTop <= 0) {
//            return
//        }

        val immersionTop = SystemUtil.getStatusHeight()
        //LogUtils.d("fitStatusBar immersionTop=$immersionTop")

//        //只有流海屏需要处理
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1 || immersionTop < 30.px2dp()) {
//            return
//        }

        //布局偏移到状态栏背后
        val rootPms = when (rootLayout?.parent) {
            is LinearLayout -> {
                rootLayout.layoutParams as LinearLayout.LayoutParams
            }
            is RelativeLayout -> {
                rootLayout.layoutParams as RelativeLayout.LayoutParams
            }
            is ConstraintLayout -> {
                rootLayout.layoutParams as ConstraintLayout.LayoutParams
            }
            is FrameLayout -> {
                rootLayout.layoutParams as FrameLayout.LayoutParams
            }
            else -> {
                null
            }
        }
        rootPms?.apply {
            topMargin = -immersionTop
            rootLayout?.layoutParams = this
        }

        //content偏移到状态栏下方
        contentLayout.forEach {
            val contentPms = when (it?.parent) {
                is LinearLayout -> {
                    it.layoutParams as LinearLayout.LayoutParams
                }
                is RelativeLayout -> {
                    it.layoutParams as RelativeLayout.LayoutParams
                }
                is ConstraintLayout -> {
                    it.layoutParams as ConstraintLayout.LayoutParams
                }
                is FrameLayout -> {
                    it.layoutParams as FrameLayout.LayoutParams
                }
                else -> {
                    null
                }
            }
            contentPms?.apply {
                topMargin = immersionTop
                it?.layoutParams = this
            }
        }
    }
}