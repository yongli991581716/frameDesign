package com.frameDesign.baselib.controller

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.frameDesign.baselib.R
import com.frameDesign.baselib.view.internal.IRefreshAndLoad
import com.frameDesign.baselib.model.bean.DefResult
import com.frameDesign.baselib.model.bean.internal.ErrorBean
import com.frameDesign.baselib.model.bean.internal.Page
import com.frameDesign.baselib.model.bean.internal.PageParam
import com.frameDesign.baselib.model.bean.miss.EmptyDataMiss
import com.frameDesign.baselib.view.adapter.BaseItemAdapter
import com.frameDesign.baselib.view.adapter.isEmptyData
import com.frameDesign.commonlib.expand.DEF_FUN_1
import com.frameDesign.commonlib.expand.runUIThread
import com.frameDesign.commonlib.expand.toast
import com.frameDesign.commonlib.views.SuperRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.frameDesign.baselib.view.animation.EmptyViewAnim
import io.reactivex.Observable
import org.jetbrains.anko.findOptional

/**
 * 分页Fragment基类
 *
 * @author JustBlue
 * @time 2018/10/23
 */
abstract class BaseListFragment : BaseFragment(),
    IRefreshAndLoad,
    BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {

    protected var mRecyclerView: RecyclerView? = null
    protected var mRefreshLayout: SuperRefreshLayout? = null

    private var mContentAdapter: BaseQuickAdapter<out Any, out BaseViewHolder>? = null

    private val mPageParam by lazy(LazyThreadSafetyMode.NONE) { PageParam() }

    override fun internalInitView(view: View, state: Bundle?) {
        super.internalInitView(view, state)

        mRecyclerView = view.findOptional(R.id.rv_content)
        mRefreshLayout = view.findOptional(R.id.refresh_layout)

        if (mRefreshLayout != null) {
            initContentView(mRecyclerView!!, mRefreshLayout!!)
        }
    }

    /**
     * 获取分页查询参数封装类
     * @return PageParam
     */
    protected fun getPageParams(): PageParam = mPageParam

    /**
     * 绑定recyclerView and SuperRefreshLayout
     * @param recyclerView RecyclerView
     * @param layout SuperRefreshLayout
     */
    protected fun bindRefreshAndRecyclerView(rv: RecyclerView, layout: SuperRefreshLayout) {
        if (mRecyclerView != null) {
            mRecyclerView!!.adapter = null
        }
        mRecyclerView = rv

        if (mRefreshLayout != null) {
            mRefreshLayout!!.setOnRefreshListener(null)
        }
        mRefreshLayout = layout

        initContentView(rv, layout)
    }

    /**
     * 初始化[recyclerView]和[SuperRefreshLayout]
     * @param recyclerView RecyclerView
     * @param layout SuperRefreshLayout
     */
    @CallSuper
    protected open fun initContentView(rv: RecyclerView, layout: SuperRefreshLayout) {
        rv.layoutManager = layoutManager()
        rv.adapter = initAdapter()

        layout.setEnableLoadMore(false)
        layout.setOnRefreshListener(this)
    }

    private fun initAdapter(): BaseQuickAdapter<out Any, out BaseViewHolder> {
        val adapter = mContentAdapter ?: getAdapterTemp()
            .also {
                it.setEnableLoadMore(true)
                it.setOnLoadMoreListener(this, mRecyclerView!!)
            }

        mContentAdapter = adapter

        return adapter
    }

    override fun showEmptyData() {
        showContent()

        val adapter = mContentAdapter ?: return

        if (adapter.emptyViewCount == 0 && mRefreshLayout != null) {
            adapter.setEmptyView(R.layout.layout_list_empty_item, mRefreshLayout)

            (adapter as? BaseItemAdapter)?.setEmptyViewConvert(EmptyViewAnim())
        }

        if (emptyAdapter()) {
            mContentAdapter?.notifyDataSetChanged()
        }
    }

    override fun layoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(activity)

    override fun emptyAdapter(): Boolean =
        mContentAdapter?.isEmptyData() ?: true

    override fun failureLoadStatus(loadMore: Boolean) {
        tryFinishRefreshLayout()

        val adapter = mContentAdapter ?: return

        if (!loadMore) {
            adapter.setNewData(null)
            adapter.loadMoreEnd()
        } else {
            adapter.loadMoreFail()
        }
    }

    override fun completeLoadStatus(loadMore: Boolean, hasData: Boolean) {
        tryFinishRefreshLayout()

        val adapter = mContentAdapter ?: return

        adapter.loadMoreComplete()

        if (!hasData)
            adapter.loadMoreEnd()
    }

    /**
     * 尝试复原刷新控件的显示状态
     */
    private fun tryFinishRefreshLayout() {
        val layout = mRefreshLayout ?: return

        // 如正在刷新状态, 还原View状态
        if (layout.state == RefreshState.Refreshing)
            layout.finishRefresh()
    }

    /**
     * 显示加载失败后的界面
     * @param loadMore Boolean
     * @param e Throwable
     * @param msg String
     */
    protected open fun showLoadFailure(loadMore: Boolean, e: Throwable, msg: String) {
        when (e) {
            is EmptyDataMiss -> tryShowEmptyUI(loadMore)

            else -> {
                failureLoadStatus(loadMore)

                if (!loadMore) {
                    dispatchFailure(e, msg)
                } else {
                    toast(msg)
                }
            }
        }
    }

    /**
     * 尝试显示空数据界面
     */
    private fun tryShowEmptyUI(loadMore: Boolean) {
        completeLoadStatus(loadMore, false)

        if (!loadMore) {
            mContentAdapter?.setNewData(null)
        }

        if (emptyAdapter()) {
            showEmptyData()
        }
    }

    final override fun loadData() {
        loadData(false)
    }

    /**
     * 添加加载函数
     *
     * @param loadMore Boolean
     */
    open fun loadData(loadMore: Boolean) {
        if (!loadMore) {
            mPageParam.reset()
        }

        if (emptyAdapter()) {
            showLoading()
        }
    }

    override fun onLoadMoreRequested() = loadData(true)

    override fun onRefresh(refreshLayout: RefreshLayout) = loadData(false)

    /**
     * 绑定显示列表数据
     * @receiver Observable<T>
     * @param onFinished () -> Unit
     * @param onFailure (t: Throwable, msg: String) -> Unit
     * @param runNext (T) -> Unit
     */
    protected fun <T> Observable<T>.bindListSub(
        onFinished: (hasError: Boolean) -> Unit = DEF_FUN_1,
        onFailure: (error: ErrorBean) -> Unit = DEF_FUN_1,
        onSuccess: (data: T) -> Unit
    ) =
        this.bindDestroy()
            .runUIThread()
            .subscribe(object : DefResult<T>() {

                override fun doFinish(errorFinish: Boolean) {
                    if (onFinished == DEF_FUN_1) {
                        hideProgress()
                    } else {
                        onFinished(errorFinish)
                    }
                }

                override fun doFailure(eBean: ErrorBean) {
                    val (e, msg) = eBean

                    if (onFailure == DEF_FUN_1) {
                        showLoadFailure(false, e, msg)
                    } else {
                        onFailure(eBean)
                    }
                }

                override fun doSuccess(data: T) {
                    showContent()

                    onSuccess(data)

                    completeLoadStatus(
                        false,
                        false
                    )
                }
            })

    /**
     * 绑定显示列表数据
     * @receiver Observable<T>
     * @param loadMore Boolean
     * @param onFinished () -> Unit
     * @param onFailure (t: Throwable, msg: String) -> Unit
     * @param runNext (T) -> Unit
     */
    protected fun <T> Observable<Page<T>>.bindPageSub(
        loadMore: Boolean,
        onFinished: (hasError: Boolean) -> Unit = DEF_FUN_1,
        onFailure: (error: ErrorBean) -> Unit = DEF_FUN_1,
        onSuccess: (data: List<T>) -> Unit
    ) =
        this.bindDestroy()
            .runUIThread()
            .subscribe(object : DefResult<Page<T>>() {

                override fun doFinish(errorFinish: Boolean) {
                    if (onFinished == DEF_FUN_1) {
                        hideProgress()
                    } else {
                        onFinished(errorFinish)
                    }
                }

                override fun doFailure(eBean: ErrorBean) {
                    val (e, msg) = eBean

                    if (onFailure == DEF_FUN_1) {
                        showLoadFailure(loadMore, e, msg)
                    } else {
                        onFailure(eBean)
                    }
                }

                override fun doSuccess(data: Page<T>) {
                    showContent()

                    mPageParam.success(data)

                    onSuccess(data.list)

                    completeLoadStatus(
                        loadMore,
                        mPageParam.hasData()
                    )
                }
            })


}
