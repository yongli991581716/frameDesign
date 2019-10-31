package com.frameDesign.baselib.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.frameDesign.baselib.view.holder.BaseHolder

/**
 * @desc
 * @author liyong
 * @date 2018/10/17
 */
class FDViewAdapter(state: Int, val view: View) : BaseAdapter<BaseHolder>(state) {

    override fun doCreateView(layoutId: Int, root: ViewGroup, inflater: LayoutInflater): View = view

    override fun doViewConvert(holder: BaseHolder) {}

    override fun doViewRecycle(holder: BaseHolder) {}

}
