package com.frameDesign

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.frameDesign.baselib.controller.BaseActivity
import com.frameDesign.baselib.model.repository.HttpUrlRepository
import com.frameDesign.commonlib.uitls.DialogUtils
import com.frameDesign.commonlib.uitls.DownloadHelper
import com.frameDesign.commonlib.uitls.permission.IPermissionListener
import com.frameDesign.commonlib.uitls.permission.PermissionFactory
import com.frameDesign.commonlib.uitls.permission.PermissonBean
import com.frameDesign.commonreslib.const.router.RouterLogin
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutView(): Any = R.layout.activity_main


    override fun initView(state: Bundle?) {

        mTitleDelegate.setTitleContent("首页")
    }

    fun readWritePermission(view: View) {
        PermissionFactory.execute(PermissonBean(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ), object : IPermissionListener {
            override fun onGranted(permission: PermissonBean) {
                //授权成功
            }

            override fun onDenied(permission: PermissonBean) {
                //授权失败
            }

        })
    }


    fun openFDDiaglog(view: View) {
        DialogUtils.createAlertDialog(this, "提示", "确认是对的吗？", "取消",
            { dialog, which -> },
            "确认",
            { dialog, which -> }).show()
    }

    fun openFDProgressDiaglog(view: View) {
        var dialog = DialogUtils.createProgressDialog(this).also {

            it.show()
        }
        Handler().postDelayed({ dialog.dismiss() }, 3000)

    }

    fun openPicker(view: View) {

    }

    fun loadData(view: View) {
//        HttpUrlRepository.globalObtainH5Urls.bindSub {
//            var str = StringBuilder()
//            it.actions.forEach {
//                str.append("title=${it.title};ref=${it.rel};href=${it.href} \n")
//            }
//            tv_data.text = str
//        }

        //TestHttpRepository.obtainUrl()
        HttpUrlRepository.requestLogin().bindSub {
            tv_data.text = "name=${it.user?.name};token=${it.token}"
        }
    }


    fun jumpToGood(view: View) {
        mARouter.build(RouterLogin.LOGIN_ACTIVITY).navigation()
    }

    fun downloadFile(view: View) {

        DownloadHelper.startDownloadDialog(
            this,
            BuildConfig.APPLICATION_ID,
            "http://oss.pgyer.com/d1855ce5c7d87d249f624ea2f1d990e2.apk?auth_key=1571729855-67a81535cce039ee6fe872bf546c4075-0-3dbab1b2d66a54ed22ea8a4ace6e3bf0&response-content-disposition=attachment%3B+filename%3Dhealthmanager_v1.0.34_flavor_uat_release.apk",
            false
        )
    }

}
