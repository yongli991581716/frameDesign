// 不提示警告
@file:Suppress("NOTHING_TO_INLINE")

package com.zqkh.commlibrary.utilslibrary.expand

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.frameDesign.commonlib.expand.DEF_FUN
import com.frameDesign.commonlib.expand.currentTime
import com.frameDesign.commonlib.expand.ifElse
import com.frameDesign.commonlib.expand.offsetTime

/**
 * @Create JustBlue
 * @Time 07/10/2018
 * @Desc View 相关拓展
 */

/**
 * [get]获取View可见状态
 * [set]设置View可见状态
 */
inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        if (isVisible == value) {
            /* 如相等, 无操作 */
            return
        }

        visibility = if (value)
            View.VISIBLE else View.GONE
    }

/**
 * [View.setVisibility]的代理
 */
inline var View.inVisible: Boolean
    get() = visibility == View.INVISIBLE
    set(value) {
        visibility = value.ifElse(
            View.INVISIBLE, View.VISIBLE
        )
    }

/**
 * 获取View可见的区域, 对应[View.getGlobalVisibleRect]
 */
val View.globalVisibleRect: Rect
    get() = Rect().apply {
        getGlobalVisibleRect(this)
    }

/**
 * 获取View的显示区域(如未遮挡, 一般返回(0, 0, width, height)
 * , 如遮挡会减去被遮挡的区域), 对应[View.getLocalVisibleRect]
 */
val View.localVisibleRect: Rect
    get() = Rect().apply {
        getLocalVisibleRect(this)
    }

// 上一次点击事件发生时间, 用于屏蔽快速点击相应两次
var clickSaveTime = 0L

/**
 * 绑定点击事件; 添加快速点击响应两次适配
 *
 * 注: 时间为全局时间, 这会使整个APP都不会同时响应点击事件;
 */
inline infix fun View.doOnClick(crossinline action: (view: View) -> Unit) {
    this.setOnClickListener {
        if (offsetTime(clickSaveTime) > 150) {
            clickSaveTime = currentTime()
            action(it)
        }
    }
}

/**
 * 绑定点击事件; 添加快速点击响应两次适配(时间间隔为150ms)
 *
 * 注: 时间为全局时间, 这会使整个APP都不会同时响应点击事件
 * (优点是: 避免为每一个点击事件创建一个记录点击时间的实例);
 */
inline infix fun View.doOnClick(listener: View.OnClickListener) {
    this.setOnClickListener {
        if (offsetTime(clickSaveTime) > 150) {
            clickSaveTime = currentTime()
            listener.onClick(it)
        }
    }
}

/**
 * 回调下一次布局改变, 回调完成后自动删除回调
 */
inline fun View.doOnNextLayout(crossinline action: (view: View) -> Unit) {
    this.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {

            view.removeOnLayoutChangeListener(this)
            action(view)
        }
    })
}

inline fun View.getLocation(rect: Rect = Rect()): Rect = rect.also {
    it.set(left, top, right, bottom)
}

/**
 * 回调一次布局改变, 回调完成后自动删除回调, 如界面已经加载过一次, 直接执行回调
 */
inline fun View.doOnLayout(crossinline action: (view: View) -> Unit) {
    if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
        action(this)
    } else {
        doOnNextLayout(action)
    }
}

/**
 * 更新View的内边距, 内部通过[View.setPaddingRelative]
 */
@RequiresApi(17)
inline fun View.updatePaddingRelative(
    @Px start: Int = paddingStart,
    @Px top: Int = paddingTop,
    @Px end: Int = paddingEnd,
    @Px bottom: Int = paddingBottom
) {

    setPaddingRelative(start, top, end, bottom)
}

/**
 * 更新View padding
 */
inline fun View.updatePadding(
    @Px l: Int = paddingLeft,
    @Px t: Int = paddingTop,
    @Px r: Int = paddingRight,
    @Px b: Int = paddingBottom
) {

    setPadding(l, t, r, b)
}

/**
 * 更新View的位置和大小
 * @receiver View
 * @param l Int
 * @param t Int
 * @param r Int
 * @param b Int
 */
inline fun View.updateLayoutSize(l: Int = left, t: Int = top, r: Int = right, b: Int = bottom) {
    layout(l, t, r, b)
}

/**
 * 更新[View.getLayoutParams]宽高值
 *
 * 如[View.getLayoutParams]为null, 无操作
 * @param width
 */
inline fun View.updateLayoutSize(width: Int = -1, height: Int = -1) {
    var change = false

    val params = layoutParams ?: return

    if (width > 0 && params.width != width) {
        change = true
        params.width = width
    }

    if (height > 0 && params.height != height) {
        change = true
        params.height = height
    }

    if (change) requestLayout()
}

/**
 * 获取指定类型的LayoutParams, 并回掉更新
 */
@JvmName("updateLayoutParamsTyped")
inline fun <reified T : ViewGroup.LayoutParams> View.updateLayoutParams(block: T.() -> Unit) {
    val params = layoutParams as T
    block(params)
    layoutParams = params
}

/**
 * 更像view的外边距
 * @receiver View
 * @param l Int
 * @param t Int
 * @param r Int
 * @param b Int
 */
@JvmName("updateMargin")
inline fun View.updateMargin(
    l: Int = -1,
    t: Int = -1,
    r: Int = -1,
    b: Int = -1
) {
    val params = this.layoutParams

    if (params is ViewGroup.MarginLayoutParams) {
        var change = false

        if (t > 0 && t != params.topMargin) {
            params.topMargin = t
            change = true
        }

        if (l > 0 && l != params.leftMargin) {
            params.leftMargin = l
            change = true
        }

        if (r > 0 && r != params.rightMargin) {
            params.rightMargin = r
            change = true
        }

        if (b > 0 && b != params.bottomMargin) {
            params.bottomMargin = b
            change = true
        }

        if (change) {
            this.requestLayout()
        }
    }
}

/**
 * 延迟运行;
 * 注: [action]回调, 有内存泄漏的风险;
 */
inline fun View.postDelayed(delayInMillis: Long, crossinline action: () -> Unit): Runnable {
    val runnable = Runnable { action() }
    postDelayed(runnable, delayInMillis)
    return runnable
}

/**
 * 回调[ViewPager]::onPageScrollStateChanged
 */
inline fun ViewPager.doOnPageChange(crossinline runStateChange: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) = runStateChange(state)

        override fun onPageSelected(position: Int) = DEF_FUN()
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = DEF_FUN()
    })
}

/**
 * 回调[ViewPager]::onPageSelected
 */
inline fun ViewPager.doOnPageSelect(crossinline runPageSelect: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) = runPageSelect(position)

        override fun onPageScrollStateChanged(state: Int) = DEF_FUN()
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = DEF_FUN()
    })
}

/**
 * 回调[ViewPager]::onPageScrolled
 */
inline fun ViewPager.doOnPageScroll(crossinline runPageScroll: (Int, Float, Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) = DEF_FUN()
        override fun onPageScrollStateChanged(state: Int) = DEF_FUN()

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) =
            runPageScroll(position, positionOffset, positionOffsetPixels)
    })
}

/**
 * 快速创建[FragmentPagerAdapter]
 */
inline fun <T : Fragment> AppCompatActivity.createPageAdapter(vararg fragments: T): FragmentPagerAdapter {
    return object : FragmentPagerAdapter(supportFragmentManager) {

        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment = fragments[position]
    }
}




