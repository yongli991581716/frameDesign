package com.frameDesign.baselib.model.bean.test

import com.frameDesign.baselib.model.bean.internal.DataEmpty

/**
 * ..
 *
 * @author JustBlue
 * @time 2018/10/19
 */
class UserTokenBean : DataEmpty {

    var token: String = ""
    var timestamp: Long = 0
    var user: UserBean? = null


    override fun notEmptyData(): Boolean =
        user?.notEmptyData() ?: false

}
