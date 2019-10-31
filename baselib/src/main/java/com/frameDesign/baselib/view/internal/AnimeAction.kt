package com.frameDesign.baselib.view.internal

/**
 * @desc
 * @author liyong
 * @date 2018/10/17
 */
internal interface AnimAction {

    var duration: Long

    val running: Boolean

    val startDelay: Long

//    fun end(anim: Anim)

    fun start()

    fun cancel()

    fun setListener(listener: AnimListener)

}