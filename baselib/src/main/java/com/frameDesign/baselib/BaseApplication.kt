package com.frameDesign.baselib

import android.app.Activity
import android.app.Application
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.frameDesign.baseres.const.ConstConfig
import com.frameDesign.commonlib.CommHelper
import com.frameDesign.commonlib.uitls.DownloadHelper

/**
 * Created by liyong on 2019-10-21.
 */
abstract class BaseApplication : MultiDexApplication() {

    companion object {
        private var mActivityList = ArrayList<Activity>()
        lateinit var mCtx: Application
    }

    override fun onCreate() {
        super.onCreate()

        initDatas()
        //初始化路由
        initRouter()
        //下载管理器初始化
        DownloadHelper.initDownload(mCtx)
        //公共库组件公用上下文初始化
        CommHelper.init(mCtx)
    }

    /**
     * 初始化路由
     */

    private fun initRouter() {
        if (ConstConfig.FD_DEBUG) { // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()    // 打印日志
            ARouter.openDebug()  // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }

        ARouter.init(mCtx)
    }

    /**
     * 添加activity
     */
    fun addActivity(activity: Activity?) {
        activity?.let {
            mActivityList.add(activity)
        }
    }

    /**
     * 获取activity集合
     */
    fun getActivityList(): ArrayList<Activity> {
        return mActivityList
    }

    abstract fun initDatas()
}