package com.frameDesign.baselib.model.repository

import android.Manifest.permission.ACCESS_NETWORK_STATE
import androidx.annotation.RequiresPermission
import com.frameDesign.baselib.model.bean.internal.DataSource
import com.frameDesign.baselib.model.bean.internal.PageParam
import com.frameDesign.baselib.model.bean.miss.DefMiss
import com.frameDesign.baselib.model.bean.miss.EmptyDataMiss
import com.frameDesign.baselib.model.bean.miss.NetMiss
import com.frameDesign.baselib.model.bean.miss.NotFoundUrlMiss
import com.frameDesign.baselib.model.repository.test.HttpUrlRepository
import com.frameDesign.baselib.utils.ActionsHelp
import com.frameDesign.commonlib.uitls.NetUtil
import com.frameDesign.commonlib.uitls.StringUtils
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.reflect.ParameterizedType
import java.util.concurrent.TimeUnit

/**
 * 接口仓库基类
 *
 * @author JustBlue
 * @time 2018/10/12
 */
open class BaseRepository<Service> {

    protected val service: Service by lazy {
        RetrofitHelp.getService(mClazz)
    }

    private val mClazz: Class<Service> by lazy {
        val clazz = this::class.java

        val superclass = clazz.genericSuperclass

        if (superclass is ParameterizedType) {
            return@lazy superclass.actualTypeArguments[0] as Class<Service>
        } else {
            throw IllegalArgumentException("obtain service class failure!")
        }
    }

    /**
     * 获取其他类型的接口
     * @return S
     */
    protected inline fun <reified S> obtain(): S {
        return RetrofitHelp.getService(S::class.java)
    }

    /**
     * 去基操作, 用于去掉数据结构[DataSource]业务封装层
     * @receiver Observable<DataSource<T>>
     * @return Observable<T>
     */
    protected fun <T> Observable<out DataSource<T>>.rebase(): Observable<T> {
        return this.flatMap {
            if (!it.success()) {
                return@flatMap resolveDataError(it)
            }

            if (it.emptyData()) { // 数据为空
                throw EmptyDataMiss()
                //但是在这里2种情况
                //1、在获取潜在渠道详情界面的时候 ，店铺数据可能为空 但是要显示企业
//                return@flatMap Observable.just(it.data())

                //2、渠道详情 删除店铺 返回的删除成功  但是 it.data()要报null
            }

            return@flatMap Observable.just(it.data())
        }
    }

    /**
     * 检查结果是否成功(只判断并返回是否成功, 同时会忽略原始信息)
     * 注: 一般用于只关心接口是否成功的接口; 且正常的数据一定为true
     * (因为RxJava需要一个返回值), 错误会一异常的形式下发
     *
     * @receiver Observable<out DefResult<T>>
     * @return Observable<Boolean>
     */
    protected fun <T> Observable<out DataSource<T>>.checkSuccess(): Observable<Boolean> {
        return this.map {
            return@map if (it.success()) {
                true
            } else {
                throw DefMiss(it.code(), it.message())
            }
        }
    }

    /**
     * 确认错误数据对应的异常
     * 注: 异常会被RxJava截获并下发
     *
     * @param it DataSource<T>
     * @return Nothing
     */
    private fun <T> resolveDataError(errorData: DataSource<T>): Nothing {
        var c = errorData.code()
        var m = errorData.message()

        if (otherError(m)) {
            c = 2000
            m = "服务器异常, 请稍后再试!"
        }

        throw DefMiss(c, m)
    }

    /**
     * 由服务器调用第三方服务回调的错误
     * @param message String?
     * @return Boolean
     */
    private fun otherError(message: String?): Boolean {
        return StringUtils.getLength(message) >= 100
                || message?.contains(" 404 ") == true
                || message?.contains(" 500 ") == true
                || message?.toLowerCase()?.contains("connection refused") == true
    }

    /**
     * 添加刷新api并重试网络请求
     * @receiver Observable<T>
     * @return Observable<T>
     */
    protected fun <T> Observable<T>.tryConnectApi(): Observable<T> {
        return this
            .startWith {
                // 检查是否已经加载api data(接口数据)
                if (ActionsHelp.hasApiActions()) {
                    Observable.empty<T>()
                } else {
                    Observable.error<T>(NotFoundUrlMiss())
                }.subscribe(it)
            }
            .retryWhen {
                var retryCount = 0
                return@retryWhen it.flatMap {
                    // 判断异常是否属于http异常, 且code在[400, 499](url异常)
                    val is404 = it is HttpException && it.code() in 400..406
                    val needUrl = (is404 || it is NotFoundUrlMiss) && retryCount++ == 0

                    return@flatMap if (needUrl) {
                        HttpUrlRepository.globalObtainUrls
                    } else {
                        Observable.error(it)
                    }
                }
            }
    }

