package com.frameDesign.baselib.model.bean.internal

import com.frameDesign.baselib.const.HttpConfig
import com.frameDesign.commonlib.expand.nullOrEmpty


/**
 * @desc  数据基类
 * @author liyong
 * @date 2018/10/17
 */
class ZQData<T> : DataSource<T> {

    val code: Int = 0
    val message: String = ""

    val data: T? = null

    override fun data(): T = data!!

    override fun code(): Int = code

    override fun message(): String = message

    override fun success(): Boolean = code == HttpConfig.SUCCESS_CODE

    override fun notEmptyData(): Boolean {
        val d = data ?: return false

        if (d is DataEmpty) {
            return d.notEmptyData()
        }

        return true
    }

}

/**
 * 集合数据结构
 *
 * @author JustBlue
 * @time 2018/10/12
 */
class ZQList<T> : DataSource<List<T>> {

    val code: Int = 0
    val message: String = ""

    val data: List<T> = listOf()

    override fun data(): List<T> = data

    override fun code(): Int = code

    override fun message(): String = message

    override fun success(): Boolean = code == HttpConfig.SUCCESS_CODE

    override fun notEmptyData(): Boolean = data.isNotEmpty()

}

/**
 * 分页数据二级实体封装类
 *
 * @author JustBlue
 * @time 2018/10/23
 */
class Page<T> : DataSource<List<T>> {

    companion object {

        fun <T> empty(): Page<T> {
            return Page()
        }

        fun <T> create(size: Int, total: Int): Page<T> {
            return Page<T>().apply {
                pageSize = size
                totalCount = total
            }
        }

        fun <T> create(datas: MutableList<T>): Page<T> {
            return Page<T>().apply {
                this.list.addAll(datas)
            }
        }
    }

    var pageSize = 0
    var totalCount = 0

    var list = mutableListOf<T>()

    override fun data(): List<T> = list

    override fun code(): Int = HttpConfig.SUCCESS_CODE

    override fun message(): String = "加载成功"

    override fun success(): Boolean = true

    override fun notEmptyData(): Boolean = !list.nullOrEmpty()

}

/**
 * 集合数据结构
 *
 * @author JustBlue
 * @time 2018/10/12
 */
class ZQPage<T> : DataSource<Page<T>> {

    val code: Int = 0
    val message: String = ""

    var data: Page<T> = Page.empty()

    override fun data(): Page<T> = data

    override fun code(): Int = code

    override fun message(): String = message

    override fun success(): Boolean = code == HttpConfig.SUCCESS_CODE

    override fun notEmptyData(): Boolean = data?.notEmptyData() ?: false

}

