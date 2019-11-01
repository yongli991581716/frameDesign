package com.frameDesign.commonlib.views.internal

import android.view.View

/**
 * @desc  不同行为下view
 * ..
 * @author liyong
 * @date 2018/10/17
 */
interface ViewAction {

    /**
     * 绑定HolderView
     * 如: View已是[com.holder.HolderView]直接使用
     * @param view View
     */
    fun bindHolderView(view: View)

    /**
     * 展示加载界面
     */
    fun showLoading()

    /**
     * 展示实际数据(及加载结果)
     */
    fun showContent()

    /**
     * 展示数据加载失败界面
     */
    fun showDataError()

    /**
     * 显示空数据界面
     */
    fun showEmptyData()

    /**
     * 展示没有网络界面
     */
    fun showNoNetwork()

    /**
     * 通过状态显示界面
     * @param status Int
     */
    fun showViewByStatus(status: Int)

    /**
     * 展示异常界面
     * @param t Throwable
     * @param msg String
     */
    fun dispatchFailure(t: Throwable, msg: String)

}