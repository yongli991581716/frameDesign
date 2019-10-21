package com.frameDesign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.frameDesign.baseres.const.router.RouterLogin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun  jumpToGood(view: View){
        ARouter.getInstance().build(RouterLogin.LOGIN_ACTIVITY).navigation()
    }
}
