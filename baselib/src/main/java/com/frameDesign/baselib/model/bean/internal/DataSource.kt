package com.frameDesign.baselib.model.bean.internal

import java.io.Serializable


/**
 * @desc  风格数据接口
 * ..
 * @author liyong
 * @date 2018/10/17
 */
interface DataSource<T> : DataEmpty, Serializable {

    /**
     * 获取实际数据
     * @return T
     */
    fun data(): T

    /**
     * 获取当前请求验证码
     * @return Int
     */
    fun code(): Int

    /**
     * 获取当前请求提示消息
     * @return String
     */
    fun message(): String

    /**
     * 判断当前请求是否请求成功
     * @return Boolean
     */
    fun success(): Boolean

}