    /**
     * 检查网路是否联通
     * @receiver Observable<T>
     * @return Observable<T>
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    protected fun <T> Observable<T>.checkNetwork(): Observable<T> {
        return this.startWith {
            if (NetUtil.isConnected()) {
                Observable.empty<T>()
            } else {
                Observable.error<T>(NetMiss())
            }.subscribe(it)
        }
    }

    /**
     * 将实际执行代码调度到异步执行
     * @receiver Observable<T>
     * @return Observable<T>
     */
    protected inline fun <T> Observable<T>.asyncWorker(): Observable<T> =
        this.subscribeOn(FDSchedulers.worker)

    /**
     * 封装上传json数据包
     * @param kvs Array<out Pair<String, Any>>
     * @return RequestBody
     */
    protected fun jsonParams(vararg kvs: Pair<String, Any?>): RequestBody {
        val jsonObject = JSONObject()

        for ((key, value) in kvs) {
            jsonObject.put(key, value)
        }

        val json = jsonObject.toString()

        val mediaType = "application/json; charset=utf-8"
            .toMediaTypeOrNull()
        return RequestBody.create(mediaType, json)
    }

    /**
     * 封装json字符串
     * @param block JSONObject.() -> Unit
     * @return RequestBody
     */
    protected fun jsonParams(block: JSONObject.() -> Unit): RequestBody {
        val jsonObject = JSONObject()

        jsonObject.block()

        val json = jsonObject.toString()

        val mediaType = "application/json; charset=utf-8"
            .toMediaTypeOrNull()
        return RequestBody.create(mediaType, json)
    }

    /**
     * 通过map封装数据
     * @param block HashMap<String, Any>.() -> Unit
     * @return RequestBody
     */
    fun jsonMapParams(block: HashMap<String, Any>.() -> Unit): RequestBody {
        val map = HashMap<String, Any>()

        map.block()

        val json = JsonParserHelper
            .getCustomGson().toJson(map)

        val mediaType = "application/json; charset=utf-8"
            .toMediaTypeOrNull()
        return RequestBody.create(mediaType, json)
    }

    /**
     * 封装json字符串
     * @param block JSONObject.() -> Unit
     * @return RequestBody
     */
    protected fun jsonPageParams(param: PageParam, block: JSONObject.() -> Unit): RequestBody {
        val jsonObject = JSONObject()

        jsonObject.put("pageSize", param.size)
        jsonObject.put("pageIndex", param.page)

        jsonObject.block()

        val json = jsonObject.toString()

        val mediaType = "application/json; charset=utf-8"
            .toMediaTypeOrNull()
        return RequestBody.create(mediaType, json)
    }

    /**
     * 封装 get请求查询条件
     * @param param PageParam
     * @param block HashMap<String, Any>.() -> Unit
     * @return HashMap<String, Any>
     */
    protected fun queryPageParams(
        param: PageParam,
        block: HashMap<String, Any>.() -> Unit = {}
    ): HashMap<String, Any> {
        return HashMap<String, Any>()
            .also {
                it["pageSize"] = param.size
                it["pageIndex"] = param.page
            }.apply(block)
    }

    /**
     * 封装 get请求查询条件
     * @param param PageParam
     * @param block HashMap<String, Any>.() -> Unit
     * @return HashMap<String, Any>
     */
    protected fun queryParams(block: HashMap<String, Any>.() -> Unit): HashMap<String, Any> {
        return HashMap<String, Any>().apply(block)
    }

    /**
     * 合并部分常用操作
     * [tryConnectApi] 检查url是否可以, 并添加url刷新
     * [checkNetwork]  检查网路是否可用
     * [asyncWorker]   调度线程到自定义线程执行
     * @receiver Observable<T>
     * @return Observable<T>
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    protected fun <T> Observable<T>.unitConnect(): Observable<T> =
        this.tryConnectApi()
            .checkNetwork()
            .asyncWorker()
            // 防止接口快速返回数据, 导致界面频繁刷新(从而导致闪屏)
            .delay(300, TimeUnit.MILLISECONDS, true)

}
