package com.frameDesign.baselib.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import android.view.View
import com.frameDesign.baselib.R
import com.frameDesign.baselib.model.bean.DefResult
import com.frameDesign.baselib.model.bean.internal.ErrorBean
import com.frameDesign.baselib.model.bean.miss.EmptyDataMiss
import com.frameDesign.baselib.model.bean.miss.NetMiss
import com.frameDesign.baselib.utils.expand.timerEvent
import com.frameDesign.baselib.view.HolderView
import com.frameDesign.baselib.view.delegate.PageDelegate
import com.frameDesign.baselib.view.delegate.TitleDelegate
import com.frameDesign.commonlib.expand.DEF_FUN_1
import com.frameDesign.commonlib.expand.fdColor
import com.frameDesign.commonlib.expand.fdToast
import com.frameDesign.commonlib.expand.runUIThread
import com.frameDesign.commonlib.uitls.DialogUtils
import com.frameDesign.commonlib.uitls.NetUtil
import com.frameDesign.commonlib.views.dialog.FDProgressDialog
import com.frameDesign.commonlib.views.internal.IProgress
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import org.jetbrains.anko.contentView

/**
 * @desc  自定义activity基类
 * ..
 * @author liyong
 * @date 2018/10/17
 */
abstract class BaseActivity : BaseCommActivity(), IProgress {

    protected var mProgressAlert: FDProgressDialog? = null

    protected lateinit var mPageDelegate: PageDelegate

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

        mPageDelegate = PageDelegate(this) {
            if (it == HolderView.state_netMiss) {
                if (NetUtil.isConnected()) {
                    retryLoadData()
                } else {
                    Snackbar.make(contentView!!, "网络连接异常, 请检查您的网络设置!", Snackbar.LENGTH_LONG)
                        .setAction("去设置") {
                            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                        }
                        .setActionTextColor(fdColor(R.color.colorPrimary))
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
        mPageDelegate.initHolderView(view)

    override fun showLoading() = mPageDelegate.showLoading()

    override fun showContent() = mPageDelegate.showContent()

    override fun showEmptyData() = mPageDelegate.showEmpty()

    override fun showDataError() = mPageDelegate.showError()

    override fun showNoNetwork() = mPageDelegate.showNetMiss()

    override fun showViewByStatus(status: Int) = mPageDelegate.showByState(status)

    override fun dispatchFailure(t: Throwable, msg: String) {
        if (!onInterceptErrorEvent(t, msg)) {
            when (t) {
                is NetMiss -> showNoNetwork()
                is EmptyDataMiss -> showEmptyData()

                else -> {
                    showDataError()

                    if (msg.isNotEmpty()) {
                        fdToast(msg)
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

    fun delayHideProgress() {
        timerEvent(1000)
            .bindDestroy()
            .subscribe {
                hideProgress()
            }
    }
}