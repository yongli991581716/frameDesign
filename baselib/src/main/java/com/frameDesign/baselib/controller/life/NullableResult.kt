package com.frameDesign.baselib.controller.life


/**
 * @desc   创建可空数据类型, 因为RxJava不能传递 null
 * ..
 * @author liyong
 * @date 2018/10/17
 */
data class NullableResult<T> private constructor(var result: T?) {

    companion object {

        /**
         * 创建带实际数据的数据模型
         * @param data T
         * @return NullableResult<T>
         */
        internal fun <T> create(data: T): NullableResult<T> {
            return NullableResult(data)
        }

        /**
         * 创建空数据的数据模型
         * @return NullableResult<T>
         */
        internal fun <T> empty(): NullableResult<T> {
            return NullableResult(null)
        }

    }

    fun notEmptyData(): Boolean {
        return result != null
    }

}