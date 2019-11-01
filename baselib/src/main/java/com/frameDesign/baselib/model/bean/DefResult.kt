package com.frameDesign.baselib.model.bean

import com.frameDesign.baselib.model.bean.internal.ErrorBean
import com.frameDesign.commonlib.expand.fdToast


/**
 * 默认结果
 * @author liyong
 * @date 2018/10/17
 */
abstract class DefResult<T> : BaseResult<T>() {

    override fun doFinish(errorFinish: Boolean) {}

    override fun doFailure(eBean: ErrorBean) {
        fdToast(eBean.msg)
    }

}