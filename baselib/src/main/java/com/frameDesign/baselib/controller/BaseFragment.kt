package com.frameDesign.baselib.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.CallSuper
import com.frameDesign.baselib.R
import com.frameDesign.baselib.view.delegate.PageDelegate
import com.frameDesign.commonlib.views.internal.IProgress
import com.frameDesign.baselib.model.bean.miss.EmptyDataMiss
import com.frameDesign.baselib.model.bean.miss.NetMiss
import com.frameDesign.baselib.model.bean.DefResult
import com.frameDesign.baselib.model.bean.internal.ErrorBean
import com.frameDesign.baselib.view.HolderView
import com.frameDesign.commonlib.expand.DEF_FUN_1
import com.frameDesign.commonlib.expand.runUIThread
import com.frameDesign.commonlib.expand.toast
import com.frameDesign.commonlib.expand.zqColor
import com.frameDesign.commonlib.uitls.DialogUtils
import com.frameDesign.commonlib.uitls.NetUtil
import com.frameDesign.commonlib.views.dialog.FDProgressDialog
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable

/**
 * @desc  自定义Base基类
 * ..
 * @author liyong
 * @date 2018/10/17
 */
abstract class BaseFragment : BaseCommFragment(), IProgress {

    protected var mHoldDelegate: PageDelegate? = null
    protected var mProgressAlert: FDProgressDialog? = null

    @CallSuper
    @SuppressLint("MissingPermission")
    override fun initDataBeforeView(view: View, savedInstanceState: Bundle?) {
        super.initDataBeforeView(view, savedInstanceState)

        mHoldDelegate = PageDelegate(this) {
            if (it == HolderView.state_netMiss) {
                if (NetUtil.isConnected()) {
                    retryLoadData()
                } else {
                    Snackbar.make(view, "网络连接异常, 请检查您的网络设置!", Snackbar.LENGTH_LONG)
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

    override fun bindHolderView(view: View) {
        mHoldDelegate?.initHolderView(view)
    }

    override fun showLoading() {
        mHoldDelegate?.showLoading()
    }

    override fun showContent() {
        mHoldDelegate?.showContent()
    }

    override fun showEmptyData() {
        mHoldDelegate?.showEmpty()
    }

    override fun showDataError() {
        mHoldDelegate?.showError()
    }

    override fun showNoNetwork() {
        mHoldDelegate?.showNetMiss()
    }

    override fun showViewByStatus(status: Int) {
        mHoldDelegate?.showByState(status)
    }

    override fun dispatchFailure(t: Throwable, msg: String) {
        if (!onInterceptErrorEvent(t, msg)) {
            if (msg.isNotEmpty()) {
                toast(msg)
            }
            when (t) {
                is NetMiss -> showNoNetwork()
                is EmptyDataMiss -> showEmptyData()
                else -> showDataError()
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
                .createProgressDialog(activity)
        }
        mProgressAlert?.setMessage(msg)

        if (mProgressAlert?.isShowing == false) {
            mProgressAlert?.show()
        }
    }

    override fun hideProgress() {
        if (mProgressAlert?.isShowing == true) {
            mProgressAlert?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        hideProgress()
        mProgressAlert = null
    }

    /**
     * 添加界面绑定的事件注册
     * @receiver Observable<T>
     * @param onFinished  处理终止事件(包括正常完成\异常导致的终止, 两个诱因为独立的)
     * @param onFailure   处理异常事件, 默认调用[dispatchErrorEvent]
     * @param onSuccess   处理响应数据, 必须添加
     */
    protected fun <T> Observable<T>.bindSub(
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
                        dispatchFailure(e, msg)
                    } else {
                        onFailure(eBean)
                    }
                }

                override fun doSuccess(data: T) = onSuccess(data)
            })

}