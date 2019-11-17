package com.frameDesign.baselib.controller

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.*
import com.frameDesign.baselib.R
import com.frameDesign.baselib.model.bean.h5.H5RequestBean
import com.frameDesign.baselib.model.repository.test.HttpUrlRepository
import com.frameDesign.baselib.utils.h5.H5Helper
import com.frameDesign.baselib.utils.h5.H5Utils
import com.frameDesign.commonlib.expand.F2T
import com.frameDesign.commonlib.expand.currentTime
import com.frameDesign.commonlib.expand.isVisible
import com.frameDesign.commonlib.uitls.NetUtil
import com.frameDesign.commonlib.uitls.StringUtils
import com.github.lzyzsd.jsbridge.BridgeWebViewClient
import com.umeng.socialize.UMShareAPI
import kotlinx.android.synthetic.main.activity_service_h5.*
import org.jetbrains.anko.toast
import java.util.concurrent.atomic.AtomicBoolean

/**
 * web服务界面
 *
 * @author liyong
 * @time 2018/11/8
 */
open class WebH5Activity : BaseActivity() {

    companion object {

        /**
         * 跳转web展示界面
         * @param act
         * @param alias 接口别名
         * @param share 是否分享
         * @param queryMap 查询参数
         */
        fun start(
            act: Activity,
            alias: String,
            share: Boolean = false,
            parameterSelector: HashMap<String, String>.() -> Unit
        ) {
            val map = HashMap<String, String>()
                .apply(parameterSelector)

            start(act, alias, share, map)
        }

        /**
         * 跳转web展示界面
         * @param act
         * @param alias 接口别名
         * @param share 是否分享
         * @param queryMap 查询参数
         */
        fun start(
            act: Activity,
            alias: String,
            share: Boolean = false,
            queryMap: HashMap<String, String> = hashMapOf()
        ) {
            val extras = Bundle()
            extras.putString("alias", alias)
            extras.putBoolean("share", share)
            extras.putSerializable("data", queryMap)

            act.startActivity(Intent(act, WebH5Activity::class.java)
                .apply { this.putExtras(extras) })
        }
    }

    override fun getLayoutView(): Any =
        R.layout.activity_service_h5

    protected var mShareParams: H5RequestBean.Param? = null

    @Volatile
    private var mCurrentUrl = ""
    private lateinit var mActionAlias: String
    private var mQueryMap = HashMap<String, String>()

    private var hasError = false
    private var lastTime = 0L
    private var retryOnce = AtomicBoolean()

    override fun initDataBeforeView() {
        super.initDataBeforeView()

        mActionAlias = intent.getStringExtra("alias") ?: ""
        val map = intent.getSerializableExtra("data")
                as? HashMap<String, String>

        if (map != null && map.isNotEmpty()) {
            mQueryMap = map
        }

        if (mActionAlias.isEmpty()) {
            toast("无效的数据传递")
            finish()
        }
    }

    override fun initView(state: Bundle?) {
        mTitleDelegate.switchWhiteTheme()
        mTitleDelegate.setLineVisible(false)
        mTitleDelegate.setTitleContent("产品介绍")

        web_content.webChromeClient = object : WebChromeClient() {

            private var mCustomView: View? = null
            private var mCustomViewCallback: CustomViewCallback? = null

//            override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                super.onProgressChanged(view, newProgress)
//                LogUtils.d("newProgress=$newProgress")
//                if (newProgress >= 100) {
//                    pbar_layout?.visibility = View.INVISIBLE
//                } else {
//                    pbar_layout?.visibility = View.VISIBLE
//                }
//            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                if (title.contains("404")) {
//                    showRetryErrorView(web_view)
                    showDataError()
                } else if (!title.isEmpty() && !StringUtils.isUrl(title)) {//title不为空，且非地址 从chrome取
                    mTitleDelegate.setTitleContent(title)
                }
            }

