package com.frameDesign.commonlib.views.internal


/**
 * @desc  定义progress行为
 * ..
 * @author liyong
 * @date 2018/10/17
 */
interface IProgress {

    /**
     * 显示进度对话框
     * @param msg String
     */
    fun showProgress(msg: String = "")

    /**
     * 隐藏进度对话框
     */
    fun hideProgress()

}