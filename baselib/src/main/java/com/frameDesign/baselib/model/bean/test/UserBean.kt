package com.frameDesign.baselib.model.bean.test

import com.frameDesign.baselib.model.bean.internal.DataEmpty

/**
 * 用户信息列表
 *
 * @author JustBlue
 * @time 2018/10/19
 */
class UserBean : DataEmpty {

    companion object {

        val DEF_USER by lazy { UserBean() }
    }

    var name: String = ""
    var organId: String = ""
    var peopleId: String = ""
    var operatorId: String = ""
    var identityType: String = ""


    override fun notEmptyData(): Boolean =
        organId.isNotEmpty() && peopleId.isNotEmpty()

}