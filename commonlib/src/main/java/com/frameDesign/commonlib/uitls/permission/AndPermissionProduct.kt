package com.frameDesign.commonlib.uitls.permission

import com.yanzhenjie.permission.AndPermission

/**
 * 权限库封装
 * @author liyong
 * @date 2019-10-22
 */
class AndPermissionProduct : PermissionProduct() {
    override fun execute(permission: PermissonBean, listener: IPermissionListener?) {
        AndPermission.with(permission.activity).runtime().permission(permission.permissions).onGranted { permissions ->
            listener?.onGranted(permission)
        }.onDenied { permissions ->
            listener?.onDenied(permission)
        }.start()
    }


}