package com.frameDesign.baselib.model.bean.test

/**
 * ..
 *
 * @author JustBlue
 * @time 2018/10/16
 */
data class TestUserBean(
    var user: User = User(),
    var token: String = "",
    var timestamp: Long = 0
)

data class User(
    var id: String = "",
    var avatar: String = "",
    var sex: String = "",
    var realName: String = "",
    var phone: String = "",
    var account: String = "",
    var nativePlace: String = "",
    var bloodType: String = "",
    var ethnic: String = "",
    var inviteCode: InviteCode = InviteCode(),
    var addressList: List<Any> = listOf(),
    var authInfo: Any? = Any(),
    var bodyInfo: BodyInfo = BodyInfo(),
    var loginInfoList: List<LoginInfo> = listOf(),
    var nickName: String = "",
    var source: Any? = Any(),
    var archivesNo: String = "",
    var profession: String = "",
    var authStatus: String = "",
    var activeStatus: String = "",
    var createTime: Long = 0,
    var agencyLevel: String = "",
    var vipFlag: Boolean = false,
    var freezeFlag: Any? = Any(),
    var vestFlag: Boolean = false,
    var act: String = ""
)

data class InviteCode(
    var code: String = ""
)

data class BodyInfo(
    var height: Int = 0,
    var weight: Int = 0,
    var birthDate: String = ""
)

data class LoginInfo(
    var mark: String = "",
    var source: String = "",
    var bindStatus: Boolean = false
)