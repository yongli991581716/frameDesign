package com.frameDesign.baselib.view.adapter

import com.frameDesign.baselib.view.holder.BaseHolder

/**
 * @desc
 * @author liyong
 * @date 2018/10/17
 */
class FDLayoutAdapter(state: Int, layoutId: Int) : BaseAdapter<BaseHolder>(state, layoutId) {

    override fun doViewConvert(holder: BaseHolder) {}

    override fun doViewRecycle(holder: BaseHolder) {}
}