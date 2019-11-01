package com.frameDesign.commonlib.views.internal

/**
 * @desc  加载数据接口
 * ..
 * @author liyong
 * @date 2018/10/17
 */
interface LoadAction {

    var firstLoad: Boolean

    fun loadData()

    fun lazyLoad()

    fun restLoad() {
        firstLoad = true
    }

}