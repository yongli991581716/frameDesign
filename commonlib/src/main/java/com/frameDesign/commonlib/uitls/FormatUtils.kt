package com.frameDesign.commonlib.uitls

import android.text.TextUtils
import java.text.DecimalFormat

/**
 * Created by liyong on 2018/4/8.
 * 数字格式化工具
 */
object FormatUtils {

    fun viewCount(count: Int?): String? {
        return when {
            count == null -> "0"
            count < 0 -> "0"
            count < 10000 -> "$count"
            else -> "${ArithUtils.numberFormat2(ArithUtils.divide(count, 1000))}k"//保留一位小数
        }
    }

    /**
     * 格式化数字, 格式如下
     * 例:
     * 10 - 10
     * 100 -> 100
     * 1000 -> 1k
     * 10000 -> 10k
     */
    fun formatKilCount(count: Int): String {
        return when {
            count < 0 -> "0"
            count < 1000 -> "$count"
            else -> "${ArithUtils.numberFormat2(ArithUtils.divide(count, 1000))}k"//保留一位小数
        }
    }

    /**
     * 西方计数法, 并保留1位小数, 整数部分每三个数字添加插入一个逗号
     *
     * (如: 100000.456 --> 100,000.46)
     */
    fun formatXXX_X(count: Double): String {
        val format = DecimalFormat(",###.##")

        return format.format(count)
    }

    /**
     * 西方计数法, 并保留两位小数, 整数部分每三个数字添加插入一个逗号
     *
     * (如: 100000.456 --> 100,000.46)
     */
    fun formatXXX_XX(count: Double): String {
        val format = DecimalFormat(",###.##")

        return format.format(count)
    }

    fun formatXXX_00(count: Double): String {
        val format = DecimalFormat("#,##0.00")

        return format.format(count)
    }

    /**
     * 西方计数法(整数), 每三个数字添加插入一个逗号
     *
     * (如: 10000000 --> 10,000,000)
     */
    fun formatXXX(count: Long): String {
        val format = DecimalFormat(",###")

        return format.format(count)
    }

    /**
     * 格式化计数
     */
    fun formatCount(count: Int): String {
        return if (count > 100000) {
            "${formatXXX_XX(count / 1000.0)}k"
        } else {
            formatXXX(count.toLong())
        }
    }

    /**
     * 格式化计数
     */
    fun formatCount(count: Long): String {
        return if (count > 100000) {
            "${formatXXX_XX(count / 1000.0)}k"
        } else {
            formatXXX(count)
        }
    }

    /**
     * 格式化计数
     */
    fun formatCount_w(count: Long): String {
        return if (count > 10000) {
            "${formatXXX_X(count / 10000.0)}w"
        } else {
            formatXXX(count)
        }
    }

    /**
     * 首页格式化数字
     */
    fun formatCounrMain_W(count:Long):String{
        return if(count>10000){
            "${ArithUtils.numberFormat2(ArithUtils.divide(count.toInt(),10000))}万"
        }else {
            "$count"
        }
    }
    /**
     * 首页格式化数字- 大于10000  显示 xx.x k
     */
    fun formatCounrMain_subscript(count:Long):String{
        return if(count>10000){
            "${ArithUtils.numberFormat2(ArithUtils.divide(count.toInt(),1000))}k"
        }else {
            "$count"
        }
    }


    /**
     *分割钱
     */
    fun splitMoney(money: String?): ArrayList<String> {
        val newMoney = ArrayList<String>()

        if (money != null && money.indexOf(".") > 0) {
            val temp = money.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            newMoney[0] = temp[0]

            if (CollectionUtils.getSize(temp) > 1) {
                newMoney[1] = "." + temp[1]
            } else {
                newMoney[1] = ".00"
            }

        } else {
            newMoney[0] = money ?: "0"
            newMoney[1] = ".00"
        }

        return newMoney
    }

    /**
     *隐藏手机号
     */
    fun hideMobile(mobile: String?): String? {
        return if (mobile?.isNotEmpty() == true && mobile.length >= 11) {
            mobile.substring(0, 3) + "...." + mobile.substring(mobile.length - 4, mobile.length)
        } else mobile


    }

    /**
     * 验证是否是视频
     */
    fun isVideo(url: String?): Boolean {
        if (url != null) {
            //            *.avi *.rmvb *.rm *.asf *.divx *.mpg *.mpeg *.mpe *.wmv *.mp4 *.mkv *.vob
            val lower = url.toLowerCase()
            return (lower.endsWith(".mp4")
                    || lower.endsWith(".avi")
                    || lower.endsWith(".rmvb")
                    || lower.endsWith(".rm")
                    || lower.endsWith(".asf")
                    || lower.endsWith(".divx")
                    || lower.endsWith(".mpg")
                    || lower.endsWith(".mpeg")
                    || lower.endsWith(".mpe")
                    || lower.endsWith(".wmv")
                    || lower.endsWith(".mkv")
                    || lower.endsWith(".vob")
                    || lower.endsWith(".mov"))
        }

        return false
    }

    /**
     * 验证手机格式
     */
    fun isMobileNO(mobiles: String): Boolean {
        /*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        val telRegex = "[1][3456789]\\d{9}"// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return if (TextUtils.isEmpty(mobiles))
            false
        else
            mobiles.matches(telRegex.toRegex())
    }
}