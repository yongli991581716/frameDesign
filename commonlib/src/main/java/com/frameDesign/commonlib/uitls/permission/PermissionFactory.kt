package com.frameDesign.commonlib.uitls.permission

/**
 * 权限辅助类
 * @author liyong
 * @date 2019-10-22
 */
object PermissionFactory {

    private lateinit var mPermissionProduct: PermissionProduct

    fun init(permissionProduct: PermissionProduct) {
        mPermissionProduct = permissionProduct
    }


    /**
     * 权限执行
     */
    fun execute(permission: PermissonBean, listener: IPermissionListener? = null) {
        mPermissionProduct.execute(permission, listener)
    }
}