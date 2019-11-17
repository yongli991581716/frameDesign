package com.frameDesign.baselib.model.bean

import com.frameDesign.commonlib.uitls.JsonUtils
import java.io.Serializable
import java.util.*

/**
 * Created by liyong on 2017/12/21.
 */
open class BaseBean : Serializable {
    var locaUuid = UUID.randomUUID()//本地化唯一标识，服务器返回值里面没有这个参数
    var message = ""

    var eventBusType = 0
    var bindFlag: Boolean? = null// 微信是否绑定

    override fun toString(): String {
        return JsonUtils.toJsonString(this) ?: ""
    }
}