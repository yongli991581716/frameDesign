package com.frameDesign.baselib.utils

import com.frameDesign.baselib.model.bean.actions.*
import java.util.concurrent.atomic.AtomicReference

/**
 * Created by liyong on 2017/12/21.
 */
object ActionsHelp {

//    val ATION_EMPTY_ITEM by lazy { ActionsBeanItem() }
//    val ATION_H5_EMPTY_ITEM by lazy { ActionsH5BeanItem() }

    val mActionRef = AtomicReference<ActionsBean>()
    val mActionH5Ref = AtomicReference<ActionsH5Bean>()

    /**
     * 保存H5接口
     * @param bean ActionsH5Bean
     */
    fun saveH5Actions(bean: ActionsH5Bean) = mActionH5Ref.set(bean)

    /**
     * 通过别名获取H5路径
     * @param alias String
     * @return ActionsH5BeanItem?
     */
    fun getH5ApiByAlias(alias: String): ActionsH5BeanItem? {
        while (true) {
            val current = mActionH5Ref.get()

            if (current != null) {
                val apiItem = current.actions
                    ?.find {
                        it.rel == alias
                    }

                if (mActionH5Ref.get() == current) {
                    return apiItem
                }
            }

            return null
        }
    }

    /**
     * 获取分享路径
     * @param alias String
     * @return ActionsH5ShareBean?
     */
    fun getShareApiByAlias(alias: String): ActionsH5ShareBean? {
        while (true) {
            val current = mActionH5Ref.get()

            if (current != null) {
                val apiItem = current.shareDomains
                    ?.find {
                        it.rel == alias
                    }

                if (mActionH5Ref.get() == current) {
                    return apiItem
                }
            }

            return null
        }
    }

    /**
     * 判断是否加载H5路径
     * @return Boolean
     */
    fun hasH5Actions(): Boolean {
        while (true) {
            val current = mActionH5Ref.get()

            val hasData = current?.actions?.isNotEmpty() ?: false

            if (current == mActionH5Ref.get()) {
                return hasData
            }
        }
    }

    /**
     * 保存接口数据
     * @param bean ActionsBean
     */
    fun saveData(bean: ActionsBean) = mActionRef.set(bean)

    /**
     * 通过接口别名获取接口数据
     * @param alias String
     * @return ActionsBeanItem?
     */
    @Suppress("MISSING_DEPENDENCY_CLASS")
    fun getApiByAlias(alias: String): ActionsBeanItem? {
        while (true) {
            val current = mActionRef.get()

            if (current != null) {
                val apiItem = current.actions
                    ?.find {
                        it.rel == alias
                    }

                if (mActionRef.get() == current) {
                    return apiItem
                }
            }

            return null
        }
    }

    /**
     * 是否已存在Api数据
     * @return Boolean
     */
    fun hasApiActions(): Boolean {
        while (true) {
            val current = mActionRef.get()

            val hasData = current?.actions?.isNotEmpty() ?: false

            if (current == mActionRef.get()) {
                return hasData
            }
        }
    }

}