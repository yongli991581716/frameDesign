package com.frameDesign.baselib.utils

import android.app.Activity
import android.graphics.Bitmap
import com.frameDesign.baselib.R
import com.frameDesign.baselib.controller.BaseActivity
import com.frameDesign.baselib.model.bean.actions.ActionsH5ShareBean
import com.frameDesign.commonlib.uitls.DialogUtils
import com.frameDesign.commonlib.uitls.LogUtils
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import org.jetbrains.anko.toast


/**
 * umeng分享工具
 *
 * @author liyong
 * @date 2018/1/22.
 */
object UmengUtils {

    /**
     * 分享类型
     */
    object ShareType {
        val WECHAT = "wechat"//微信
        val MOMENTS = "moments"//朋友圈
        val QQ = "qq"//QQ
        val SINA = "weibo"//weibo
    }

    /**
     *分享平台(选择好友、朋友圈)
     */
    fun share(act: BaseActivity, content: ActionsH5ShareBean?, umLis: UMShareListener?) {
        if (content != null) {
            val lis = umLis ?: object : UMShareListener {
                override fun onResult(share_media: SHARE_MEDIA?) {
                    act.toast("分享成功")
                }

                override fun onCancel(share_media: SHARE_MEDIA?) {
                    act.toast("分享取消")
                }

                override fun onError(share_media: SHARE_MEDIA?, throwable: Throwable?) {
                    val err = throwable?.message
                    LogUtils.d("UmengUtils err=$err")
                    act.toast("分享失败")
                }

                override fun onStart(share_media: SHARE_MEDIA?) {
                    act.showProgress("正在分享...")
                    act.delayHideProgress()
                }
            }

            AndPermission.with(act).runtime()
                .permission(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
                .onGranted {
                    val mShare = UMShareAPI.get(act)
                    val thumb = UMImage(act, content.picUrl)
                    thumb.setThumb(thumb)
                    //val inviteCode = MemCache.getUser()?.inviteCode?.code
                    //val surl = "${content.url}#/reg?code=$inviteCode"
                    val web = UMWeb(content.url)
                    web.title = content.title//标题
                    web.setThumb(thumb)  //缩略图
                    web.description = content.desc//描述

                    ShareAction(act)
//                                .withText(content.title)
//                                .withMedia(web)
                        .setDisplayList(
                            SHARE_MEDIA.WEIXIN,
                            SHARE_MEDIA.WEIXIN_CIRCLE/*, SHARE_MEDIA.QQ*/
                        )
//                                .setCallback(lis)
                        .setShareboardclickCallback { snsPlatform, sharE_MEDIA ->
                            when (sharE_MEDIA) {
                                SHARE_MEDIA.WEIXIN -> {//分享微信
                                    if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN)) {
                                        ShareAction(act)
                                            .withText(content.title)
                                            .withMedia(web)
                                            .setPlatform(SHARE_MEDIA.WEIXIN)
                                            .setCallback(lis)
                                            .share()
                                    } else {
                                        act.toast(R.string.pls_install_wx)
                                    }
                                }
                                SHARE_MEDIA.WEIXIN_CIRCLE -> {//分享微信朋友圈
                                    if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN_CIRCLE)) {
                                        ShareAction(act)
                                            .withText(content.title)
                                            .withMedia(web)
                                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                            .setCallback(lis)
                                            .share()
                                    } else {
                                        act.toast(R.string.pls_install_wx)
                                    }
                                }
//                                        SHARE_MEDIA.QQ -> {//分享QQ
//                                            if (mShare.isInstall(act, SHARE_MEDIA.QQ)) {
//                                                ShareAction(act)
//                                                        .withText(content.title)
//                                                        .withMedia(web)
//                                                        .setPlatform(SHARE_MEDIA.QQ)
//                                                        .setCallback(lis)
//                                                        .share()
//
//                                            } else {
//                                                act.toast(R.string.pls_install_qq)
//                                            }
//                                        }
//                                        else -> {
//                                        }
                            }
                        }
                        .open()
                }.onDenied {
                    if (AndPermission.hasAlwaysDeniedPermission(act, it)) {
                        // 这些权限被用户总是拒绝。
                        DialogUtils.createSettingDialog(act).show()
                    }
                }
                .start()
        }
    }

    /**
     *分享平台(选择好友、朋友圈)
     */
    fun shareShop(act: BaseActivity, content: ActionsH5ShareBean?, umLis: UMShareListener?) {
        if (content != null) {
            val lis = umLis ?: object : UMShareListener {
                override fun onResult(share_media: SHARE_MEDIA?) {
                    act.toast("分享成功")
                }

                override fun onCancel(share_media: SHARE_MEDIA?) {
                    act.toast("分享取消")
                }

                override fun onError(share_media: SHARE_MEDIA?, throwable: Throwable?) {
                    val err = throwable?.message
                    LogUtils.d("UmengUtils err=$err")
                    act.toast("分享失败")
                }

                override fun onStart(share_media: SHARE_MEDIA?) {
                    act.showProgress("正在分享...")
                    act.delayHideProgress()
                }
            }

            AndPermission.with(act).runtime()
                .permission(
                    Permission.ACCESS_FINE_LOCATION,
                    Permission.ACCESS_COARSE_LOCATION,
                    Permission.WRITE_EXTERNAL_STORAGE,
                    Permission.ACCESS_FINE_LOCATION
                )
                .onGranted {
                    val mShare = UMShareAPI.get(act)
                    val thumb = UMImage(act, content.picUrl)
                    thumb.setThumb(thumb)
                    //val inviteCode = MemCache.getUser()?.inviteCode?.code
                    //val surl = "${content.url}#/reg?code=$inviteCode"
                    val web = UMWeb(content.url)
                    web.title = content.title//标题
                    web.setThumb(thumb)  //缩略图
                    web.description = content.desc//描述

                    ShareAction(act)
//                                .withText(content.title)
//                                .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                                .setCallback(lis)
                        .setShareboardclickCallback { snsPlatform, sharE_MEDIA ->
                            when (sharE_MEDIA) {
                                SHARE_MEDIA.WEIXIN -> {//分享微信
                                    if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN)) {
                                        ShareAction(act)
                                            .withText(content.title)
                                            .withMedia(web)
                                            .setPlatform(SHARE_MEDIA.WEIXIN)
                                            .setCallback(lis)
                                            .share()
                                    } else {
                                        act.toast(R.string.pls_install_wx)
                                    }
                                }
                                SHARE_MEDIA.WEIXIN_CIRCLE -> {//分享微信朋友圈
                                    if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN_CIRCLE)) {
                                        ShareAction(act)
                                            .withText(content.title)
                                            .withMedia(web)
                                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                            .setCallback(lis)
                                            .share()
                                    } else {
                                        act.toast(R.string.pls_install_wx)
                                    }
                                }
//                                        SHARE_MEDIA.QQ -> {//分享QQ
//                                            if (mShare.isInstall(act, SHARE_MEDIA.QQ)) {
//                                                ShareAction(act)
//                                                        .withText(content.title)
//                                                        .withMedia(web)
//                                                        .setPlatform(SHARE_MEDIA.QQ)
//                                                        .setCallback(lis)
//                                                        .share()
//
//                                            } else {
//                                                act.toast(R.string.pls_install_qq)
//                                            }
//                                        }
//                                        SHARE_MEDIA.QZONE -> {
//                                            if (mShare.isInstall(act, SHARE_MEDIA.QZONE)) {
//                                                ShareAction(act)
//                                                        .withText(content.title)
//                                                        .withMedia(web)
//                                                        .setPlatform(SHARE_MEDIA.QZONE)
//                                                        .setCallback(lis)
//                                                        .share()
//
//                                            } else {
//                                                act.toast(R.string.pls_install_qq)
//                                            }
//                                        }
                                else -> {
                                }
                            }
                        }
                        .open()
                }.onDenied {
                    if (AndPermission.hasAlwaysDeniedPermission(act, it)) {
                        // 这些权限被用户总是拒绝。
                        DialogUtils.createSettingDialog(act).show()
                    }
                }
                .start()
        }
    }

    /**
     *分享平台(选择好友、朋友圈)
     */
    fun shareImage(act: BaseActivity, bmp: Bitmap?, umLis: UMShareListener?) {
        if (bmp != null) {
            val lis = umLis ?: object : UMShareListener {
                override fun onResult(share_media: SHARE_MEDIA?) {
                    act.toast("分享成功")
                }

                override fun onCancel(share_media: SHARE_MEDIA?) {
                    act.toast("分享取消")
                }

                override fun onError(share_media: SHARE_MEDIA?, throwable: Throwable?) {
                    val err = throwable?.message
                    LogUtils.d("UmengUtils err=$err")
                    act.toast("分享失败")
                }

                override fun onStart(share_media: SHARE_MEDIA?) {
                    act.showProgress("正在分享...")
                    act.delayHideProgress()
                }
            }

            AndPermission.with(act).runtime()
                .permission(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
                .onGranted {
                    val mShare = UMShareAPI.get(act)
                    val thumb = UMImage(act, bmp)
                    thumb.setThumb(thumb)

                    ShareAction(act)
//                                    .withMedia(thumb)
                        .setDisplayList(
                            SHARE_MEDIA.WEIXIN,
                            SHARE_MEDIA.WEIXIN_CIRCLE,
                            SHARE_MEDIA.QQ
                        )
//                                .setCallback(lis)
                        .setShareboardclickCallback { snsPlatform, sharE_MEDIA ->
                            when (sharE_MEDIA) {
                                SHARE_MEDIA.WEIXIN -> {//分享微信
                                    if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN)) {
                                        ShareAction(act)
                                            .withMedia(thumb)
                                            .setPlatform(SHARE_MEDIA.WEIXIN)
                                            .setCallback(lis)
                                            .share()
                                    } else {
                                        act.toast(R.string.pls_install_wx)
                                    }
                                }
                                SHARE_MEDIA.WEIXIN_CIRCLE -> {//分享微信朋友圈
                                    if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN_CIRCLE)) {
                                        ShareAction(act)
                                            .withMedia(thumb)
                                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                            .setCallback(lis)
                                            .share()
                                    } else {
                                        act.toast(R.string.pls_install_wx)
                                    }
                                }
                                SHARE_MEDIA.QQ -> {//分享QQ
                                    if (mShare.isInstall(act, SHARE_MEDIA.QQ)) {
                                        ShareAction(act)
                                            .withMedia(thumb)
                                            .setPlatform(SHARE_MEDIA.QQ)
                                            .setCallback(lis)
                                            .share()

                                    } else {
                                        act.toast(R.string.pls_install_qq)
                                    }
                                }
                                else -> {
                                }
                            }
                        }
                        .open()
                }.onDenied {
                    if (AndPermission.hasAlwaysDeniedPermission(act, it)) {
                        // 这些权限被用户总是拒绝。
                        DialogUtils.createSettingDialog(act).show()
                    }
                }
                .start()
        }
    }

    /**
     *直接分享平台
     * channel: H5RequestBean channel
     */
    fun share(
        act: BaseActivity,
        channel: String,
        content: ActionsH5ShareBean?,
        umLis: UMShareListener?
    ) {
        if (content != null) {
            val lis = umLis ?: object : UMShareListener {
                override fun onResult(share_media: SHARE_MEDIA?) {
                    act.toast("分享成功")
                }

                override fun onCancel(share_media: SHARE_MEDIA?) {
                    act.toast("分享取消")
                }

                override fun onError(share_media: SHARE_MEDIA?, throwable: Throwable?) {
                    val err = throwable?.message
                    LogUtils.d("UmengUtils err=$err")
                    act.toast("分享失败")
                }

                override fun onStart(share_media: SHARE_MEDIA?) {
                    act.showProgress("正在分享...")
                    act.delayHideProgress()
                }
            }

            AndPermission.with(act).runtime()
                .permission(
                    Permission.WRITE_EXTERNAL_STORAGE,
                    Permission.ACCESS_FINE_LOCATION,
                    Permission.READ_EXTERNAL_STORAGE
                )
                .onGranted {
                    val mShare = UMShareAPI.get(act)
                    val thumb = UMImage(act, content.picUrl)
                    thumb.setThumb(thumb)

//                        val inviteCode = MemCache.getUser()?.inviteCode?.code
//                        val surl = "${content.url}#/reg?code=$inviteCode&phone=${MemCache.getUser()?.phone}&id=${MemCache.getUser()?.id}"
                    val web = UMWeb("xxx")

                    web.title = content.title//标题
                    web.setThumb(thumb)  //缩略图
                    web.description = content.desc//描述

                    when (channel) {
                        ShareType.WECHAT -> {//分享微信
                            if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN)) {
                                ShareAction(act)
                                    .withText(content.title)
                                    .withMedia(web)
                                    .setPlatform(SHARE_MEDIA.WEIXIN)
                                    .setCallback(lis)
                                    .share()
                            } else {
                                act.toast(R.string.pls_install_wx)
                            }
                        }
                        ShareType.MOMENTS -> {//分享微信朋友圈
                            if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN_CIRCLE)) {
                                ShareAction(act)
                                    .withText(content.title)
                                    .withMedia(web)
                                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                    .setCallback(lis)
                                    .share()
                            } else {
                                act.toast(R.string.pls_install_wx)
                            }

                        }
                        ShareType.QQ -> {//分享QQ
                            if (mShare.isInstall(act, SHARE_MEDIA.QQ)) {
                                ShareAction(act)
                                    .withText(content.title)
                                    .withMedia(web)
                                    .setPlatform(SHARE_MEDIA.QQ)
                                    .setCallback(lis)
                                    .share()

                            } else {
                                act.toast(R.string.pls_install_qq)
                            }

                        }
                    }
                }.onDenied {
                    if (AndPermission.hasAlwaysDeniedPermission(act, it)) {
                        // 这些权限被用户总是拒绝。
                        DialogUtils.createSettingDialog(act).show()
                    }
                }
                .start()
        }
    }

    /**
     *直接分享平台
     * channel: H5RequestBean channel
     */
    fun shareImage(act: BaseActivity, channel: String?, bitmap: Bitmap?, umLis: UMShareListener?) {
        if (bitmap != null && channel != null) {
            val lis = umLis ?: object : UMShareListener {
                override fun onResult(share_media: SHARE_MEDIA?) {
                    act.toast("分享成功")
                }

                override fun onCancel(share_media: SHARE_MEDIA?) {
                    act.toast("分享取消")
                }

                override fun onError(share_media: SHARE_MEDIA?, throwable: Throwable?) {
                    val err = throwable?.message
                    LogUtils.d("UmengUtils err=$err")
                    act.toast("分享失败")
                }

                override fun onStart(share_media: SHARE_MEDIA?) {
                    act.showProgress("正在分享...")
                    act.delayHideProgress()
                }
            }

            AndPermission.with(act).runtime()
                .permission(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
                .onGranted {
                    val mShare = UMShareAPI.get(act)
                    val thumb = UMImage(act, bitmap)
                    thumb.setThumb(thumb)

                    when (channel) {
                        ShareType.WECHAT -> {//分享微信
                            if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN)) {
                                ShareAction(act)
                                    .withMedia(thumb)
                                    .setPlatform(SHARE_MEDIA.WEIXIN)
                                    .setCallback(lis)
                                    .share()
                            } else {
                                act.toast(R.string.pls_install_wx)
                            }
                        }
                        ShareType.MOMENTS -> {//分享微信朋友圈
                            if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN_CIRCLE)) {
                                ShareAction(act)
                                    .withMedia(thumb)
                                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                    .setCallback(lis)
                                    .share()
                            } else {
                                act.toast(R.string.pls_install_wx)
                            }

                        }
                        ShareType.QQ -> {//分享QQ
                            if (mShare.isInstall(act, SHARE_MEDIA.QQ)) {
                                ShareAction(act)
                                    .withMedia(thumb)
                                    .setPlatform(SHARE_MEDIA.QQ)
                                    .setCallback(lis)
                                    .share()

                            } else {
                                act.toast(R.string.pls_install_qq)
                            }

                        }
                    }
                }.onDenied {
                    if (AndPermission.hasAlwaysDeniedPermission(act, it)) {
                        // 这些权限被用户总是拒绝。
                        DialogUtils.createSettingDialog(act).show()
                    }
                }
                .start()
        }
    }

    /**
     * 微信认证
     */
    fun authWeixin(act: Activity, lis: UMAuthListener) {
        val mShare = UMShareAPI.get(act)
        if (mShare.isInstall(act, SHARE_MEDIA.WEIXIN)) {
            mShare.getPlatformInfo(act, SHARE_MEDIA.WEIXIN, lis)

        } else {
            act.toast(R.string.pls_install_wx)
        }
    }
}