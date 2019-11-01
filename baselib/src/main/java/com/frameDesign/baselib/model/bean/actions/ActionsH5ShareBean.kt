package com.frameDesign.baselib.model.bean.actions

import java.io.Serializable

/**
 * Created by gaoyang on 2017/12/21.
 */
class ActionsH5ShareBean : Serializable {

    companion object {

        fun createTest(): ActionsH5ShareBean {
            return ActionsH5ShareBean().also {
                it.rel = "xx"
                it.url = "xx"
                it.desc = "测试描述"
                it.title = "测试标题"
                it.picUrl = "xx"
            }
        }
    }

    var rel: String = ""
    var url: String = ""
    var desc: String = ""
    var title: String = ""
    var picUrl: String = ""
}