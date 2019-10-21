package com.frameDesign.logincomponent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.frameDesign.baseres.const.router.RouterGood

@Route(path = "/login/login")
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logincomponent_activity_login)
    }


    fun jumpToGoods(view: View){
        ARouter.getInstance().build(RouterGood.GOOD_ACTIVITY).navigation()
    }
}
