package com.frameDesign.logincomponent

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.frameDesign.baselib.controller.BaseActivity
import com.frameDesign.commonreslib.const.router.RouterGood

@Route(path = "/login/login")
class LoginActivity : BaseActivity() {
    override fun getLayoutView() = R.layout.logincomponent_activity_login


    override fun initView(state: Bundle?) {
        mTitleDelegate.setTitleContent("登录组件")
    }


    fun jumpToGoods(view: View) {
        ARouter.getInstance().build(RouterGood.GOOD_ACTIVITY).navigation()
    }
}
