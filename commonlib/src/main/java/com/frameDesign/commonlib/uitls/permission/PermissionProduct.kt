package com.frameDesign.commonlib.uitls.permission

/**
 * description
 * @author liyong
 * @date 2019-10-22
 */
abstract class PermissionProduct {

    abstract  fun execute(permission: PermissonBean,listener: IPermissionListener? =null)

}