// 不提示警告
@file:Suppress("NOTHING_TO_INLINE")

package com.frameDesign.baselib.utils.expand

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.frameDesign.baselib.controller.BaseCommActivity
import com.frameDesign.baselib.utils.expand.rx.GlobalLayoutOb
import com.frameDesign.commonlib.expand.currentTime
import com.frameDesign.commonlib.expand.runUIThread
import com.frameDesign.commonlib.views.internal.ILifeCycle
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 发出一个layout事件流
 * @receiver View
 * @param life ILifeCycle
 * @return Observable<String>
 */
fun View.layoutEvent(): Observable<String> =
    GlobalLayoutOb(this)

/**
 * 拦截[BaseCommActivity]声明周期并为[EditText]
 * 绑定[EditText.addTextChangedListener]
 * @receiver BaseCommActivity
 * @param editText EditText
 * @param block (String) -> Unit
 */
fun EditText.doOnTextChange(life: ILifeCycle, block: (text: String) -> Unit) =
    life.doOnTextChange(this, block)

/**
 * 拦截[BaseCommActivity]声明周期并为[EditText]
 * 绑定[EditText.addTextChangedListener]
 * @receiver BaseCommActivity
 * @param editText EditText
 * @param block (String) -> Unit
 */
fun ILifeCycle.doOnTextChange(editText: EditText, block: (text: String) -> Unit) {
    val textWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            block(editText.text.toString())
        }
    }

    editText.addTextChangedListener(textWatcher)

    doOnDestroy {
        editText.removeTextChangedListener(textWatcher)
    }
}

/**
 * 倒计时
 * @receiver BaseCommActivity
 * @param time Long
 * @param delay Long
 * @param onFinish () -> Unit
 * @param runTimer (time: Long) -> Unit
 */
fun BaseCommActivity.countDown(
    time: Long,
    delay: Long = 1000,
    onFinish: () -> Unit,
    runTimer: (time: Long) -> Unit
) {
    runTimer(time)

    var start = currentTime()
    Observable.interval(delay, TimeUnit.MILLISECONDS)
        .map {
            val current = currentTime()
            val offset = time - current + start

            return@map if (offset > 0) {
                offset
            } else 0L
        }
        .takeUntil { it == 0L }
        .bindDestroy()
        .runUIThread()
        .subscribe({
            runTimer(it)
        }, {}) {
            onFinish()
        }
}

/**
 * 一秒钟获取一次时间
 */
fun BaseCommActivity.startCountiniuTimer(runTimer: (time: Long) -> Unit) {
    runTimer(1000)
    Observable.interval(1000, TimeUnit.MILLISECONDS)
        .bindDestroy()
        .runUIThread()
        .subscribe({
            runTimer(it)
        }, {})
}

/**
 * 检查是否存在毕传参数
 * @receiver BaseCommActivity
 * @param keyArr Array<out String>
 */
fun BaseCommActivity.validValueTransmit(vararg keyArr: String) {
    for (key in keyArr) {
        if (!intent.hasExtra(key)) {
            com.frameDesign.commonlib.expand.fdToast("参数传递异常")
            this.finish()
        }
    }
}

/**
 * 延时执行
 * @receiver ILifeCycle
 * @param time Long
 * @param runTimer (time: Long) -> Unit
 */
inline fun ILifeCycle.postDelay(
    time: Long,
    crossinline runTimer: (time: Long) -> Unit
): Disposable {
    return timerEvent(time)
        .subscribe {
            runTimer(it)
        }
}

/**
 * 延时事件流
 * @receiver ILifeCycle
 * @param time Long
 * @return Observable<Long>
 */
inline fun ILifeCycle.timerEvent(time: Long): Observable<Long> {
    return Observable.timer(time, TimeUnit.MILLISECONDS)
        .bindDestroy()
        .runUIThread()
}