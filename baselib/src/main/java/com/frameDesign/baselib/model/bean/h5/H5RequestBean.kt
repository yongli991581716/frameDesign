package com.frameDesign.baselib.model.bean.h5

import java.io.Serializable


/**
 * Created by gaoyang on 2017/12/28.
 */
open class H5RequestBean : Serializable {
    var rel: String? = null
    var data: Param? = null

    class Param : Serializable {

        /**
         *分享数据
         */
        var url: String? = null
        var rel: String? = null
        var title: String = ""
        var desc: String = ""
        var picUrl: String = ""

        var params: Map<String, String>? = null

        var id: String? = null
        var type: String? = null

    }
}