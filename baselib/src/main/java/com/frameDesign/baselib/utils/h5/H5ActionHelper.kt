package com.frameDesign.baselib.utils.h5

import android.app.Activity
import com.frameDesign.baselib.controller.WebH5Activity

/**
 * H5跳转辅助类
 *
 * @author liyong
 * @time 2018/12/7
 */
object H5ActionHelper {

    const val h5_content_not_params = "h5_introduction"
    const val h5_content_has_params = "h5_service_protocol"


    /**
     * 跳转项目示例（不包含包含参数）
     * @param act Activity
     */
    fun gotoNotParamsWebDemo(act: Activity) {
        WebH5Activity.start(act, h5_content_not_params)
    }

    /**
     * 跳转项目示例（包含参数）
     * @param act Activity
     * @param id String
     */
    fun gotoHasParamsWebDemo(act: Activity, id: String) {
        WebH5Activity.start(act, h5_content_has_params) {
            put("id", id)
        }
    }


}