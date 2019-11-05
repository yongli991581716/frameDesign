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

    private val MAX_MEMORY = Runtime.getRuntime().maxMemory() / 4
    private const val MAX_CACHE_SIZE = 30L * ByteConstants.MB
    private val CACHE_SIZE by lazy {
        var value = if (MAX_MEMORY < MAX_CACHE_SIZE) {
            MAX_MEMORY
        } else {
            MAX_CACHE_SIZE
        }
        value
    }
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