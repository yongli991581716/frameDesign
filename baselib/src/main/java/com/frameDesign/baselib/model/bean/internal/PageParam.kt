package com.frameDesign.baselib.model.bean.internal

/**
 * @desc 页面参数封装
 * ..
 * @author liyong
 * @date 2018/10/17
 */
class PageParam {

    companion object {
        const val PAGE_SIZE = 20
    }

    var size = PAGE_SIZE

    var page = 1
    var total = 0
    var current = 0

    fun reset() {
        page = 1
        total = 0
        current = 0
    }

    fun success(data: Page<*>) {
        page++
        total = data.totalCount
        current += data.data().size
    }

    fun hasData(): Boolean = current < total

}