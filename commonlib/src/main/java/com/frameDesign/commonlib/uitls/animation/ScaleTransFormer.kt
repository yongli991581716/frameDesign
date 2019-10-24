package com.frameDesign.commonlib.uitls.animation

import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * 首页banner 缩放大小  按着 Y 轴进行缩放
 */
class ScaleTransFormer : ViewPager.PageTransformer {
    private val MIN_SCALE = 0.80f
    private val MIN_ALPHA = 0.5f

    override fun transformPage(view: View, position: Float) {
        if (position < -1) {//看不到的一页，第一页前边 *
            //view.scaleX=MIN_SCALE
            view.scaleY = MIN_SCALE
        } else if (position <= 1) {
            if (position < 0) {//滑出的页 0.0 ~ -1 *
                val scaleFactor = (1 - MIN_SCALE) * (0 - position)
                //  view.scaleX=(1 - scaleFactor)
                view.scaleY = (1 - scaleFactor)
            } else {//滑进的页 1 ~ 0.0 *
                val scaleFactor = (1 - MIN_SCALE) * (1 - position)
                // view.scaleX=(MIN_SCALE + scaleFactor)
                view.scaleY = (MIN_SCALE + scaleFactor)
            }
        } else {//看不到的另一页，最后一页后边 *
            // view.scaleX=MIN_SCALE
            view.scaleY = MIN_SCALE
        }
    }
}