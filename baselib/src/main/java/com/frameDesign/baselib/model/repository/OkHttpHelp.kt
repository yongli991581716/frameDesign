package com.frameDesign.baselib.model.repository

import com.frameDesign.commonreslib.const.ConstConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * 用于创建okHttp客户端操作类
 *
 * @author JustBlue
 * @time 2018/10/12
 */
internal object OkHttpHelp {

    const val TIMEOUT_UPLOAD = 45_000L  // 上传图片超时时间
    const val TIMEOUT_DEFAULT = 15_000L // 普通请求超时时间

    private fun create(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_DEFAULT, TimeUnit.MILLISECONDS)
            .addInterceptor(getLogIntercept())
            .sslSocketFactory(HttpsTrustManager.createSSLSocketFactory(), HttpsTrustManager())
    }

    /**
     * 获取okHttp日志拦截类
     * @return Interceptor
     */
    private fun getLogIntercept(): Interceptor {
        return HttpLoggingInterceptor().also {
            it.level = if (ConstConfig.FD_DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * 创建默认OkHttp客户端操作类
     * @return OkHttpClient
     */
    fun createDefault(): OkHttpClient =
        create().writeTimeout(TIMEOUT_DEFAULT, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_DEFAULT, TimeUnit.MILLISECONDS)
            .addInterceptor(DataIntercept)
            .build()

    /**
     * 创建默认上传文件的客户端操作类
     * @return OkHttpClient
     * @deprecated 优化后超时时间放在[DataIntercept]中自动设置
     */
    @Deprecated("")
    fun createUpload(): OkHttpClient =
        create().writeTimeout(TIMEOUT_UPLOAD, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_UPLOAD, TimeUnit.MILLISECONDS)
            .addInterceptor(DataIntercept)
            .build()

}