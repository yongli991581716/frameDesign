package com.frameDesign.commonlib.expand

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import androidx.annotation.*
import androidx.fragment.app.Fragment
import com.frameDesign.commonlib.CommHelper
import com.frameDesign.commonlib.uitls.DensityUtils
import com.frameDesign.commonlib.uitls.LogUtils
import com.frameDesign.commonlib.uitls.ToastUtils
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import java.lang.reflect.Type
import java.text.DecimalFormat
import java.util.concurrent.atomic.AtomicBoolean


/**
 *全局拓展
 * @author liyong
 * @date 2018-11-07.
 */
val DEF_FUN = {
    // 无参默认函数
    LogUtils.d("null impl function")
}

val DEF_FUN_1 = { _: Any ->
    // 单参默认函数
    LogUtils.d("null impl function")
}

val DEF_FUN_2 = { _: Any, _: Any ->
    // 双参默认函数
    LogUtils.d("null impl function")
}

val DEF_FUN_3 = { _: Any, _: Any, _: Any ->
    // 三参默认函数
    LogUtils.d("null impl function")
}

/**
 * 延时操作
 */
inline infix fun <T : Number> T.timer(noinline runTimer: () -> Unit) {
    Handler().postDelayed(runTimer, this.toLong())
}

/**
 * 限制Int上下限
 */
inline fun Int.limit(lower: Int = Int.MIN_VALUE, upper: Int = Int.MAX_VALUE): Int =
    when {
        upper < lower -> throw IllegalArgumentException("upper must more than lower")
        this > upper -> upper
        this < lower -> lower
        else -> this
    }

/**
 * 判断当前线程是否为UI线程
 */
inline fun checkUIThread(): Boolean =
    Looper.getMainLooper() == Looper.myLooper()

/* 当前时间 */
inline fun currentTime(): Long = System.currentTimeMillis()

/* 指定时间与现在的相对偏移 */
inline fun offsetTime(time: Long): Long = currentTime() - time

val SCREEN_W = DensityUtils.getScreenWidth()
val SCREEN_H = DensityUtils.getScreenHeight()

inline fun <T : Number> T.dp2px(): Int = DensityUtils.dip2px(this.toFloat())
inline fun <T : Number> T.px2dp(): Int = DensityUtils.px2dip(this.toFloat())
inline fun <T : Number> T.sp2px(): Int = DensityUtils.sp2px(this.toFloat())
inline fun <T : Number> T.px2sp(): Int = DensityUtils.px2sp(this.toFloat())
inline fun <T : Number> T.format3Comma(): String {
    val df = DecimalFormat("#,###")
    return df.format(this)
}

//inline fun <T : BaseBean> T.toJsonString(): String? = JsonUtils.toJsonString(this)

/* 通过id获取String, 简化代码(下同) */
inline fun fdString(@StringRes idRes: Int): String =
    CommHelper.mCtx.resources.getString(idRes)

inline fun fdColor(@ColorRes idRes: Int): Int =
    CommHelper.mCtx.resources.getColor(idRes)

inline fun fdDrawable(@DrawableRes idRes: Int): Drawable =
    CommHelper.mCtx.resources.getDrawable(idRes)

inline fun fdDimen(@DimenRes idRes: Int): Int =
    CommHelper.mCtx.resources.getDimensionPixelSize(idRes)

inline fun fdArray(@ArrayRes idRes: Int): Array<String> =
    CommHelper.mCtx.resources.getStringArray(idRes)

/**
 * 简化[AtomicBoolean.compareAndSet]操作
 */
inline fun AtomicBoolean.T2F(): Boolean = this.compareAndSet(true, false)

inline fun AtomicBoolean.F2T(): Boolean = this.compareAndSet(false, true)


/**
 * 简化Toast提示, 默认不提示 null ro ""
 */
inline infix fun <ACT : Activity> ACT.fdToast(msg: String?) {
    ToastUtils.show(CommHelper.mCtx, msg)
}

/**
 * 简化Toast提示, 默认不提示 null ro ""
 */
inline infix fun <FGT : Fragment> FGT.fdToast(msg: String?) {
    ToastUtils.show(CommHelper.mCtx, msg)
}

/**
 * 显示[Toast]
 */
inline fun fdToast(msg: String?) {
    msg ?: return

    if (msg.isNotEmpty()) {
        ToastUtils.show(CommHelper.mCtx, msg)
    }
}

/**
 * 显示[Toast]
 */
inline fun fdToast(resId: Int?) {
    resId ?: return

    ToastUtils.show(CommHelper.mCtx as Activity, resId)
}

/**
 * 仿三项运算 如为true返回[trueValue], false返回[falseValue]
 */
inline fun <T> Boolean.ifElse(trueValue: T, falseValue: T): T =
    if (this) trueValue else falseValue

/**
 * 迭代集合拼接成一个字符串, 可添加指定中介符
 *
 * 如:  (集合a = ["a", "b", "c"]) a.join(",")
 * 输出: a,b,c
 */
inline fun <T> Collection<T>?.join(midVal: String = "", itemVal: T.() -> String): String {
    if (this == null) return "" // 空集合

    val ite = iterator()
    val sbs = StringBuffer()

    if (ite.hasNext()) {
        sbs.append(ite.next().itemVal())
    }

    while (ite.hasNext()) {
        val iv = ite.next().itemVal()
        // 默认排除 ""(空字符串)
        if (iv.isNotEmpty()) {
            sbs.append(midVal)
            sbs.append(iv)
        }
    }

    return sbs.toString()
}

/**
 * 安全的获取
 * @receiver List<T>?
 * @param i Int
 * @return T?
 */
inline fun <T> List<T>?.safeGet(i: Int): T? {
    if (this == null || isEmpty()) {
        return null
    }
    if (i in 0 until size) {
        return get(i)
    }
    return null
}

/**
 * 判断集合是null 或 空
 * @receiver List<T>?
 * @return Boolean
 */
inline fun <T> List<T>?.nullOrEmpty(): Boolean =
    this == null || this.isEmpty()

inline fun runUIThread(crossinline block: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        kotlin.run(block)
    }
}

/**
 * 获取[T]的实际类型
 * @receiver T
 * @return Type
 */
inline fun <reified T> tokenType(): Type =
    object : TypeToken<T>() {}.type


/**
 * 打印RxJava返回值
 * @receiver Observable<*>
 * @param tag String
 * @return Disposable
 */
inline fun Observable<*>.showLog(tag: String): Disposable {
    return this.subscribe({
        Logger.e("$tag onNext ->$it")
    }, {
        Logger.e("$tag onError ->${it::class.java.simpleName} ${it.message}")
    }) {
        Logger.e("$tag onCompleted")
    }
}


/**
 * 调度到主线程执行
 * @receiver Observable<T>
 * @return Observable<T>
 */
inline fun <T> Observable<T>.runUIThread(): Observable<T> =
    this.observeOn(AndroidSchedulers.mainThread())

/**
 *
 * @receiver Observable<T>
 * @param otehr Observable<R>
 * @return Observable<T>
 */
inline fun <T, R> Observable<T>.zipAndIgnore(otehr: Observable<R>): Observable<T> {
    return this.zipWith(otehr, BiFunction { t1, _ -> t1 })
}