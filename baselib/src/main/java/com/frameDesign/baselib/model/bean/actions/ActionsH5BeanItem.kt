package com.frameDesign.baselib.model.bean.actions

import java.io.Serializable

/**
 * Created by liyong on 2017/12/21.
 */
class ActionsH5BeanItem : Serializable {
    var rel: String = ""
    var href: String = ""
    var title: String = ""

    /**
     * 判断url是否非法
     * @return Boolean
     */
    fun invalidAction(): Boolean =
            rel.isNullOrEmpty()
                    || href.isNullOrEmpty()
}