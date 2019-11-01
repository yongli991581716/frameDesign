package com.frameDesign.baselib.model.repository

import com.frameDesign.baselib.const.HttpConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap

/**
 * 创建Retrofit工具类
 *
 * @author JustBlue
 * @time 2018/10/12
 */
object RetrofitHelp {

    // retrofit RxJava2适配器
    private val mCallAdapter = RxJava2CallAdapterFactory.create()

    private val mRetrofitBuild by lazy {
        Retrofit.Builder()
            .client(OkHttpHelp.createDefault())  // 添加OkHttp绑定
            .addConverterFactory(
                GsonConverterFactory
                    .create(JsonParserHelper.getCustomGson())
            )
            .addCallAdapterFactory(mCallAdapter)
            .baseUrl(HttpConfig.BASE_URL)
    }

    // service 实例缓存, 对于项目service默认只创建一个
    private val mServiceCaches = ConcurrentHashMap<String, Any>()

    /**
     * 默认Retrofit操作类
     */
    private val mDefaultRetrofit by lazy {
        mRetrofitBuild.build()
    }

    /**
     * 创建接口代理service的实例
     * @param cache 是否启用缓存
     * @return T
     */
    inline fun <reified T> getService(cache: Boolean = true): T =
        getService(T::class.java, cache)

    /**
     * 创建接口代理service的实例
     * @param clazz service的实际类型
     * @param cache 是否启用缓存
     * @return T
     */
    fun <T> getService(clazz: Class<T>, cache: Boolean = true): T {
        val cName = clazz.name // 获取缓存map的key

        if (cache) {
            var service = mServiceCaches[cName]
            if (service == null) {
                synchronized(this) {
                    service = mServiceCaches[cName]

                    if (service == null) {
                        // 创建并缓存service的实例
                        service = mDefaultRetrofit.create(clazz)
                        mServiceCaches[cName] = service!!
                    }

                    return service as T
                }
            } else {
                return service as T
            }
        } else {
            // 如未指定使用缓存, 创建时也不压入缓存
            return mDefaultRetrofit.create(clazz)
        }
    }

}