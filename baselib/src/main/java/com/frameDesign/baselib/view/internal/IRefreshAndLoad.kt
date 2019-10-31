package com.frameDesign.baselib.view.internal

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * ..
 *
 * @author JustBlue
 * @time 2018/10/23
 */
interface IRefreshAndLoad {

    /**
     * 创建[RecyclerView.Adapter]
     * @return BaseQuickAdapter<Any, out BaseViewHolder>
     */
    fun getAdapterTemp(): BaseQuickAdapter<out Any, out BaseViewHolder>

    /**
     * 返回默认[RecyclerView.LayoutManager]
     * @return RecyclerView.LayoutManager
     */
    fun layoutManager(): RecyclerView.LayoutManager

    /**
     * 加载完成, 重置界面状态()
     * @param loadMore Boolean
     * @param hasData Boolean
     */
    fun completeLoadStatus(loadMore: Boolean, hasData: Boolean)

    /**
     * 加载失败
     * @param loadMore Boolean
     */
    fun failureLoadStatus(loadMore: Boolean)

    /**
     * 是否为空数据
     * @return Boolean
     */
    fun emptyAdapter(): Boolean

    fun noEmptyAdapter(): Boolean = !emptyAdapter()

}