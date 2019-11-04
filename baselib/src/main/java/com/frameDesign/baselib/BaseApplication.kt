package com.frameDesign.baselib

import android.app.Activity
import android.app.Application
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.drawee.backends.pipeline.Fresco
import com.frameDesign.baselib.utils.FrescoHelp
import com.frameDesign.commonlib.CommHelper
import com.frameDesign.commonlib.uitls.DownloadHelper
import com.frameDesign.commonlib.uitls.permission.AndPermissionProduct
import com.frameDesign.commonlib.uitls.permission.PermissionFactory
import com.frameDesign.commonreslib.const.ConstConfig


/**
 * Created by liyong on 2019-10-21.
 */
open class BaseApplication : MultiDexApplication() {

    companion object {
        private var mActivityList = ArrayList<Activity>()
        lateinit var mCtx: Application
    }

    override fun onCreate() {
        super.onCreate()

        //初始化context
        initContext()
        //初始化配置
        initConfig()
        //初始化数据
        initDatas()

    }

    open fun initContext() {

    }

    open fun initConfig() {
        //初始化路由
        initRouter()
        //下载管理器初始化
        DownloadHelper.initDownload(mCtx)
        //公共库组件公用上下文初始化
        CommHelper.init(mCtx)
        //权限工厂初始化,此处使用AndPermission库
        PermissionFactory.init(AndPermissionProduct())
        //图片库初始化
        Fresco.initialize(mCtx, FrescoHelp.getConfig(mCtx))
    }

    open fun initDatas() {

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


}