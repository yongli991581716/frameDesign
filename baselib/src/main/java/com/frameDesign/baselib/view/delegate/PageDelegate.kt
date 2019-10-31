package com.frameDesign.baselib.view.delegate

import android.app.Activity
import android.view.View
import com.frameDesign.baselib.R
import com.frameDesign.baselib.controller.BaseCommActivity
import com.frameDesign.baselib.controller.BaseCommFragment
import com.frameDesign.commonlib.views.internal.BaseActivityDelegate
import com.frameDesign.commonlib.views.internal.ILifeCycle
import com.frameDesign.baselib.view.internal.HolderAction
import com.frameDesign.baselib.view.delegate.HolderDelegate
import com.frameDesign.baselib.view.HolderView
import com.frameDesign.baselib.view.adapter.BaseAdapter
import com.frameDesign.baselib.view.adapter.RetryAdapter
import com.frameDesign.commonlib.expand.replace

/**
 * @desc 用于代理
 * ..
 * @author liyong
 * @date 2018/10/17
 */
class PageDelegate(act: Activity, cycle: ILifeCycle, val doRetry: (status: Int) -> Unit) :
    BaseActivityDelegate(act, cycle),
    HolderAction {

    constructor(activity: BaseCommActivity, doRetry: (status: Int) -> Unit) : this(
        activity,
        activity,
        doRetry
    )

    constructor(
        fragment: BaseCommFragment,
        doRetry: (status: Int) -> Unit
    ) : this(fragment.activity!!, fragment, doRetry) {
        this.mFragment = fragment
    }

    private var mStateAdapterList = mutableListOf<BaseAdapter<*>>()

    private var mFragment: BaseCommFragment? = null
    private var mHolderView: HolderView? = null
    private var mReplaceView: View? = null

    private val showHolder: Boolean
        get() = mHolderView?.parent != null

    private val needReplace: Boolean
        get() = mReplaceView != null

    fun initHolderView(view: View) {
        if (view != null) {
            if (view is HolderView) {
                mHolderView = view
                mReplaceView = null

                bindDefaultAdapter()
            } else {
                // 还原View原始状态
                if (mHolderView != null) {
                    mHolderView!!.showContent()

                    if (mReplaceView != null && showHolder) {
                        mReplaceView!! replace mHolderView!!
                    }
                }
                mReplaceView = view

                mHolderView = inflate(R.layout.layout_holder_default)

                bindDefaultAdapter()
            }
        }
    }

    override fun onViewCreated() {
        if (mFragment != null) {
            val viewFind = mFragment!!.view

            mHolderView = viewFind?.findViewById(R.id.holder_layout)
        } else {
            mHolderView = find(R.id.holder_layout)
        }

        bindDefaultAdapter()
    }

    private fun bindDefaultAdapter() {
        val holderView = mHolderView ?: return

        val emptyAdapter = createRetryAdapter(HolderView.state_empty)
        val errorAdapter = createRetryAdapter(HolderView.state_error)
        val netMissAdapter = createRetryAdapter(HolderView.state_netMiss)
        val loadingAdapter = HolderDelegate.create(
            HolderView.state_loading,
            R.layout.layout_holder_loading
        )

        mStateAdapterList.add(emptyAdapter)
        mStateAdapterList.add(errorAdapter)
        mStateAdapterList.add(netMissAdapter)
        mStateAdapterList.add(loadingAdapter)

        holderView.addAdapter(emptyAdapter)
        holderView.addAdapter(errorAdapter)
        holderView.addAdapter(netMissAdapter)
        holderView.addAdapter(loadingAdapter)
    }

    private fun createRetryAdapter(state: Int): RetryAdapter =
        object : RetryAdapter(state) {
            override fun onRetryEvent(status: Int) = doRetry(status)
        }

    override fun onDestroy() {
        mStateAdapterList.clear()
    }

    /**
     * 尝试切换holderView的显示状态
     * 分为以下几种情况: (H为HolderView, R为替换显示的View, 前提两个都存在)
     * 1. 需要显示H, 现在显示的H, 无操作
     * 2. 需要显示R, 现在显示的R, 无操作
     * 3. 需要显示H([show] == true), 现在显示的R, R替换为H显示
     * 4. 需要显示R([show] == false), 现在显示的H, H替换为R显示
     */
    private fun trySwitchHolderViewShow(show: Boolean) {
        if (needReplace && mHolderView != null) {
            if (showHolder != null) {
                if (show) {
                    mHolderView!! replace mReplaceView!!
                } else {
                    mReplaceView!! replace mHolderView!!
                }
            }
        }
    }

    override fun showByState(state: Int) {
        trySwitchHolderViewShow(
            state != HolderView.state_content
                    && state != HolderView.state_normal
        )

        mHolderView?.showByState(state)
    }

    override fun showEmpty() {
        trySwitchHolderViewShow(true)

        mHolderView?.showEmpty()
    }

    override fun showError() {
        trySwitchHolderViewShow(true)

        mHolderView?.showError()
    }

    override fun showContent() {
        trySwitchHolderViewShow(false)

        mHolderView?.showContent()
    }

    override fun showLoading() {
        trySwitchHolderViewShow(true)

        mHolderView?.showLoading()
    }

    override fun showNetMiss() {
        trySwitchHolderViewShow(true)

        mHolderView?.showNetMiss()
    }

}