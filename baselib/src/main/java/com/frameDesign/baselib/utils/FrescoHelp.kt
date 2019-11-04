package com.frameDesign.baselib.utils

import android.content.Context
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.frameDesign.baselib.BaseApplication
import com.frameDesign.commonlib.uitls.FileUtils

/**
 * fresco图片库配置
 * @author liyong
 * @date 2019-11-04
 */
object FrescoHelp {

    private const val CACHE_SIZE = 50L * ByteConstants.MB
    private const val FILE_NAME = "fresco_images"

    fun getConfig(context: Context): ImagePipelineConfig {
        val cacheConfig = DiskCacheConfig.newBuilder(context)
            .setBaseDirectoryName(FILE_NAME)
            .setBaseDirectoryPath(FileUtils.getCacheFile())
            .setMaxCacheSize(CACHE_SIZE)
            .build()
        return ImagePipelineConfig.newBuilder(BaseApplication.mCtx)
            .setMainDiskCacheConfig(cacheConfig)
            .build()
    }
}