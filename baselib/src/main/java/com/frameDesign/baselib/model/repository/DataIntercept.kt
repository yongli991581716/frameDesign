package com.frameDesign.baselib.model.repository

import android.os.Build
import com.frameDesign.baselib.const.HttpConfig
import com.frameDesign.baselib.model.bean.miss.DefMiss
import com.frameDesign.baselib.model.bean.miss.LoginMiss
import com.frameDesign.baselib.model.bean.miss.NotFoundUrlMiss
import com.frameDesign.baselib.model.bean.miss.ResponseParseMiss
import com.frameDesign.baselib.utils.ActionsHelp
import com.frameDesign.commonlib.uitls.LogUtils
import com.frameDesign.commonlib.uitls.SystemUtil
import com.google.gson.JsonParser
import com.google.gson.stream.JsonToken
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

/**
 * 用于拦截okHttp请求
 *
 * @author JustBlue
 * @time 2018/10/12
 */
internal object DataIntercept : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val httpUrl = request.url
        val host = httpUrl.toUrl().host

        // 如果是获取全局url直接执行
        if (host in HttpConfig.globalApi.global_url ||
            host in HttpConfig.globalApi.global_h5_url ||
            host !in HttpConfig.BASE_URL
        ) {
            return chain.proceed(request)
        }

        val method = request.method

        var path = httpUrl.toUrl().path
            .replace("/", "")

        if (path.isNullOrEmpty()) {
            throw NullPointerException("url path is null")
        }

        val item = ActionsHelp.getApiByAlias(path)
            ?: throw NotFoundUrlMiss()

        val newReq = request.newBuilder()
            .fdApiUrl(httpUrl, item.href)
            .fdHeader(method)
            .build()

        val response = chain.proceed(newReq)
        return rebuildResponse(response)
    }

    /**
     * 重新构建服务器响应数据,
     * 已匹配restful接口设计模式
     * @param response Response
     */
    private fun rebuildResponse(response: Response): Response {
        val body = response.body ?: throw ResponseParseMiss()

        val type = body.contentType()
        val json = body.string()

        val isArray = json.startsWith("[")
                && json.endsWith("]")

        val newJson = when {
            isArray -> HttpConfig.restfulJson(json)
            isSuccessJson(json) -> HttpConfig.restfulJson(json)
            else -> {
                if (json.startsWith("{")
                    && json.endsWith("}")
                ) {
                    parseDefError(json)
                } else {
                    json
                }
            }
        }

        val newBody = ResponseBody.create(type, newJson)

        return response.newBuilder()
            .body(newBody)
            .build()
    }

    /**
     * 解析数据并抛出异常
     * @param json String
     * @return Nothing
     */
    private fun parseDefError(json: String): Nothing {
        val jp = JsonParser().parse(json).asJsonObject

        val code = jp.get("code").asInt
        val element = jp.get("message")

        val msg = if (element == JsonToken.NULL) {
            "服务器异常, 请稍后再试!"
        } else {
            val jsonMsg = element.asString

            if ("token无效" in jsonMsg) {
                throw LoginMiss()
            }

            //此处可作全局apiException处理封装（可选）
            jsonMsg
        }

        throw DefMiss(code, msg)
    }

    private fun isSuccessJson(json: String): Boolean {
        val isRestful = "code" !in json || "message" !in json

        return isRestful && "<html>" !in json
    }

    /**
     * 添加统一的系统header
     * @receiver Request.Builder
     * @return Request.Builder
     */
    private inline fun Request.Builder.fdHeader(method: String): Request.Builder {
        val vName = SystemUtil.getAppVersionName()
        val vCode = SystemUtil.getAppVersionCode()

        this.addHeader("version", vName ?: "")
        this.addHeader("osType", "Android")
        this.addHeader("version_code", "$vCode")
        this.addHeader("appName", "frameDesign")
        this.addHeader("Authorization", HttpConfig.getToken())
        this.addHeader("osVersion", "${Build.VERSION.SDK_INT}")

        if (method == HttpConfig.POST) {
            // 添加json请求
            this.header("Accept", "application/json")
            this.header("Content-Type", "application/json")
        }

        return this
    }

    /**
     * 封装新的url
     * @receiver Request.Builder
     * @param httpUrl HttpUrl
     * @param url String
     * @return Request.Builder
     */
    private inline fun Request.Builder.fdApiUrl(httpUrl: HttpUrl, url: String): Request.Builder {
        val newUrl = url.toHttpUrlOrNull()?.newBuilder() ?: throw NotFoundUrlMiss()

        val size = httpUrl.querySize

        for (i in 0 until size) {
            val k = httpUrl.queryParameterName(i)
            val v = httpUrl.queryParameterValue(i)

            newUrl.addQueryParameter(k, v)
        }

        val newUrlStr = newUrl.toString()

        LogUtils.v("OkHttp", "actual-url: $newUrlStr")

        return this.url(newUrl.build())
    }
}

