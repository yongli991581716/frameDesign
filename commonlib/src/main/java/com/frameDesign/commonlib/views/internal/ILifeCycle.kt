package com.frameDesign.commonlib.views.internal

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @desc  定义生命周期类
 * ..
 * @author liyong
 * @date 2018/10/17
 */
interface ILifeCycle {

    companion object {

        /**
         * 以下对应[android.app.Activity]的生命周期
         */
        const val CREATE = 0
        const val START = 1
        const val RESUME = 2
        const val PAUSE = 3
        const val STOP = 4
        const val DESTROY = 5

        /**
         * 对Fragment的补充
         */
        const val ATTACH = 6
        const val DETACH = 7
        // 兼容Activity和Fragment, 补充一个自定义周期
        // 表示在这个周期中, View可用, 且只调用一次
        const val VIEW_CREATED = 8
    }

    /**
     * 当前生命周期
     */
    var mCurrentCycle: Int

    /**
     * 生命周期分发操作类
     */
    val mLifecycle: PublishSubject<Int>

    /**
     * 是否为活跃的生命周期(存在View, 且不等于[DESTROY]\ [DETACH])
     * @return Boolean
     */
    fun isActiveCycle(): Boolean

    /**
     * 获取生命周期分发事件流
     * @return Observable<Int>
     */
    fun getLifeCycle(): Observable<Int> = mLifecycle

    /**
     * 绑定生命周期onDestroy, 完成请求自销毁
     * 注: 只是不接收响应, 已发出请求无法取消
     *
     * @receiver Observable<T>
     * @return Observable<T>
     */
    fun <T> Observable<T>.bindDestroy(): Observable<T> {
        return this.takeUntil<Int> {
            mLifecycle
                    .filter { it == DESTROY }
                    .take(1)
                    .subscribe(it)
        }
    }

    /**
     * 只执行[android.app.Activity.onDestroy]声明周期
     * @param block () -> Unit
     */
    fun doOnDestroy(block: () -> Unit) {
        getLifeCycle().subscribe {
            if (it == ILifeCycle.DESTROY) {
                kotlin.run(block)
            }
        }
    }

}