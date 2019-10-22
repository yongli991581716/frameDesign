package com.frameDesign.commonlib.uitls

import com.google.gson.Gson
import org.json.JSONArray
import java.lang.reflect.Type

/**
 * json序列化/反序列化工具
 * @author liyong
 * @date 2019-10-22.
 */
object JsonUtils {


    /**
     * 泛型通过GSON转化json
     *
     * @param t 参数
     * @return
     */
    inline fun <reified T : Any> cloneObject(t: T?): T? {
        if (t == null) {
            return null
        }

        val str = toJsonString(t)

        return fromJson(str)
    }

    /**
     * 对象转化json
     *
     * @param t 参数
     * @return
     */
    fun <T> toJsonString(t: T?): String? {
        if (t == null) {
            return null
        }

        return Gson().toJson(t)
    }

    /**
     * json转化对象
     *
     * @param json 参数
     * @return
     */
    inline fun <reified T : Any> fromJson(json: String?): T? {
        if (StringUtils.isEmpty(json)) {
            return null
        }
        return Gson().fromJson(json, T::class.java)
    }

    /**
     * json转化集合对象
     *
     * @param str 字符串
     * @param type 对象类型
     * @return
     */
    fun <T> toListObject(str: String?, type: Type): List<T>? {
//        val type = object : TypeToken<List<T>>() {}.type
        if (str.isNullOrEmpty()) {
            return null
        }

        return Gson().fromJson(str, type)
    }

    /**
     * 集合对象转化jsonArray
     *
     * @param lst 参数
     * @return
     */
    fun <T> listToJsonArray(lst: List<T>?): JSONArray? {
        if (lst != null) {
            val ja = JSONArray()
            lst.forEach {
                ja.put(it)
            }

            return ja
        }

        return null
    }

    /**
     * string转化为map对象
     *
     * @param json 参数
     * @return
     */
    fun <K, V> toMapObject(str: String?, type: Type): Map<K, V>? {
//        val type = object : TypeToken<Map<K, V>>() {}.type
        if (str.isNullOrEmpty()) {
            return null
        }

        return Gson().fromJson(str, type)
    }
}
