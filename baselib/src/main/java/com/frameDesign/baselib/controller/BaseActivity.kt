package com.frameDesign.baselib.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import android.view.View
import com.frameDesign.baselib.R
import com.frameDesign.baselib.view.delegate.PageDelegate
import com.frameDesign.baselib.view.delegate.TitleDelegate
import com.frameDesign.commonlib.views.internal.IProgress
import com.frameDesign.baselib.model.bean.miss.EmptyDataMiss
import com.frameDesign.baselib.model.bean.miss.NetMiss
import com.frameDesign.baselib.view.HolderView
import com.frameDesign.commonlib.expand.zqColor
import com.frameDesign.commonlib.uitls.DialogUtils
import com.frameDesign.commonlib.uitls.NetUtil
import com.frameDesign.commonlib.views.dialog.FDProgressDialog
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.contentView
import org.jetbrains.anko.toast

/**
 * @desc  自定义activity基类
 * ..
 * @author liyong
 * @date 2018/10/17
 */
abstract class BaseActivity : BaseCommActivity(), IProgress {

    protected var mProgressAlert: FDProgressDialog? = null

    protected lateinit var mHoldDelegate: PageDelegate

    protected val mTitleDelegate by lazy(LazyThreadSafetyMode.NONE) {
        TitleDelegate(mActivity, this) {
            onBackPressed()
        }.also {
            it.setTitleLeftIcon(R.mipmap.common_ic_back_white)
        }
    }

    @SuppressLint("MissingPermission")
    override fun initDataBeforeView() {
        super.initDataBeforeView()

        mHoldDelegate = PageDelegate(this) {
            if (it == HolderView.state_netMiss) {
                if (NetUtil.isConnected()) {
                    retryLoadData()
                } else {
                    Snackbar.make(contentView!!, "网络连接异常, 请检查您的网络设置!", Snackbar.LENGTH_LONG)
                        .setAction("去设置") {
                            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                        }
                        .setActionTextColor(zqColor(R.color.colorPrimary))
                        .show()
                }
            } else {
                retryLoadData()
            }
        }
    }

    /**
     * 重试加载数据
     */
    protected open fun retryLoadData() {
        loadData()
    }

    //
    override fun bindHolderView(view: View) =
        mHoldDelegate.initHolderView(view)

    override fun showLoading() = mHoldDelegate.showLoading()

    override fun showContent() = mHoldDelegate.showContent()

    override fun showEmptyData() = mHoldDelegate.showEmpty()

    override fun showDataError() = mHoldDelegate.showError()

    override fun showNoNetwork() = mHoldDelegate.showNetMiss()

    override fun showViewByStatus(status: Int) = mHoldDelegate.showByState(status)

    override fun dispatchFailure(t: Throwable, msg: String) {
        if (!onInterceptErrorEvent(t, msg)) {
            when (t) {
                is NetMiss -> showNoNetwork()
                is EmptyDataMiss -> showEmptyData()

                else -> {
                    showDataError()

                    if (msg.isNotEmpty()) {
                        toast(msg)
                    }
                }
            }
        }
    }

    /**
     * 下发异常处理
     * @param t Throwable
     * @param msg String
     * @return Boolean true: 表示拦截异常
     *                 false: 表示不拦截, 交由默认处理
     */
    open fun onInterceptErrorEvent(t: Throwable, msg: String): Boolean = false

    override fun showProgress(msg: String) {
        if (mProgressAlert == null) {
            mProgressAlert = DialogUtils
                .createProgressDialog(this)
        }
        mProgressAlert?.setMessage(msg)
        mProgressAlert?.show()
    }

    override fun hideProgress() {
        mProgressAlert?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()

        hideProgress()
    }


}