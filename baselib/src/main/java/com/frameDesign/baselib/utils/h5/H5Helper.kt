package com.frameDesign.baselib.utils.h5

import android.os.Build
import com.frameDesign.baselib.const.HttpConfig
import com.frameDesign.baselib.controller.BaseActivity
import com.frameDesign.baselib.controller.WebH5Activity
import com.frameDesign.baselib.model.bean.h5.H5RequestBean
import com.frameDesign.baselib.utils.ActionsHelp
import com.frameDesign.baselib.utils.UmengUtils
import com.frameDesign.commonlib.expand.DEF_FUN_1
import com.frameDesign.commonlib.expand.fdToast
import com.frameDesign.commonlib.uitls.JsonUtils
import com.frameDesign.commonlib.uitls.LogUtils
import com.frameDesign.commonlib.uitls.StringUtils
import com.frameDesign.commonlib.uitls.SystemUtil
import com.frameDesign.commonlib.views.CustWebView
import java.util.*


/**
 * h5与native交互辅助类
 *
 * @author liyong
 * @date 2018/1/22.
 */
object H5Helper {

    /**
     * H5交互处理
     */
    private val system_getHeader = "system_getHeader"
    private val share_common_data = "share_common_data"
    private val system_getUserInfo = "system_getUserInfo"
    private val open_share_pannel = "open_share_pannel"//商品详情分享

    /**
     * 处理H5交互
     */
    fun processJsInterface(
        activity: BaseActivity,
        web: CustWebView,
        shareCallback: (H5RequestBean) -> Unit = DEF_FUN_1
    ) {
        web.registerHandler("JS2NativeInteraction") { data, function ->
            LogUtils.d("processJsInterface data=$data")
            val req = JsonUtils.fromJson<H5RequestBean>(data)
            if (req != null) {
                when (req.rel) {
                    system_getHeader -> {
                        //得到请求头
                        val headerStr = JsonUtils.toJsonString(h5Header())
                        LogUtils.d("system_getHeader back=$headerStr")
                        function.onCallBack(headerStr)
                    }
                    system_getUserInfo -> {
                        //得到用户信息
//                        val user = JsonUtils.toJsonString(UserConfig.mActualUser)
//                        LogUtils.d("system_getUserInfo user=$user")
//                        function.onCallBack(user)
                    }
                    share_common_data -> {//获取分享数据
                        if (activity is WebH5Activity) {
                            activity.sendData(req.data)
                        }
                    }
                    open_share_pannel -> {
                        shareCommon(activity, req.data!!)
                    }

                }

            }
        }
    }


    /**
     * 添加统一的系统header
     * @receiver Request.Builder
     * @return Request.Builder
     */
    fun h5Header(): Map<String, String> {
        val vName = SystemUtil.getAppVersionName()
        val vCode = SystemUtil.getAppVersionCode()

        val map = HashMap<String, String>()
        map["osType"] = "Android"
        map["osVersion"] = Build.VERSION.SDK_INT.toString() + ""
        map["appName"] = "ZQKH-BreastButler"
        map["version"] = "$vName"
        map["version_code"] = "$vCode"
        map["Authorization"] = HttpConfig.getToken()
        return map
    }

    /**
     * 动态公共分享
     */
    fun shareCommon(activity: BaseActivity, params: H5RequestBean.Param) {
        val action = ActionsHelp.getShareApiByAlias(params.rel ?: "")
        if (action == null) {
            fdToast("路径解析失败!")
            return
        }
        action?.url =
            StringUtils.joinUrl(params.params, action?.url) ?: ""// "${params?.url}&id=${params.id}"
        if (!StringUtils.isEmpty(params.title)) {
            action?.title = params.title
        }
        if (!StringUtils.isEmpty(params.picUrl)) {
            action?.picUrl = params.picUrl
        }
        if (!StringUtils.isEmpty(params.desc)) {
            action?.desc = params.desc
        }
        UmengUtils.share(activity, action, null)
    }
}