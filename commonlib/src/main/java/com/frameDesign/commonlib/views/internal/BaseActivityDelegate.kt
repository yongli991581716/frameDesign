package com.frameDesign.commonlib.views.internal

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.frameDesign.commonlib.expand.timer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * @desc  activity代理类
 * ..
 * @author liyong
 * @date 2018/10/17
 */
abstract class BaseActivityDelegate(act: Activity, val mCycle: ILifeCycle) {

    private var isDestroy = false
    private var isCreated = false
    private var mCurrentCycle: Int = ILifeCycle.CREATE

    val mActivityRef = WeakReference(act)

    private val mDisposableList = CompositeDisposable()

    init {
        mCycle.getLifeCycle()
            .subscribe {
                mCurrentCycle = it
                when (it) {
                    ILifeCycle.VIEW_CREATED -> {
                        isCreated = false

                        lazyViewCreated()
                    }
                    ILifeCycle.RESUME -> {
                        onResume()

                        lazyViewCreated()
                    }
                    ILifeCycle.PAUSE -> onPause()
                    ILifeCycle.STOP -> onStop()
                    ILifeCycle.CREATE -> onCreate()
                    ILifeCycle.START -> onStart()
                    ILifeCycle.DESTROY -> {
                        mDisposableList.dispose()

                        isDestroy = true
                        onDestroy()
                    }
                }
            }

        if (isActiveCycle()) {
            24 timer {
                if (!isDestroy) {
                    lazyViewCreated()
                }
            }
        }
    }

    protected fun isActiveCycle() = mCycle.isActiveCycle()

    protected fun lazyViewCreated() {
        if (!isCreated) {
            isCreated = true

            onViewCreated()
        }
    }

    protected open fun onViewCreated() {

    }

    protected open fun onStop() {

    }

    protected open fun onDestroy() {

    }

    protected open fun onPause() {

    }

    protected open fun onResume() {

    }

    protected open fun onStart() {

    }

    protected open fun onCreate() {

    }

    /**
     * findViewById 适配
     * @param viewId Int
     * @return V
     */
    protected inline fun <reified V : View> find(@IdRes viewId: Int): V? {
        return mActivityRef.get()?.findViewById(viewId)
    }

    /**
     * 填充布局
     * @param lRes Int
     * @return V?
     */
    protected fun <V : View> inflate(@LayoutRes lRes: Int): V? {
        val activity = mActivityRef.get() ?: return null

        return LayoutInflater.from(activity).inflate(lRes, null) as? V
    }

    /**
     * findViewById
     * @receiver View?
     * @param viewId Int
     * @return V?
     */
    protected fun <V : View> View?.fv(@IdRes viewId: Int): V? {
        return this?.findViewById(viewId) as? V
    }

    /**
     * 绑定生命周期
     * @receiver Observable<T>
     * @return Observable<T>
     */
    protected fun <T> Observable<T>.bindDestroy(): Observable<T> {
        return this.doOnSubscribe {
            mDisposableList.add(it)
        }
    }

}