package com.frameDesign.baselib.const

import com.frameDesign.commonlib.uitls.LogUtils
import com.frameDesign.commonlib.uitls.sp.SPBase

/**
 * @desc  http配置常量
 * ..
 * @author liyong
 * @date 2018/10/17
 */
object HttpConfig : SPBase("fd-yx_gj-http") {
    const val GET = "GET"
    const val POST = "POST"

    const val SUCCESS_CODE = -1256

    private const val JSON_TEMP = "{\"code\":$SUCCESS_CODE,\"message\":\"获取成功\",\"data\":%1\$s}"
    private const val JSON_TEMP_NO_DATA = "{\"code\":$SUCCESS_CODE,\"message\":\"获取成功\"}"

    private const val DEF_TOKEN = "FD_DEF_TOKEN"

    @Volatile
    private var currentToken = DEF_TOKEN

    internal const val BASE_URL = "http://xxxx/"

    const val FLAVOR_DEV = "flavor_dev"
    const val FLAVOR_TEST = "flavor_test"
    const val FLAVOR_UAT = "flavor_uat"
    const val PUB = "pub"


    lateinit var globalApi: IApi

    fun bindEnvConfig(env: String) {
        globalApi = when (env) {
            FLAVOR_DEV -> DevApi // dev环境
            FLAVOR_TEST -> TestApi
            FLAVOR_UAT -> UatApi // uat环境
            else -> PubApi // 正式环境
        }
    }

    /**
     * 获取已存在token
     * @return String
     */
    fun getToken(): String {
        var current = currentToken

        if (current == DEF_TOKEN) {
            synchronized(DEF_TOKEN) {
                current = currentToken

                if (current == DEF_TOKEN) {
                    current = spu.getString(
                        DEF_TOKEN, ""
                    )

                    currentToken = current
                }
            }
        }

        LogUtils.d("currentToken=$currentToken")
        return currentToken
    }

    /**
     * 保存新的token
     * @param token String
     */
    fun setToken(token: String) {
        currentToken = token

        spu.put(DEF_TOKEN, token)
    }

    /**
     * 封装服务器返回的数据格式已满足restful数据结构
     * @param json String
     * @return String
     */
    fun restfulJson(json: String?): String {
        if (json.isNullOrEmpty()) {
            return JSON_TEMP_NO_DATA
        }

        return JSON_TEMP.format(json)
    }
}

interface IApi {

    val global_url: String

    val global_h5_url: String

}

internal object DevApi : IApi {

    override val global_url = "http://xxxx"

    override val global_h5_url = "http://xxx"

}

internal object TestApi : IApi {

    override val global_url = "http://xxx"

    override val global_h5_url = "http://xxx"

}

internal object UatApi : IApi {

    override val global_url = "https://xxx"

    override val global_h5_url = "https://xxx"

}

internal object PubApi : IApi {

    override val global_url = "https://XXX"

    override val global_h5_url = "https://XXX"

}