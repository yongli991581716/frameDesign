package com.framedesign.goodcomponent

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.frameDesign.commonreslib.const.router.RouterLogin

@Route(path = "/good/good")
class GoodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.goodcomponent_activity_good)
    }

    fun jumpToLogin(view: View) {
        ARouter.getInstance().build(RouterLogin.LOGIN_ACTIVITY).navigation()
    }
}
