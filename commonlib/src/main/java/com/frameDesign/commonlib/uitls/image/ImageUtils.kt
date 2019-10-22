package com.frameDesign.commonlib.uitls.image

import android.graphics.Bitmap
import com.frameDesign.commonlib.uitls.FileUtils
import java.io.File

/**
 *图片工具 进一步
 *
 * @author liyong
 * @date 2019-10-22.
 */
object ImageUtils {

    /**
     * 常规文件大小单位
     */
    val SIZE_1K = 1024
    val SIZE_1M = SIZE_1K * SIZE_1K//1M

    val THUMBNAIL_HEIGHT = 256//小图上大小
    val IMAGE_HEIGHT = 1280//大图大小
    val IMAGE_HEIGHT_BIG = 1920//大图大小

    /**
     * 得到缩略图
     *@param   filePath 文件路径
     * @return 位图
     */
    fun getThumbnailBitmap(filePath: String): Bitmap? {
        try {
            return BitmapUtils.getSmallBitmap(
                filePath,
                THUMBNAIL_HEIGHT,
                THUMBNAIL_HEIGHT
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 得到缩略文件
     * @param   filePath 文件路径
     * @return 位图
     */
    fun getThumbnailFile(filePath: String): File? {
        val bmp = getThumbnailBitmap(filePath)
        val file = FileUtils.getRandomThumbnailFile()
        if (FileUtils.saveBitmap(bmp, file.path)) {
            return file
        }
        return null
    }

    /**
     * 得到压缩图片到指定大小，并保存成文件，默认1M
     * maxLength：指定大小
     * @param   filePath 文件路径
     * @return 位图
     */
    fun resizeImageToFile(filePath: String): File? {
        return saveImageToFile(filePath)
    }

    /**
     * 存储图片
     * @param filePath String
     * @return File?
     */
    fun saveImageToFile(filePath: String): File? {
        return saveImageToFile(
            filePath,
            IMAGE_HEIGHT,
            IMAGE_HEIGHT
        )
    }

    /**
     * 存储图片
     * @param filePath String
     * @return File?
     */
    fun saveImageToFileBig(filePath: String): File? {
        return saveImageToFile(
            filePath,
            IMAGE_HEIGHT_BIG,
            IMAGE_HEIGHT_BIG
        )
    }

    /**
     * 存储图片
     * @param filePath String
     * @param w String
     * @param h String
     * @return File?
     */
    fun saveImageToFile(filePath: String, w: Int, h: Int): File? {
        val bmp = BitmapUtils.checkScaleBitmap(filePath, w, h, false)
        val file = FileUtils.getRandomImageFile()
        FileUtils.saveBitmap(bmp, file)
        return file
    }

}