            /**
             * 视频横竖屏切换
             */
            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                super.onShowCustomView(view, callback)
                if (mCustomView != null) {
                    callback.onCustomViewHidden()
                    return
                }
                mCustomView = view
                root_layout.addView(mCustomView)
                root_layout.setBackgroundResource(R.color.colorBlack)

                content_layout.visibility = View.GONE

                mCustomViewCallback = callback
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            override fun onHideCustomView() {
                content_layout.visibility = View.VISIBLE
                if (mCustomView == null) {
                    return
                }
//                mCustomView?.visibility = View.GONE
                root_layout.removeView(mCustomView)
                mCustomView = null
                root_layout.setBackgroundResource(R.color.colorWhite)

                mCustomViewCallback?.onCustomViewHidden()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                super.onHideCustomView()
            }

        }

        web_content.webViewClient = object : BridgeWebViewClient(web_content) {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (!hasError || currentTime() - lastTime > 240) {
                    showContent()
                }
            }

//            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
//                super.onReceivedHttpError(view, request, errorResponse)
//
//                if (retryOnce.compareAndSet(false, true)) {
//                    view?.loadUrl(mCurrentUrl)
//                } else {
//                    showDataError()
//                }
//            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)

                if (retryOnce.F2T()) {
                    handler?.proceed()
                } else {
                    showDataError()
                }
//                showDataError()
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)

                showDataError()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)

                showDataError()
            }

        }

        H5Helper.processJsInterface(this, web_content)
    }

    fun showShareAlert(param: H5RequestBean.Param) {
        H5Helper.shareCommon(this, param)
    }

    override fun showNoNetwork() {
        super.showNoNetwork()

        web_content.isVisible = false
    }

    override fun showLoading() {
        super.showLoading()

        web_content.isVisible = false
    }

    override fun showDataError() {
        super.showDataError()

        web_content.isVisible = false

        lastTime = currentTime()
        hasError = true
    }

    override fun showContent() {
        super.showContent()

        web_content.isVisible = true
    }

    override fun loadData() {
        retryOnce.lazySet(false)

        try {
            web_content.clearHistory()
            web_content.clearMatches()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        if (NetUtil.isConnected()) {
            showLoading()

            actualLoadData()
        } else {
            showNoNetwork()
        }
    }

    private fun actualLoadData() {
        //TODO 根据具体项目场景
        if (mCurrentUrl.isEmpty()) {
            HttpUrlRepository.obtainH5Action(mActionAlias)
                .bindSub(onFinished = {
                    // 等待网页加载完成
                }, onFailure = {

                }) {
                    mCurrentUrl = if (mQueryMap.isNotEmpty()) {
                        H5Utils.encQueryUrl(it, mQueryMap)
                    } else it

                    web_content.loadUrl(mCurrentUrl)
                }
        } else {
            web_content.loadUrl(mCurrentUrl)
        }
    }

    override fun onResume() {
        super.onResume()

        web_content.onResume()
    }

    override fun onPause() {
        super.onPause()

        web_content.onPause()
    }

    /**
     * 横竖屏切换
     */
    override fun onConfigurationChanged(config: Configuration) {
        super.onConfigurationChanged(config)
        when (config.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    override fun onDestroy() {
        try {
            web_content.loadDataWithBaseURL(
                null, "",
                "text/html", "utf-8", null
            );
        } finally {
            web_content.clearHistory()
        }

        super.onDestroy()

        web_content.destroy()
    }

    fun sendData(data: H5RequestBean.Param?) {
        this.mShareParams = data

        this.showShareBtn()
    }

    open fun showShareBtn() {
        mTitleDelegate.setTitleRightIcon(R.mipmap.ic_fenx)
        mTitleDelegate.doRightClick {
            val param = mShareParams
            if (param == null) {
                toast("分享数据异常!")
                return@doRightClick
            }
            showShareAlert(param)
        }
    }
}
