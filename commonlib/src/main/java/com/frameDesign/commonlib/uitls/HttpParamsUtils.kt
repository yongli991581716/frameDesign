package com.frameDesign.commonlib.uitls

import org.json.JSONObject

/**
 * http参数组包工具
 * @author liyong
 * @date 2019-10-22.
 */
object HttpParamsUtils {
    val LIST_VIEW_PAGE_SIZE = 20//单页加载条数
    val LIST_VIEW_PAGE_SIZE_MAX = 100//单页加载条数

    /**
     * 创建普通HTTP参数
     *
     * @param params 参数
     * @return JSONObject 返回json
     */
    fun createParams(vararg params: Pair<String, Any?>): JSONObject {
        val json = JSONObject()
        params.forEach {
            if (it.second is List<*>) {
                val ja = JsonUtils.listToJsonArray(it.second as List<*>)
                json.putOpt(it.first, ja)
            } else {
                json.putOpt(it.first, it.second)
            }
        }

        return json
    }

    /**
     * 创建默认数量分页HTTP参数
     *
     * @param params 参数
     * @return JSONObject 返回json
     */
    fun createPageParams(vararg params: Pair<String, Any?>): JSONObject {
        val json = createParams(*params)
        json.put("pageSize", LIST_VIEW_PAGE_SIZE)

        return json
    }

    /**
     * 创建动态数量分页HTTP参数
     *
     * @param pageCount 每页数量
     * @param params 参数
     * @return JSONObject 返回json
     */
    fun createPageParams(pageCount: Int, vararg params: Pair<String, Any?>): JSONObject {
        val json = createParams(*params)
        json.put("pageSize", pageCount)

        return json
    }

    /**
     * 创建HTTP对象参数
     *
     * @param any 参数
     * @return JSONObject 返回json
     */
    fun createObjParams(any: Any): JSONObject {
        return JSONObject(JsonUtils.toJsonString(any))
    }
}