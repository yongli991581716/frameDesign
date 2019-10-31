package com.frameDesign.baselib.controller

import android.app.Activity
import android.content.Intent
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.frameDesign.commonlib.uitls.CollectionUtils
import com.frameDesign.commonlib.uitls.SystemUtil
import com.frameDesign.commonlib.uitls.permission.IPermissionListener
import com.frameDesign.commonlib.uitls.permission.PermissionFactory
import com.frameDesign.commonlib.uitls.permission.PermissonBean
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

abstract class BaseImageActivity : BaseActivity() {

    private var blCropImage = false//调用相册时候，是否需要剪切图片
    private var blSquare = false//调用相册时候，是否需要正方形剪切

    val REQUEST_CODE_PICK_IMAGE = 1230
    val REQUEST_CROP_IMAGE = 1231
    val INTENT_CROP_SQUARE = "INTENT_CROP_SQUARE"
    val INTENT_FILE_PATH = "INTENT_FILE_PATH"
    /**
     * 剪切的图片
     */
    abstract fun croppedImage(imgFile: String)

    /**
     * 相册的图片
     */
    abstract fun pickImage(imgs: List<Image>)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CROP_IMAGE -> {
                    if (data != null) {
                        val imgPath = data.getStringExtra(INTENT_FILE_PATH)
                        croppedImage(imgPath)
                    }
                }
                REQUEST_CODE_PICK_IMAGE -> {
                    if (data != null) {
                        val imgs = ImagePicker.getImages(data)
                        if (blCropImage) {
//                            imgs?.let {
//                                startActivityForResult<CropImageActivity>(ActivityRequestCode.REQUEST_CROP_IMAGE,
//                                        INTENT_FILE_PATH to imgs[0].path,
//                                        INTENT_CROP_SQUARE to blSquare)
//                            }
                        } else {
                            pickImage(imgs)
                        }
                    }
                }
            }
        }
    }

    /**
     * 得到图片
     * limit: 最大选择的图片数量
     * blCropImage: 调用相册时候，是否需要剪切图片
     * blSquare: 调用相册时候，是否需要正方形剪切
     */
    fun gotoImagePicker(limit: Int, blCropImage: Boolean = false, blSquare: Boolean = false) {

        PermissionFactory.execute(PermissonBean(
            this,
            arrayOf(
                Permission.CAMERA,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE
            )
        ), object : IPermissionListener {
            override fun onGranted(permission: PermissonBean) {
                //授权成功
                this@BaseImageActivity.blCropImage = blCropImage
                this@BaseImageActivity.blSquare = blSquare

                val pickerCreate = ImagePicker.create(this@BaseImageActivity)
                if (limit == 1) {
                    pickerCreate
                        .single()
                        .start(REQUEST_CODE_PICK_IMAGE)
                } else if (limit > 1) {
                    pickerCreate
                        .limit(limit)
                        .start(REQUEST_CODE_PICK_IMAGE)
                }
            }

            override fun onDenied(permission: PermissonBean) {
                //授权失败
                if (AndPermission.hasAlwaysDeniedPermission(
                        this@BaseImageActivity,
                        CollectionUtils.arrayToList(permission.permissions)
                    )
                ) {
                    // 这些权限被用户总是拒绝。
                    SystemUtil.gotoPermissionSettings(this@BaseImageActivity)
                }
            }

        })
    }

    /**
     * 得到图片，仅仅只用拍照
     * blCropImage: 调用相册时候，是否需要剪切图片
     * blSquare: 调用相册时候，是否需要正方形剪切
     */
    fun gotoCamera(blCropImage: Boolean = false, blSquare: Boolean = false) {

        PermissionFactory.execute(PermissonBean(
            this,
            arrayOf(
                Permission.CAMERA,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE
            )
        ), object : IPermissionListener {
            override fun onGranted(permission: PermissonBean) {
                //授权成功
                this@BaseImageActivity.blCropImage = blCropImage
                this@BaseImageActivity.blSquare = blSquare

                ImagePicker.cameraOnly().start(this@BaseImageActivity, REQUEST_CODE_PICK_IMAGE)
//                    pickerCreate.theme(R.style.ImagePickerTheme)
            }

            override fun onDenied(permission: PermissonBean) {
                //授权失败
                if (AndPermission.hasAlwaysDeniedPermission(
                        this@BaseImageActivity,
                        CollectionUtils.arrayToList(permission.permissions)
                    )
                ) {
                    // 这些权限被用户总是拒绝。
                    SystemUtil.gotoPermissionSettings(this@BaseImageActivity)
                }
            }

        })

    }

}