package com.frameDesign.baselib.const

import com.frameDesign.commonlib.uitls.LogUtils
import com.frameDesign.commonlib.uitls.sp.SPBase

/**
 * @desc  http配置常量
 * ..
 * @author liyong
 * @date 2018/10/17
 */
object HttpConfig : SPBase("zq-yx_gj-http") {
    const val GET = "GET"
    const val POST = "POST"

    const val SUCCESS_CODE = -1256

    private const val JSON_TEMP = "{\"code\":$SUCCESS_CODE,\"message\":\"获取成功\",\"data\":%1\$s}"
    private const val JSON_TEMP_NO_DATA = "{\"code\":$SUCCESS_CODE,\"message\":\"获取成功\"}"

//    private const val DEF_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjMzAwYzgxNGZiN" +
//            "Dc3NGEzNDgxNTMzOWFmYjhjZTA0N2YiLCJhY2NvdW50SWQiOiJqYTE" +
//            "4Njg4NWEwZTA1NGNmNWJiZWZkNzFmZjcyNmRkZTQiLCJjb2RlIjpud" +
//            "WxsLCJjcmVhdGVkIjoxNTM5NjcyNjM4OTcyLCJuaWNrTmFtZSI6bnV" +
//            "sbCwicm9sZXMiOm51bGwsIm1vYmlsZSI6bnVsbCwiaWQiOiJjMzAwY" +
//            "zgxNGZiNDc3NGEzNDgxNTMzOWFmYjhjZTA0N2YiLCJhdmF0YXIiOiJ" +
//            "jMzAwYzgxNGZiNDc3NGEzNDgxNTMzOWFmYjhjZTA0N2YiLCJleHAiO" +
//            "jE1NzEyMDg2MzgsInVzZXJJZCI6ImMzMDBjODE0ZmI0Nzc0YTM0ODE" +
//            "1MzM5YWZiOGNlMDQ3ZiJ9.yDha5ALF5fFSOrVKC46xeaVB4L1ZbkP5" +
//            "vCu1FR5gKOgn8K4rcZrEcS-cvTa0cm4BDjY9CsPWFbcPI1duAlybfQ"

    private const val DEF_TOKEN = "ZQ_DEF_TOKEN"

    @Volatile
    private var currentToken = DEF_TOKEN

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