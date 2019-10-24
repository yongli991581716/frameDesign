package com.frameDesign.commonlib.uitls.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Base64
import android.view.View
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream


/**
 * 位图工具 单一功能需做多步复杂处理，可在ImageUtils文件内添加方法
 *
 * @author liyong
 * @date 2019-10-22.
 */
object BitmapUtils {

    /**
     * 利用Base64将我们string转换
     *@param str 资源字符串
     * @return 位图
     */
    fun base64ToBitmap(str: String): Bitmap {
        // 利用Base64将我们string转换
        val byteArray = Base64.decode(str, Base64.DEFAULT)
        val byStream = ByteArrayInputStream(byteArray)
        // 生成bitmap
        val img = BitmapFactory.decodeStream(byStream)
        return img
    }

    /**
     * convert Bitmap to byte array
     * @param b 位图
     * @return ByteArray
     */
    fun bitmapToByte(b: Bitmap): ByteArray {
        val o = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.PNG, 100, o)
        return o.toByteArray()
    }

    /**
     * convert byte array to Bitmap
     * @param b 字节
     * @return Bitmap
     */
    fun byteToBitmap(b: ByteArray?): Bitmap? {
        return if (b == null || b.size == 0) null else BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    /**
     * 把bitmap转换成Base64编码String
     * @param bitmap
     * @return String
     */
    fun bitmapToString(bitmap: Bitmap): String {
        return Base64.encodeToString(bitmapToByte(bitmap), Base64.DEFAULT)
    }

    /**
     * convert Drawable to Bitmap
     * @param drawable
     * @return Bitmap
     */
    fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        return if (drawable == null) null else (drawable as BitmapDrawable).bitmap
    }

    /**
     *  convert Bitmap to Drawable
     * @param bitmap 位图
     * @return Drawable
     */
    fun bitmapToDrawable(bitmap: Bitmap?): Drawable? {
        return if (bitmap == null) null else BitmapDrawable(bitmap)
    }

    /**
     *scale image
     *@param org 位图
     *@param newWidth 缩放宽值
     *@param newHeight 缩放高值
     * @return 位图
     */
    fun scaleImageTo(org: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        return scaleImage(
            org,
            newWidth.toFloat() / org.width,
            newHeight.toFloat() / org.height
        )
    }

    /**
     *  scale image
     *@param org 位图
     *@param scaleWidth 缩放宽比例
     *@param scaleHeight 缩放高比例
     * @return 位图
     */
    fun scaleImage(org: Bitmap?, scaleWidth: Float, scaleHeight: Float): Bitmap? {
        if (org == null) {
            return null
        }
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(org, 0, 0, org.width, org.height, matrix, true)
    }

    /**
     *  获取view背景图片
     *@param view
     * @return 位图
     */
    fun getCacheBitmapFromView(view: View): Bitmap? {
        val drawingCacheEnabled = true
        view.isDrawingCacheEnabled = drawingCacheEnabled
        view.buildDrawingCache(drawingCacheEnabled)
        val drawingCache = view.drawingCache
        val bitmap: Bitmap?
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache)
            view.isDrawingCacheEnabled = false
        } else {
            bitmap = null
        }
        return bitmap
    }

    /**
     * 缩放图片，满足条件才缩放
     * @param path String
     * @param newWidth Int
     * @param newHeight Int
     * @param forceScale Boolean 强制缩放到指定大小
     * @return Bitmap
     */
    fun checkScaleBitmap(path: String?, newWidth: Int, newHeight: Int, forceScale: Boolean): Bitmap? {
        if (path.isNullOrEmpty()) {
            return null
        }

        // 获得图片的宽高.
        val bm = getSmallBitmap(path, newWidth, newHeight)
        return checkScaleBitmap(bm, newWidth, newHeight, forceScale)
    }

    /**
     * 缩放图片，满足条件才缩放
     * @param bm Bitmap
     * @param newWidth Int
     * @param newHeight Int
     * @param forceScale Boolean 强制缩放到指定大小
     * @return Bitmap
     */
    fun checkScaleBitmap(origin: Bitmap, newWidth: Int, newHeight: Int, forceScale: Boolean): Bitmap? {
        if (origin == null) {
            return null
        }

        val width = origin.width
        val height = origin.height

        if (!forceScale) {
            if (newWidth > width && newHeight > height) {//图片不需要放大
                return origin
            }
        }

        // 计算缩放比例.
        var scaleWidth = newWidth.toFloat() / width
        var scaleHeight = newHeight.toFloat() / height

        var ratio = if (scaleHeight > scaleWidth) {
            scaleWidth
        } else {
            scaleHeight
        }

        val matrix = Matrix()
        matrix.preScale(ratio, ratio)
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        return newBM
    }

    /**
     * 得到图片宽高
     * @param path String
     * @return IntArray
     */
    fun getBitmapWidthHeight(path: String): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        return intArrayOf(options.outWidth, options.outHeight)
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    fun cropBitmap(bitmap: Bitmap): Bitmap {
        val w = bitmap.width // 得到图片的宽，高
        val h = bitmap.height
        var cropWidth = if (w >= h) h else w// 裁切后所取的正方形区域边长
        cropWidth /= 2
        val cropHeight = (cropWidth / 1.2).toInt()
        return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false)
    }

    /**
     * 旋转变换图像
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    fun rotateBitmap(origin: Bitmap?, alpha: Float): Bitmap? {
        if (origin == null) {
            return null
        }
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.setRotate(alpha)
        // 围绕原地进行旋转
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        return newBM
    }

    /**
     * 偏移效果
     * @param origin 原图
     * @return 偏移后的bitmap
     */
    fun skewBitmap(origin: Bitmap?): Bitmap? {
        if (origin == null) {
            return null
        }
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.postSkew(-0.6f, -0.3f)
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        return newBM
    }

    /**
     * 获取高斯模糊的图片 宽高256
     *
     * @param context 上下文对象
     * @param bitmap  传入的bitmap图片
     * @param radius  模糊度（Radius最大只能设置25.f）
     * @return
     */
    fun blurBitmap(context: Context, bmp: Bitmap?, radius: Int): Bitmap? {
        if (bmp != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //用需要创建高斯模糊bitmap创建一个空的bitmap
            val bitmap = checkScaleBitmap(bmp, 256, 256, false)!!
            val outBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
            val rs = RenderScript.create(context)
            // 创建高斯模糊对象
            val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            // 创建Allocations，此类是将数据传递给RenderScript内核的主要方 法，并制定一个后备类型存储给定类型
            val allIn = Allocation.createFromBitmap(rs, bitmap)
            val allOut = Allocation.createFromBitmap(rs, outBitmap)
            //设定模糊度(注：Radius最大只能设置25.f)
            blurScript.setRadius(radius.toFloat())
            // Perform the Renderscript
            blurScript.setInput(allIn)
            blurScript.forEach(allOut)
            // Copy the final bitmap created by the out Allocation to the outBitmap
            allOut.copyTo(outBitmap)
            // recycle the original bitmap
            // After finishing everything, we destroy the Renderscript.
            rs.destroy()

            return outBitmap
        }

        return bmp
    }


    /**
     * 按照给定的宽高，配置最小采集率，获取位图
     *@param filePath 文件路径
     *@param reqWidth 宽
     *@param reqHeight 高
     * @return 位图
     */
    fun getSmallBitmap(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize =
            calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    /**
     * 计算采集率值
     *@param options 采集率对象
     *@param reqWidth 宽
     *@param reqHeight 高
     * @return 采集率大小
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val h = options.outHeight
        val w = options.outWidth
        var inSampleSize = 0
        if (h > reqHeight || w > reqWidth) {
            val ratioW = w.toFloat() / reqWidth
            val ratioH = h.toFloat() / reqHeight
            inSampleSize = Math.min(ratioH, ratioW).toInt()
        }
        inSampleSize = Math.max(1, inSampleSize)
        return inSampleSize
    }

    /**
     * 使用默认缩放比例 宽高：600*1200
     */
    fun scaleBitmap(context: Context, res: Any): Bitmap? {

        var bitmap: Bitmap? = null
        when (res) {
            is Int -> bitmap = BitmapFactory.decodeResource(context.resources, res, getSampleOptions(context, res))
            is String -> bitmap = BitmapFactory.decodeFile(res, getSampleOptions(context, res))
            is ByteArray -> bitmap = BitmapFactory.decodeByteArray(res, 0, res.size, getSampleOptions(context, res))
            is InputStream -> bitmap = BitmapFactory.decodeStream(res, null, getSampleOptions(context, res))
        }
        return bitmap
    }


    /**
     * 默认控制宽高：600*1200
     */
    private fun getSampleOptions(
        context: Context,
        res: Any,
        defaultWidth: Int = 600,
        defaultHeight: Int = 1200
    ): BitmapFactory.Options {

        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        when (res) {
            is Int -> BitmapFactory.decodeResource(context.resources, res, options)
            is String -> BitmapFactory.decodeFile(res, options)
            is ByteArray -> BitmapFactory.decodeByteArray(res, 0, res.size, options)
            is InputStream -> BitmapFactory.decodeStream(res, null, options)
        }

        var width = options.outWidth
        var height = options.outHeight

        var sampleOption = 1
        if (width > defaultWidth || height > defaultHeight) {
            var tempWidht = width / 2
            var tempHeight = height / 2
            while (tempWidht > defaultWidth && tempHeight > defaultHeight) {
                tempWidht /= 2
                tempHeight /= 2
                sampleOption *= 2
            }
        }
        options.inJustDecodeBounds = false
        options.inSampleSize = sampleOption
        return options
    }

}