package com.frameDesign.commonlib.uitls

import android.content.Context
import android.content.SharedPreferences
import android.util.LruCache
import com.frameDesign.commonlib.CommHelper


/**
 * 存档管理
 * @author liyong
 * @date 2017-12-21.
 */
object PreferManager {

    private val prefreCache = LruCache<String, Prefer>(3)

    val ACTIONS = "ACTIONS"
    val H5_ACTIONS = "H5_ACTIONS"
    val ADDRESS_DATA = "ADDRESS_DATA"
    val SCAN_MEMBER_DATA = "SCAN_MEMBER_DATA"
    val LOGIN_INFO = "LOGIN_INFO"
    val SPLASH_CHECK_USED = "SPLASH_CHECK_USED"//是否使用过splash


    /**
     * 保存在手机里面的 默认 文件名
     */
    private const val COMM_FILE = "share_data_01"

    private var ctx = CommHelper.mCtx

    /**
     * 得到相应的存档
     * @return Prefer
     */
    fun getPrefer(): Prefer {
        return Prefer(null)
    }

    /**
     * 得到 相应的存档
     * @param fileName String?
     * @return Prefer
     */
    fun getPrefer(fileName: String?): Prefer {
        val spName = fileName ?: COMM_FILE
        LogUtils.d("getPrefer spName=$spName")

        if (prefreCache[spName] == null) {
            prefreCache.put(spName, Prefer(spName))
        }
        return prefreCache[spName]
    }

    fun removePrefer(fileName: String?) {
        prefreCache.remove(fileName ?: COMM_FILE)
    }

    /**
     * 具体存档
     */
    class Prefer(fileName: String?) {

        companion object {
            val USER_INFO = "USER_INFO"
        }


        var sp: SharedPreferences =
            ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE)

        /**
         * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
         */
        fun put(key: String, obj: Any?) {
            val editor = sp.edit()
            if (obj == null) {
                editor.remove(key)
            } else {
                when (obj) {
                    is String -> editor.putString(key, obj)
                    is Int -> editor.putInt(key, obj)
                    is Boolean -> editor.putBoolean(key, obj)
                    is Float -> editor.putFloat(key, obj)
                    is Long -> editor.putLong(key, obj)
                    else -> editor.putString(key, JsonUtils.toJsonString(obj))
                }
            }
            editor.apply()
        }

        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         */
        fun get(key: String, defaultObject: Any): Any? {
            return when (defaultObject) {
                is String -> sp.getString(key, defaultObject)
                is Int -> sp.getInt(key, defaultObject)
                is Boolean -> sp.getBoolean(key, defaultObject)
                is Float -> sp.getFloat(key, defaultObject)
                is Long -> sp.getLong(key, defaultObject)
                else -> null
            }

        }

        fun getString(key: String): String? {
            return getString(key, "")
        }

        fun getString(key: String, defaultObject: String): String? {
            val str = sp.getString(key, defaultObject)
            return str
        }

        fun getLong(key: String): Long {
            return getLong(key, 0)
        }

        fun getLong(key: String, defaultObject: Long): Long {
            val lg = sp.getLong(key, defaultObject)
            return lg
        }

        fun getInt(key: String): Int {
            return getInt(key, 0)
        }

        fun getInt(key: String, defaultObject: Int): Int {
            val lg = sp.getInt(key, defaultObject)
            return lg
        }

        fun getBoolean(key: String): Boolean {
            return getBoolean(key, false)
        }

        fun getBoolean(key: String, defaultObject: Boolean): Boolean {
            val lg = sp.getBoolean(key, defaultObject)
            return lg
        }

        inline fun <reified T : Any> getObject(key: String): T? {
            return try {
                val str = getString(key)
                JsonUtils.fromJson(str)
            } catch (e: Throwable) {
                e.printStackTrace()
                null
            }
        }

        /**
         * 移除某个key值已经对应的值
         */
        fun remove(key: String) {
            val editor = sp.edit()
            editor.remove(key)
            editor.apply()
        }

        /**
         * 清除所有数据
         */
        fun clear() {
            val editor = sp.edit()
            editor.clear()
            editor.apply()
        }

        /**
         * 查询某个key是否已经存在
         */
        fun contains(key: String): Boolean {
            return sp.contains(key)
        }

        /**
         * 返回所有的键值对
         */
        fun getAll(ctx: Context): Map<String, *> {
            return sp.all
        }
    }
}