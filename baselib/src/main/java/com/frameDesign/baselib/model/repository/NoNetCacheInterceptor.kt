package com.frameDesign.baselib.model.repository

import com.frameDesign.commonlib.uitls.NetUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


/**
 * 无网络缓存拦截
 * @author liyong
 * @date 2019-11-03
 */
class NoNetCacheInterceptor : Interceptor {
    companion object {
        private const val OFFLINE_CAHCE_TIME = 60 * 60 * 24 * 30//30天
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val req: Request = if (NetUtil.isConnected()) {
            //有网络
            chain.request()
        } else {
            //无网络,检查30天内的缓存,即使是过期的缓存
            chain.request().newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=${OFFLINE_CAHCE_TIME}")
                .build()
        }
        return chain.proceed(req)
    }


}