package com.zqkh.commlibrary.utilslibrary.expand

import android.os.Build
import android.view.View
import cn.qqtheme.framework.picker.WheelPicker
import cn.qqtheme.framework.popup.ConfirmPopup
import com.frameDesign.commonlib.R
import com.frameDesign.commonlib.expand.SCREEN_W
import com.frameDesign.commonlib.expand.zqColor

/**
 *  picker库扩展（AndroidPicker\）
 * @author liyong
 * @date 2019-10-25
 */

/**
 * 适配虚拟键
 */
fun <V : View, T : ConfirmPopup<V>> T.processVirtualKey() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    window.decorView.setOnSystemUiVisibilityChangeListener {
        var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                    //全屏
//                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        } else {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
        window.decorView.systemUiVisibility = uiOptions
    }
}


/**
 * 提供公用样式方法
 */
fun WheelPicker.setStyle(): Unit {
    setLineSpaceMultiplier(2f)

    val blueTextColor = zqColor(R.color.colorAccent)

    setSubmitTextColor(blueTextColor)
    setCancelTextColor(blueTextColor)

    setTextColor(
        zqColor(R.color.color555555),
        zqColor(R.color.colorD8D8D8)
    )

    setCancelTextSize(16)
    setSubmitTextSize(16)
    setTitleTextSize(18)

    setContentPadding(16, 0)

    setAnimationStyle(R.style.dialog)

    setTopHeight(48)
    setHeight((SCREEN_W * 0.65f).toInt())

    setCancelable(false)
    setCanceledOnTouchOutside(false)

    setTopLineColor(zqColor(R.color.colorE8E8E8))
    setDividerColor(zqColor(R.color.colorE8E8E8))
}