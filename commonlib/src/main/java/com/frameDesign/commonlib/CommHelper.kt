package com.frameDesign.commonlib

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 维护公共库需要的上下文对象等
 *
 * @author liyong
 * @date  2018/2/9.
 */
@SuppressLint("StaticFieldLeak")
object CommHelper {
    lateinit var mCtx: Context

    fun init(ctx: Application) {
        mCtx = ctx
    }
}