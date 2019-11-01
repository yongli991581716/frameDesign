package com.frameDesign.baselib.utils.expand.rx

import android.view.View
import android.view.ViewTreeObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * ..
 *
 * @author JustBlue
 * @time 2019/1/21
 */
class GlobalLayoutOb(val view: View) : Observable<String>() {

    override fun subscribeActual(observer: Observer<in String>) {
        Callback(view, observer)
    }

    inner class Callback(val view: View, val observer: Observer<in String>) : Disposable, ViewTreeObserver.OnGlobalLayoutListener {

        private var done = false

        init {
            observer.onSubscribe(this)
            view.viewTreeObserver
                    .addOnGlobalLayoutListener(this)
        }

        override fun onGlobalLayout() {
            if (!done) {
                observer.onNext("")
            }
        }

        override fun dispose() {
            done = true

            view.viewTreeObserver.removeGlobalOnLayoutListener(this)
        }

        override fun isDisposed(): Boolean = done

    }

}