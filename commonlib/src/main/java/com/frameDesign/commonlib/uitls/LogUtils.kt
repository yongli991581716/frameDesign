package com.frameDesign.commonlib.uitls

import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


/**
 * Created by liyong on 2017/12/20.
 * 日志工具
 */
object LogUtils {

    init {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
            .methodCount(0)         // (Optional) How many method line to show. Default 2
            .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
            .tag("debugLog")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    var blLog = true//是否打印日志

    private val TAG = "debugLog"

    fun v(log: String?) {
        v(TAG, log)
    }

    fun d(log: String?) {
        d(TAG, log)
    }

    fun w(log: String?) {
        w(TAG, log)
    }

    fun e(log: String?) {
        e(TAG, log)
    }

    fun v(tag: String, log: String?) {
        if (blLog && log != null && log.length < 10 * 10000) {//起过10万就不打印了
            val segmentSize = 3 * 1024
            if (log.length <= segmentSize) {// 长度小于等于限制直接打印
                Log.d(tag, log)
            } else {
                var splitMsg = log!!
                while (splitMsg.length > segmentSize) {// 循环分段打印日志
                    val logContent = splitMsg.substring(0, segmentSize)
                    splitMsg = splitMsg.replace(logContent, "")
                    Log.d(tag, logContent)
                }
                Log.d(tag, splitMsg)// 打印剩余日志
            }
        }
    }

    fun d(tag: String, log: String?) {
        if (blLog && log != null && log.length < 10 * 10000) {//起过10万就不打印了
            val segmentSize = 3 * 1024
            if (log.length <= segmentSize) {// 长度小于等于限制直接打印
                Log.d(tag, log)
            } else {
                var splitMsg = log!!
                while (splitMsg.length > segmentSize) {// 循环分段打印日志
                    val logContent = splitMsg.substring(0, segmentSize)
                    splitMsg = splitMsg.replace(logContent, "")
                    Log.d(tag, logContent)
                }
                Log.d(tag, splitMsg)// 打印剩余日志
            }
        }
    }

    fun w(tag: String, log: String?) {
        if (blLog && log != null && log.length < 10 * 10000) {//起过10万就不打印了
            val segmentSize = 3 * 1024
            if (log.length <= segmentSize) {// 长度小于等于限制直接打印
                Log.d(tag, log)
            } else {
                var splitMsg = log!!
                while (splitMsg.length > segmentSize) {// 循环分段打印日志
                    val logContent = splitMsg.substring(0, segmentSize)
                    splitMsg = splitMsg.replace(logContent, "")
                    Log.w(tag, logContent)
                }
                Log.w(tag, splitMsg)// 打印剩余日志
            }
        }
    }

    fun e(tag: String, log: String?) {
        if (blLog && log != null) {
            val segmentSize = 3 * 1024
            if (log.length <= segmentSize) {// 长度小于等于限制直接打印
                Log.e(tag, log)
            } else {
                var splitMsg = log!!
                while (splitMsg.length > segmentSize) {// 循环分段打印日志
                    val logContent = splitMsg.substring(0, segmentSize)
                    splitMsg = splitMsg.replace(logContent, "")
                    Log.e(tag, logContent)
                }
                Log.e(tag, splitMsg)// 打印剩余日志
            }
        }
    }
}