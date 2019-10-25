package com.zqkh.commlibrary.utilslibrary.expand

import com.frameDesign.commonlib.uitls.FormatUtils.formatXXX_00


/**
 * 转换金额显示模式
 * @receiver Number
 * @return String
 */
fun Number.toMoneyStr(): String = formatXXX_00(this.toDouble())