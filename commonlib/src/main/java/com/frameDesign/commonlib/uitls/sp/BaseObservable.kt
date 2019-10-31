package com.frameDesign.commonlib.uitls.sp

import com.google.gson.Gson
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val LOG_TAG = "SP_Data"

abstract class BaseObservable<T> : ReadWriteProperty<SPBase, T>, ReadOnlyProperty<SPBase, T> {

    protected var data: T? = null
    protected var name: String = ""
    protected var mode: Int = SPMode.MODE_CACHE

    /**
     * 解析[property]生成偏好设置 name和mode
     *
     * 注: 如[SPBase]的属性未添加[SPProperty], 则使用默认规则
     *
     * @param property
     * @return (name, mode)
     */
    fun obtainSaveKM(property: KProperty<*>): Pair<String, Int> {
        if (name.isEmpty()) {
            name = makeKeyName(property)

            if (name.isEmpty()) {
                throw IllegalArgumentException("name must be not empty")
            }
        }

        return name to mode
    }

    /**
     * 生成sp保存的key
     *
     * @param property KProperty<*>
     * @return String
     */
    protected open fun makeKeyName(property: KProperty<*>): String {
        if (name.isEmpty()) {
            name = "sp-key-${property.name}"
        }
        return name
    }

    /**
     * 根据指定类型执行相应代码
     * @param runGetValue () -> T?
     * @return T?
     */
    protected inline fun runGetValueByMode(runGetValue: () -> T?): T? {
        return when (mode) {
            SPMode.MODE_CACHE -> {
                if (data == null)
                    data = kotlin.run(runGetValue)

                data
            }
            else -> {
                data = kotlin.run(runGetValue)
                data
            }
        }
    }

    open fun reset() {
        data = null
    }

}

interface SPAction<T> {

    fun getActualValue(spu: SPUtils, keyName: String): T

    fun setActualValue(spu: SPUtils, keyName: String, value: T)
}

abstract class NonNullObservable<T> : BaseObservable<T>(), SPAction<T> {

    abstract val defValue: T /* 非空类型, 必须添加默认值 */

    override fun setValue(thisRef: SPBase, property: KProperty<*>, value: T) {
        this.data = value

        val (name, _) = obtainSaveKM(property)

        setActualValue(thisRef.spu, name, value)
    }

    override fun getValue(thisRef: SPBase, property: KProperty<*>): T {
        if (this.data != null) return data!!

        val (name, _) = obtainSaveKM(property)

        return runGetValueByMode {
            getActualValue(thisRef.spu, name)
        }!!
    }

}

abstract class NullableObservable<T> : BaseObservable<T?>(), SPAction<T?> {

    override fun setValue(thisRef: SPBase, property: KProperty<*>, value: T?) {
        this.data = value

        val (name, _) = obtainSaveKM(property)

        setActualValue(thisRef.spu, name, value)
    }

    override fun getValue(thisRef: SPBase, property: KProperty<*>): T? {
        if (this.data != null) return data!!

        val (name, _) = obtainSaveKM(property)

        return runGetValueByMode {
            getActualValue(thisRef.spu, name)
        }
    }

}

class DataObservable<T>(
    override val defValue: T,
    private val clazz: Class<T>,
    val encrypt: Boolean
) : NonNullObservable<T>() {

    override fun getActualValue(spu: SPUtils, keyName: String): T {
        val data = spu.getString(
            keyName, ""
        )

        if (data.isEmpty()) return defValue

        return Gson().fromJson(data, clazz)
    }

    override fun setActualValue(spu: SPUtils, keyName: String, value: T) =
        spu.put(keyName, Gson().toJson(value))

}

class AnyObservable<T>(private val clazz: Class<T>, val encrypt: Boolean) :
    NullableObservable<T>() {

    override fun getActualValue(spu: SPUtils, keyName: String): T? {
        val data = spu.getString(
            keyName, ""
        )

        if (data.isEmpty()) return null

        return try {
            Gson().fromJson(data, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun setActualValue(spu: SPUtils, keyName: String, value: T?) {
        data = value

        if (value != null) {
            val json = Gson().toJson(value)

            spu.put(keyName, json)
        } else {
            spu.put(keyName, null as? String)
        }
    }

}

class BooleanObservable(override val defValue: Boolean) : NonNullObservable<Boolean>() {

    override fun getActualValue(spu: SPUtils, keyName: String): Boolean =
        spu.getBoolean(keyName, defValue)

    override fun setActualValue(spu: SPUtils, keyName: String, value: Boolean) =
        spu.put(keyName, value)

}

class StringObservable(override val defValue: String, val encrypt: Boolean) :
    NonNullObservable<String>() {

    override fun getActualValue(spu: SPUtils, keyName: String): String =
        spu.getString(keyName, defValue)

    override fun setActualValue(spu: SPUtils, keyName: String, value: String) =
        spu.put(keyName, value)

}

class IntObservable(override val defValue: Int) : NonNullObservable<Int>() {

    override fun getActualValue(spu: SPUtils, keyName: String): Int = spu.getInt(keyName, defValue)

    override fun setActualValue(spu: SPUtils, keyName: String, value: Int) = spu.put(keyName, value)

}

class LongObservable(override val defValue: Long) : NonNullObservable<Long>() {

    override fun getActualValue(spu: SPUtils, keyName: String): Long =
        spu.getLong(keyName, defValue)

    override fun setActualValue(spu: SPUtils, keyName: String, value: Long) =
        spu.put(keyName, value)

}

class FloatObservable(override val defValue: Float) : NonNullObservable<Float>() {

    override fun getActualValue(spu: SPUtils, keyName: String): Float =
        spu.getFloat(keyName, defValue)

    override fun setActualValue(spu: SPUtils, keyName: String, value: Float) =
        spu.put(keyName, value)

}

