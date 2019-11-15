package com.frameDesign.commonlib.expand

import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequest.RequestLevel
import com.facebook.imagepipeline.request.ImageRequestBuilder


/**
 *图片库扩展（fresco）
 * @author liyong
 * @date 2019-10-25
 */
/**
 * 以高斯模糊显示, 使用[Fresco]自带实现
 *
 * @param url        url.
 * @param iteration  迭代次数，越大越模糊。
 * @param blurRadius 模糊图半径，必须大于0，越大越模糊。
 */
fun SimpleDraweeView.displayBlurImage(
    url: String?,
    iteration: Int = 6,
    blurRadius: Int = 6.dp2px(),
    forceNetwork: Boolean = false
) {
    try {

        val uri = clearCacheURI(url, forceNetwork)
        val request = ImageRequestBuilder.newBuilderWithSource(uri)
            .setPostprocessor(IterativeBoxBlurPostProcessor(iteration, blurRadius))
            // 限制Fresco输出尺寸, 由于图片最后会模糊(都会看不清),
            // 使用小尺寸图片能减轻内存消耗和提升模糊速率
            .setResizeOptions(ResizeOptions.forDimensions(150, 150))
            .build()
        val controller = Fresco.newDraweeControllerBuilder()
            .setOldController(this.controller)
            .setImageRequest(request)
            .build()
        setController(controller)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}


/**
 * 以渐进式显示, 使用[Fresco]自带实现
 * 渐进式JPEG图仅仅支持网络图。本地图片会一次解码完成，所以没必要渐进式加载。
 * 你还需要知道的是，并不是所有的JPEG图片都是渐进式编码的，所以对于这类图片，不可能做到渐进式加载。
 * @param url        url
 */
fun SimpleDraweeView.setProgressImageURI(
    url: String?,
    forceNetwork: Boolean = false
) {
    try {
        val uri = clearCacheURI(url, forceNetwork)
        val request = ImageRequestBuilder.newBuilderWithSource(uri)
            .setProgressiveRenderingEnabled(true)
            .build()
        val controller = Fresco.newDraweeControllerBuilder()
            .setImageRequest(request)
            .setOldController(this.controller)
            .build()
        this.controller = controller
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


/**
 * 以分辨率策略显示（先显示低分率，最后显示高分辨率图片）, 使用[Fresco]自带实现
 * 先显示低分辨率的图，然后是高分辨率的图
 * @param url        url
 */
fun SimpleDraweeView.setResolutionStrategyImage(
    lowResUri: String?,
    highResUri: String?,
    forceNetwork: Boolean = false
) {
    try {
        val lowResUri = clearCacheURI(lowResUri, forceNetwork)
        val highResUri = clearCacheURI(highResUri, forceNetwork)
        val controller = Fresco.newDraweeControllerBuilder()
            .setLowResImageRequest(ImageRequest.fromUri(lowResUri))
            .setImageRequest(ImageRequest.fromUri(highResUri))
            .setOldController(this.controller)
            .build()
        setController(controller)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


/**l
 * 设置图片url
 * @param url        url
 */
fun SimpleDraweeView.setImageURL(
    url: String?,
    lowestLevel: RequestLevel = RequestLevel.FULL_FETCH,
    forceNetwork: Boolean = false
) {
    try {
        val uri = clearCacheURI(url, forceNetwork)

        val request = ImageRequestBuilder
            .newBuilderWithSource(uri)
            .setLowestPermittedRequestLevel(lowestLevel)//最低级别下载，默认最低级别从内存中获取
            .build()


        val controller = Fresco.newDraweeControllerBuilder()
            .setImageRequest(request)
            .setOldController(this.controller)
            .build()
        setController(controller)
    } catch (e: Exception) {
        e.printStackTrace()
    }


}

/**
 *根据uri清除缓存及磁盘中文件
 */
fun clearCacheURI(url: String?, isClear: Boolean = true): Uri {

    val uri = Uri.parse(url)
    if (isClear) {
        if (Fresco.getImagePipeline().isInBitmapMemoryCache(uri)) {
            Fresco.getImagePipeline().evictFromMemoryCache(uri)
        }

        if (Fresco.getImagePipeline().isInDiskCacheSync(uri)) {
            Fresco.getImagePipeline().evictFromDiskCache(uri)
        }
    }
    return uri
}