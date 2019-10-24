package com.frameDesign.commonlib.uitls.animation

import android.graphics.Camera
import android.hardware.camera2.CameraManager
import android.view.SurfaceView
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation

/**
 * 绕中心Y轴旋转
 * Created by liyong on 2019-04-29.
 */
class RotateYAnimation : Animation() {

    private var mCamera = Camera()
    private var mCenterX: Float = 0f
    private var mCenterY: Float = 0f

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        mCenterX = (width / 2).toFloat()
        mCenterY = (height / 2).toFloat()
        duration = 1000
        repeatCount = 1200
        interpolator = LinearInterpolator()

    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)

        mCamera.save()

        var matrix = t?.matrix
        mCamera.rotateY(360 * interpolatedTime)

        mCamera.getMatrix(matrix)

        matrix?.preTranslate(-mCenterX, -mCenterY)
        matrix?.postTranslate(mCenterX, mCenterY)

        mCamera.restore()
    }
}