package com.frameDesign.baselib.model.repository

import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

/**
 * 自定义调度器
 */
object FDSchedulers {

    private val mThreadCount = AtomicLong()

    private val mThreadPools = Executors
            .newScheduledThreadPool(5) {
                // 创建自定义线程, 添加项目指定名称
                Thread(it).apply {
                    this.isDaemon = true
                    this.priority = Thread.MAX_PRIORITY
                    this.name = "fd-worker::${mThreadCount.getAndIncrement()}"

                   // Schedulers.newThread()
                }
            }

    val worker by lazy {
        Schedulers.from(mThreadPools)
    }
}