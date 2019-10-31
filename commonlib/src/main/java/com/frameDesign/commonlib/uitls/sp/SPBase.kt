package com.frameDesign.commonlib.uitls.sp


object DefVal {
    const val DEF_INT = 0
    const val DEF_LONG = 0L
    const val DEF_FLOAT = 0F
    const val DEF_STRING = ""
    const val DEF_DOUBLE = 0.0
    const val DEF_BOOLEAN = false
}

/**
 * 自定义SharedPreferences存储模式
 */
object SPMode {
    /**
     * 在第一次取值或每一次存值的时候, 同步更新[BaseObservable.data]
     * 在重复取值时, 会先判断[BaseObservable.data]是否有值, 有则直接返回
     * 类似于java享元设计模式
     *
     * 注: 当不是通过代理属性更新SharedPreferences时, 无法同步更新
     */
    const val MODE_CACHE = 0

    /**
     * 每次都是从SharedPreferences取值
     */
    const val MODE_RELOAD = 1
}

open class SPBase(spName: String) {

    val spu = SPUtils.getInstance(spName)

    val mObservables = mutableListOf<BaseObservable<*>>()

    /**
     * 通过json保存实体, 可空
     * @return AnyObservable<T>
     */
    inline fun <reified T> _any() = AnyObservable(T::class.java, false)
        .apply { mObservables.add(this) }

    /**
     * 通过json保存实体, 不可空, 必须传入默认参数
     * @param defValue T
     * @return DataObservable<T>
     */
    inline fun <reified T> _any(defValue: T) = DataObservable(defValue, T::class.java, false)
        .apply { mObservables.add(this) }

    inline fun _int(defValue: Int = DefVal.DEF_INT) = IntObservable(defValue)
        .apply { mObservables.add(this) }

    inline fun _long(defValue: Long = DefVal.DEF_LONG) = LongObservable(defValue)
        .apply { mObservables.add(this) }

    inline fun _float(defValue: Float = DefVal.DEF_FLOAT) = FloatObservable(defValue)
        .apply { mObservables.add(this) }

    inline fun _string(defValue: String = DefVal.DEF_STRING, encrypt: Boolean = false) =
        StringObservable(defValue, encrypt)
            .apply { mObservables.add(this) }

    inline fun _boolean(defValue: Boolean = DefVal.DEF_BOOLEAN) = BooleanObservable(defValue)
        .apply { mObservables.add(this) }

    open fun clear() {
        spu.clear()

        for (ob in mObservables) {
            ob.reset()
        }
    }

}