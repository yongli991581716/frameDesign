package com.frameDesign.baselib.controller

import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.frameDesign.baselib.R
import com.frameDesign.baselib.model.bean.h5.H5RequestBean
import com.frameDesign.baselib.model.repository.test.HttpUrlRepository
import com.frameDesign.baselib.utils.h5.H5Helper
import com.frameDesign.baselib.utils.h5.H5Utils
import com.frameDesign.commonlib.expand.F2T
import com.frameDesign.commonlib.expand.currentTime
import com.frameDesign.commonlib.expand.fdToast
import com.frameDesign.commonlib.expand.isVisible
import com.frameDesign.commonlib.uitls.NetUtil
import com.frameDesign.commonlib.uitls.StringUtils
import com.github.lzyzsd.jsbridge.BridgeWebViewClient
import com.umeng.socialize.UMShareAPI
import kotlinx.android.synthetic.main.fragment_web_h5.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * webView详情展示界面
 *
 * @author liyong
 * @time 2019/1/24
 */
class WebH5Fragment : BaseFragment() {

    companion object {


        /**
         * 展示H5界面
         * @param alias String
         * @param parameterSelector HashMap<String, String>.() -> Unit
         * @return WebH5Fragment
         */
        fun newIns(alias: String, parameterSelector: HashMap<String, String>.() -> Unit): WebH5Fragment {
            val map = HashMap<String, String>()
                    .apply(parameterSelector)

            return newIns(alias, map)
        }

        /**
         * 展示H5界面
         * @param alias String
         * @return WebH5Fragment
         */
        fun newIns(alias: String, params: HashMap<String, String> = HashMap()): WebH5Fragment {
            return WebH5Fragment().apply {
                val bundle = Bundle()
                bundle.putString("alias", alias)
                bundle.putSerializable("data", params)

                arguments = bundle
            }
        }

    }

//    private var mShareParams: H5RequestBean.Param? = null

    @Volatile
    private var mCurrentUrl = ""
    private lateinit var mActionAlias: String
    private var mQueryMap = HashMap<String, String>()

    private var hasError = false
    private var lastTime = 0L
    private var retryOnce = AtomicBoolean()

    override fun getLayoutView(): Any =
            R.layout.fragment_web_h5

    override fun initDataBeforeView(view: View, savedInstanceState: Bundle?) {
        super.initDataBeforeView(view, savedInstanceState)

        val arguments = arguments ?: return

        mActionAlias = arguments.getString("alias") ?: ""
        val map = arguments.getSerializable("data")
                as? HashMap<String, String>

        if (map != null && map.isNotEmpty()) {
            mQueryMap = map
        }

        if (mActionAlias.isEmpty()) {
            fdToast("无效的数据传递")
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        web_content.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                if (title.contains("404")) {
                    showDataError()
                } else if (!title.isEmpty() && !StringUtils.isUrl(title)) {//title不为空，且非地址 从chrome取
                    // 显示标题
                }
            }

        }

        web_content.webViewClient = object : BridgeWebViewClient(web_content) {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (!hasError || currentTime() - lastTime > 240) {
                    showContent()
                }
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)

                if (retryOnce.F2T()) {
                    handler?.proceed()
                } else {
                    showDataError()
                }
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)

                showDataError()
            }

            override fun onReceivedError(view: WebView?,
                                         request: WebResourceRequest?,
                                         error: WebResourceError?) {
                super.onReceivedError(view, request, error)

                showDataError()
            }

        }

        H5Helper.processJsInterface(mActParent as BaseActivity, web_content) {
            showShareAlert(it.data!!)
        }
    }


    /**
     * 显示分享对话框
     * @param param H5RequestBean.Param
     */
    private fun showShareAlert(param: H5RequestBean.Param) {
        H5Helper.shareCommon(mActParent as BaseActivity, param)
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
        if (mCurrentUrl.isEmpty()) {
            HttpUrlRepository.obtainH5Action(mActionAlias)
                    .bindSub(onFinished = {
                        // 等待网页加载完成
                    }) {
//                        val bean = UserConfig.mActualUser
//                        if (bean != null) {
//                            mQueryMap["inviter"] = bean.peopleId
                            mCurrentUrl = if (mQueryMap.isNotEmpty()) {
                                H5Utils.encQueryUrl(it, mQueryMap)
                            } else it

                            web_content.loadUrl(mCurrentUrl)
//                        } else {
//                            fdToast("无效的用户ID, 请稍后再试")
//                            showDataError()
//                        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        UMShareAPI.get(mActParent).onActivityResult(requestCode, resultCode, data);
    }

    override fun onDestroyView() {
        try {
            web_content.loadDataWithBaseURL(null, "",
                    "text/html", "utf-8", null);
        } finally {
            web_content.clearHistory()
        }

        web_content.destroy()
        super.onDestroyView()
    }

}