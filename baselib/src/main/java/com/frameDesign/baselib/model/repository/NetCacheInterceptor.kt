package com.frameDesign.baselib.model.repository

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 网络缓存拦截器
 * @author liyong
 * @date 2019-11-03
 */
class NetCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var process = chain.proceed(request)
        //缓存策略，当请求头有Cache-Control，则遵循请求头缓存max-age规则
        if (!TextUtils.isEmpty(request.header("Cache-Control"))) {
            process = process.newBuilder()
                .header(
                    "Cache-Control",
                    request.header("Cache-Control")!!
                )//"Cache-Control", "public,max-age=xxx"
                //移除响应pragma，响应对象中有header对象，故先删除
                .removeHeader("pragma")
                .build()
        }

        return process
    }
}