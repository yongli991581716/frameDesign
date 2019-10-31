package com.frameDesign.baselib.model.bean.internal

/**
 * @desc  空数据基类
 * ..
 * @author liyong
 * @date 2018/10/17
 */
interface DataEmpty {

    /**
     * 判断数据是否为空
     * @return Boolean
     */
    fun emptyData(): Boolean = !notEmptyData()

    /**
     * 判断数据是否不为空
     * @return Boolean
     */
    fun notEmptyData(): Boolean

}