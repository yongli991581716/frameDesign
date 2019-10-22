package com.frameDesign.commonlib.uitls.permission

/**
 * 权限监听
 * @author liyong
 * @date 2019-10-22
 */
interface IPermissionListener {
    abstract fun onGranted(permission: PermissonBean)

    abstract fun onDenied(permission: PermissonBean)
}