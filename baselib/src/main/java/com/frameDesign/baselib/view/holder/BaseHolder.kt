package com.frameDesign.baselib.view.holder

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes

/**
 * 持有view基础类
 *
 * @author liyong
 * @date  2018/5/29
 */
class BaseHolder(var contentView: View) {

    private var state = -1
    private val mViewCache =
        SparseArray<View?>()

    val context: Context
        get() = contentView.context

    val V: View
        get() = contentView

    /**
     * 获取[BaseHolder]中保存View的子View, 不可为空
     * 如存在[mViewCache]直接返回
     *
     * @param resId Int
     * @return T
     */
    fun <T : View> getView(@IdRes resId: Int): T {
        val view = mViewCache.get(resId)

        if (view != null) return view as T

        val fv: T = (contentView.findViewById(resId)
            ?: throw NullPointerException("not find View by id"))

        mViewCache.put(resId, fv)

        return fv
    }

    /**
     * 获取[BaseHolder]中保存View的子View, 可为空
     * 如存在[mViewCache]直接返回
     *
     * @param resId Int
     * @return T?
     */
    fun <T : View> find(@IdRes resId: Int): T? {
        val view = mViewCache.get(resId)

        if (view != null) return view as T

        val fv = contentView.findViewById(resId) as? T

        if (fv != null) {
            mViewCache.put(resId, fv)
        }

        return fv
    }

    internal fun getParent(): ViewGroup? =
        contentView.parent?.let {
            it as ViewGroup
        } ?: null

    internal fun bindState(state: Int) {
        this.state = state
    }

    fun getState(): Int = state

    internal fun clear() = mViewCache.clear()

}