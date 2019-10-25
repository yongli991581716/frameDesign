package com.frameDesign.commonlib.expand

import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor
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
 * @param runComplete 成功后执行回调
 */
fun SimpleDraweeView.displayBlurImage(
    url: String?,
    iteration: Int = 6,
    blurRadius: Int = 6.dp2px(),
    runComplete: (() -> Unit)? = null
) {
    try {
        val uri = Uri.parse(url)
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