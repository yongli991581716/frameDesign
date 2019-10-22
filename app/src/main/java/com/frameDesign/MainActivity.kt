package com.frameDesign

import android.app.DownloadManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.frameDesign.baselib.BaseApplication.Companion.mCtx
import com.frameDesign.baseres.const.router.RouterLogin
import com.frameDesign.commonlib.uitls.DownloadHelper
import com.frameDesign.commonlib.uitls.SystemUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun jumpToGood(view: View) {
        ARouter.getInstance().build(RouterLogin.LOGIN_ACTIVITY).navigation()
    }

    fun downloadFile(view: View) {

        DownloadHelper.startDownloadDialog(this,BuildConfig.APPLICATION_ID,
            "http://oss.pgyer.com/d1855ce5c7d87d249f624ea2f1d990e2.apk?auth_key=1571729855-67a81535cce039ee6fe872bf546c4075-0-3dbab1b2d66a54ed22ea8a4ace6e3bf0&response-content-disposition=attachment%3B+filename%3Dhealthmanager_v1.0.34_flavor_uat_release.apk",false)
    }

}
