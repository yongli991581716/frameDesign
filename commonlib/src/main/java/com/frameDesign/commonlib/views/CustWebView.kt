package com.frameDesign.commonlib.views

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebSettings
import com.frameDesign.commonlib.uitls.LogUtils
import com.github.lzyzsd.jsbridge.BridgeWebView


/**
 * 自定义webviewl
 *
 * @author liyong
 * @time 2018/11/25
 */
class CustWebView(context: Context?, attrs: AttributeSet?) : BridgeWebView(context, attrs) {

    init {
//        webSettings.setBlockNetworkImage(true);
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowFileAccess = true
        settings.builtInZoomControls = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
//        settings.setUserAgentString("ANDROID")
    }

    override fun loadUrl(url: String?) {
        super.loadUrl(url)
        LogUtils.d("load url=$url")
    }
}