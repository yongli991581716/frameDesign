package com.frameDesign.baselib.model.repository

import com.frameDesign.commonlib.uitls.FileUtils
import com.frameDesign.commonreslib.const.ConstConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * 用于创建okHttp客户端操作类
 *
 * @author liyong
 * @time 2018/10/12
 */
internal object OkHttpHelp {
    private const val CACHE_FILE_NAME = "fd2_cache"
    // 缓存目录
    private val CAChE_FILE = File(FileUtils.getCachePath(CACHE_FILE_NAME))
    // 缓存大小
    private var CAChE_SIZE = 10 * 1024 * 1024L
    private const val TIMEOUT_UPLOAD = 45_000L  // 上传图片超时时间
    private const val TIMEOUT_DEFAULT = 15_000L // 普通请求超时时间

    private fun create(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_DEFAULT, TimeUnit.MILLISECONDS)
            .addInterceptor(getLogIntercept())
        //TODO 此处加上ssl数字签名校验，导致无法缓存
        //.sslSocketFactory(HttpsTrustManager.createSSLSocketFactory(), HttpsTrustManager())
        //.hostnameVerifier(HttpsTrustManager.TrustAllHostnameVerifier())
    }

    /**
     * 获取缓存对象（缓存文件及大小）
     */
    private fun getCache(): Cache {
        return Cache(CAChE_FILE, CAChE_SIZE)
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
        create()
            .writeTimeout(TIMEOUT_DEFAULT, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_DEFAULT, TimeUnit.MILLISECONDS)
            .addInterceptor(DataIntercept)
            //若不需要缓存，则注释掉以下三行
            //1、cache(getCache())
            //2、addInterceptor(NoNetCacheInterceptor())
            //3、addNetworkInterceptor((NetCacheInterceptor()))
            .cache(getCache())//缓存文件及缓存大小
            .addInterceptor(NoNetCacheInterceptor())//无网络时缓存拦截器
            .addNetworkInterceptor((NetCacheInterceptor()))//有网络时缓存拦截器
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