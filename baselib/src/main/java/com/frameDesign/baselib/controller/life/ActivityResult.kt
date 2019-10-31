package com.frameDesign.baselib.controller.life

import android.app.Activity
import android.content.Intent

/**
 * @desc  activity结果类
 * ..
 * @author liyong
 * @date 2018/10/17
 */
data class ActivityResult(val requestCode: Int, val resultCode: Int, val data: Intent? = null) {

    companion object {

        const val DEFAULT_KEY = "rx_activity_result_data"

    }

    fun isSuccess(): Boolean =
            this.resultCode == Activity.RESULT_OK

    fun notEmptyData(): Boolean {
        return data != null
    }

}