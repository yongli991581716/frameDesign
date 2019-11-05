package com.framedesign.goodcomponent

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.frameDesign.baselib.controller.BaseActivity
import com.frameDesign.commonreslib.const.router.RouterLogin

@Route(path = "/good/good")
class GoodActivity : BaseActivity() {
    override fun getLayoutView() = R.layout.goodcomponent_activity_good

    override fun initView(state: Bundle?) {
        mTitleDelegate.setTitleContent("商品组件")
    }

    fun jumpToLogin(view: View) {
        ARouter.getInstance().build(RouterLogin.LOGIN_ACTIVITY).navigation()
    }
}
