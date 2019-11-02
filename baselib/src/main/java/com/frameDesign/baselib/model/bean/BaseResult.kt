package com.frameDesign.baselib.model.bean

import com.frameDesign.baselib.model.bean.internal.ErrorBean
import com.frameDesign.baselib.model.bean.miss.BaseMiss
import com.frameDesign.baselib.model.bean.miss.DefMiss
import com.frameDesign.baselib.model.bean.miss.EmptyDataMiss
import com.frameDesign.baselib.model.bean.miss.LoginMiss
import com.frameDesign.commonlib.expand.DEF_FUN
import com.frameDesign.commonreslib.const.ConstConfig
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

/**
 * @des 基础结果类
 * @author liyong
 * @date 2018/10/17
 */
abstract class BaseResult<T> : Observer<T> {

    companion object {

        // 跳转登录
        var gotoLogin: (() -> Unit)? = null

        private const val MAX_DEPTH = 25

        /**
         * 取出[Throwable.cause]的真实异常信息
         * 注: 最多取到25个, 如异常包裹层数大于25,
         * 异常信息可能失真
         * @param e Throwable
         * @return Throwable
         */
        fun getFinalCause(e: Throwable): Throwable {
            var e = e
            var i = 0
            while (e.cause != null) {
                if (i++ >= MAX_DEPTH) {
                    break
                }
                e = e.cause!!
            }
            return e
        }

        /**
         * 获取默认的异常信息封装类
         * @param e Throwable
         * @return ErrorBean
         */
        fun getDefaultErrorHint(e: Throwable): String {
            var m = "服务器异常, 请稍后再试!"

            when (e) {
                is DefMiss -> m = e.msg
                is BaseMiss -> m = e.msg
                is SocketTimeoutException,
                is TimeoutException -> "连接服务超时, 请稍后再试!"
                is IOException -> "服务异常断开, 请稍后再试!"
            }

            if (m.length > 40) {
                m = "服务器异常, 请稍后再试!"
            }

            return m
        }

    }

    override fun onSubscribe(d: Disposable) = DEF_FUN()

    final override fun onError(e: Throwable) {
        if (ConstConfig.FD_DEBUG) {
            e.printStackTrace()
        }

        doFinish(true)

        if (e is LoginMiss) {
            gotoLogin?.let {
                it.invoke()
                return
            }
        }

        val throwable = getFinalCause(e)

        val errorHint = resolveErrorHint(throwable)
            .takeIf { it.isNotEmpty() } ?: getDefaultErrorHint(throwable)

        doFailure(ErrorBean(throwable, errorHint))
    }

    /**
     * 由子类确定[e]异常的提示信息
     * @param e Throwable
     * @return String
     */
    protected open fun resolveErrorHint(e: Throwable): String = ""

    final override fun onNext(t: T) {
        if (t != null) {
            doSuccess(t)
        } else {
            onError(EmptyDataMiss())
        }
    }

    final override fun onComplete() {
        doFinish(false)

        doComplete()
    }

    open fun doComplete() {}

    /**
     * 自定义终结事件, 可能来自于
     */
    abstract fun doFinish(errorFinish: Boolean)

    abstract fun doSuccess(data: T)

    abstract fun doFailure(eBean: ErrorBean)

}

