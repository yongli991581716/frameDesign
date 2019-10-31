package com.frameDesign.baselib.view.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * description list 有多个item bean 的时候
 * @author liyong
 * @date 2019-10-30
 */
abstract class BaseMultiItemAdapter<T : MultiItemEntity, K : BaseViewHolder>(data: List<T>) :
    BaseMultiItemQuickAdapter<T, K>(data) {
    override fun convert(helper: K, item: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 当前请求的页数
     */
    var pageIndex = 1

    override fun setNewData(data: List<T>?) {
        super.setNewData(data)
        pageIndex++
    }

    override fun addData(data: T) {
        super.addData(data)
        pageIndex++
    }

    override fun addData(newData: Collection<out T>) {
        super.addData(newData)
        pageIndex++
    }

    override fun addData(position: Int, data: T) {
        super.addData(position, data)
        pageIndex++
    }

    override fun addData(position: Int, newData: Collection<out T>) {
        super.addData(position, newData)
        pageIndex++
    }
}