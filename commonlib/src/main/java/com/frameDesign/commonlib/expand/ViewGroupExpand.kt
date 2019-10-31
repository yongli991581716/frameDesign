package com.frameDesign.commonlib.expand

import android.view.View
import android.view.ViewGroup


/**
 * 返回一个ViewGroup的子View序列
 *
 * 注: 协程实现, 可中断
 */
fun ViewGroup.childSequence(): Sequence<View> {
    return sequence {
        val count = childCount

        for (i in 0 until count) {
            yield(getChildAt(i))
        }
    }
}

/**
 * 添加一个`操作符`, 判断View是否存在ViewGroup中
 *
 * 调用方式: child in parent
 */
inline operator fun ViewGroup.contains(child: View): Boolean {

    return child in childSequence()

    /* 第二种实现 */
//    return childSequence().any {
//        child == it
//    }
}

/**
 * 代理[ViewGroup.indexOfChild]
 *
 * 调用方式: parent[child]
 */
inline operator fun ViewGroup.get(child: View): Int {
    return indexOfChild(child)
}

/**
 * 代理[ViewGroup.indexOfChild]
 *
 * 调用方式: parent[0] - 取第一个childView
 * 注: [get]传入的是View, 当前传入的是Int
 */
inline operator fun ViewGroup.get(index: Int): View {
    return getChildAt(index)
}

/**
 * 替换ViewGroup中的子View
 *
 * 注: 此方法[targetView]必需是[this] (ViewGroup)的直接子类
 * 否则, 替换会不成功, 推荐使用[replace]...
 *
 * @param updateView 替换的View
 * @param targetView 被替换View, 如不存在, 替换不成功
 */
fun ViewGroup.swapView(updateView: View, targetView: View): Boolean {
    var index: Int = 0
    val existChild = {
        var exist = false
        for ((i, v) in childSequence().withIndex()) {
            if (v == targetView) {
                exist = true
                index = i
                break
            }
        }
        exist
    }

    if (updateView in this || !existChild()) {
        // 已存在需更新的View或不存在被替换的View, 直接替换不成功
        return false
    }

    updateView.parent?.let {
        it as ViewGroup
        /* 如更新View已有父布局, 移除 */
        it.removeView(updateView)
    }

    /* 获取View的LayoutParams, 如为null返回false */
    val params = targetView.layoutParams ?: return false

    /* 移除被替换的View */
    removeView(targetView)
    /* 添加替换的View到被替换的位置 */
    addView(updateView, index, params)

    return true
}

/**
 * 将[this] (调用的View) 替换成[replace], 如果[this].parent为null, 不替换
 *
 * 内部通过[swapView]实现View的替换
 */
infix fun View.replace(replace: View): Boolean {
    var parent = (this.parent ?: return false) as ViewGroup

    return parent.swapView(replace, this)
}