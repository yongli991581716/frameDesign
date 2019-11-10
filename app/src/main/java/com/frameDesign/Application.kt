package com.frameDesign

import com.frameDesign.baselib.BaseApplication
import com.frameDesign.baselib.const.HttpConfig


/**
 * Created by liyong on 2019-10-21.
 */
class Application : BaseApplication() {

    override fun initContext() {
        super.initContext()
        mCtx = this

    }

    override fun initConfig() {
        super.initConfig()
        HttpConfig.bindEnvConfig(BuildConfig.API_HOST)
    }

    override fun initDatas() {
        super.initDatas()

    }


}