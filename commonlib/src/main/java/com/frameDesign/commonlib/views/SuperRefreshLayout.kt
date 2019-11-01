package com.frameDesign.commonlib.views

import android.content.Context
import android.util.AttributeSet
import com.frameDesign.commonlib.R
import com.frameDesign.commonlib.expand.fdColor
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * 自定义刷新View
 *
 * @author liyong
 * @time 2018/10/23
 */
class SuperRefreshLayout(context: Context?, attrs: AttributeSet?) :
    SmartRefreshLayout(context, attrs) {

    init {
        val materialHeader = MaterialHeader(context, attrs)
        materialHeader.setColorSchemeColors(fdColor(R.color.colorPrimary))

        setRefreshHeader(materialHeader)

        setEnableLoadMore(false)
    }
}