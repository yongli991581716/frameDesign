package com.frameDesign.commonlib.uitls

import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import org.json.JSONObject
import java.util.regex.Pattern

/**
 * 字符串工具
 * @author liyong
 * @date 2019-10-22.
 */
object StringUtils {
    fun getLength(string: String?): Int {
        return string?.length ?: 0

    }


    fun fromHtml(str: String): Spanned {
        return Html.fromHtml(str)
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    fun isEmpty(input: String?): Boolean {
        if (input == null || "" == input)
            return true

        for (i in 0 until input.length) {
            val c = input[i]
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false
            }
        }
        return true
    }

    /**
     * 插入字符
     * @param orgStr 原始字符
     * @param len 间隔距离
     * @param str 插入的字符
     * @return
     */
    fun insertString(orgStr: String?, len: Int, str: String): String? {
        if (orgStr != null && orgStr.length > len && len > 0) {
            val sb = StringBuffer()
            for (i in 0 until orgStr.length) {
                sb.append(orgStr[i])
                if (i < orgStr.length - 1 && (i + 1) % len == 0) {
                    sb.append(str)
                }
            }
            return sb.toString()
        }
        return orgStr
    }

    /**
     * Function:获取身份证生日
     *
     * @param idcard : 身份证号
     * @return
     */
    fun getIdcardBirthday(idcard: String?): String {
        return if (idcard.isNullOrEmpty()) {//510132198310240011
            ""
        } else {
            idcard!!.substring(6, 10) + "-" + idcard.substring(10, 12) + "-" + idcard.substring(12, 14)
        }
    }

    /**
     * Function:判断是否是url
     *
     * @param url
     * @return
     */
    fun isUrl(url: String): Boolean {

        val pattern = Pattern.compile("^(((https?|ftp|file)://)?)[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$")
        val matcher = pattern.matcher(url)
        return matcher.matches()

    }

    fun toString(`object`: Any?): String {
        return `object`?.toString() ?: ""

    }

    /**
     * 如果为空，就默认0
     *
     * @param str
     * @return
     */
    fun toInt(str: String): Int {
        return if (TextUtils.isEmpty(str)) 0 else Integer.parseInt(str)
    }

    /**
     * 隐藏银行卡号
     *
     * @param card
     * @return
     */
    fun getCardNum(card: String?): String? {
        if (getLength(card) > 3) {
            val sb = StringBuffer()
            sb.append(card!!.substring(0, 1))

            val length2 = card.length - 2
            for (i in 1 until length2) {
                sb.append('*')
            }

            sb.append(card.substring(length2))
            return sb.toString()
        }

        return card
        //        String newStr = card == null ? null : card.substring(0, 1) + "****************" + card.substring(card.length() - 2);
        //        return newStr;
    }

    /**
     * 隐藏手机号
     *
     * @param phone
     * @return
     */
    fun getPhoneNum(phone: String?): String? {
        if (getLength(phone) > 8) {
            val sb = StringBuffer()
            sb.append(phone!!.substring(0, 3))

            val length2 = phone.length - 4
            for (i in 3 until length2) {
                sb.append('*')
            }

            sb.append(phone.substring(length2))
            return sb.toString()
        }

        return phone
        //        String newStr = getLength(phone) < 11 ? phone : phone.substring(0, 3) + "****" + phone.substring(phone.length() - 5);
        //        return newStr;
    }

    /**
     * 用指定字符分隔格式化字符串
     * <br></br>（如电话号为1391235678 指定startIndex为3，step为4，separator为'-'经过此处理后的结果为 <br></br> 139-1234-5678）
     *
     * @param source     需要分隔的字符串
     * @param startIndex 开始分隔的索引号
     * @param step       步长
     * @param separator  指定的分隔符
     * @return 返回分隔格式化处理后的结果字符串
     */
    fun formatPhone(source: String?, startIndex: Int, step: Int, separator: Char): String? {
        if (!TextUtils.isEmpty(source) && source!!.length >= 11) {
            var times = 0
            val tmpBuilder = StringBuilder(source)
            var i = 0
            while (i < tmpBuilder.length) {
                if (i == startIndex + step * times + times) {//if(i == 3 || i == 8){
                    if (separator != tmpBuilder[i]) {
                        tmpBuilder.insert(i, separator)
                    }
                    times++
                } else {
                    if (separator == tmpBuilder[i]) {
                        tmpBuilder.deleteCharAt(i)
                        i = -1
                        times = 0
                    }
                }
                i++
            }
            return tmpBuilder.toString()
        }

        return source
    }


    /**
     * 拼接Url
     */
    fun joinUrl(params: Map<String, String>?, href: String?): String? {
        var href1 = href
        if (params?.isNotEmpty() == true) {
            return joinJsonUrl(JSONObject(params), href)
        }
        return href1
    }

    fun joinJsonUrl(params: JSONObject?, href: String?): String? {
        var newUrl = href

        if (params != null) {
            var newParams = "?"
            for (key in params.keys()) {
                if (newParams.length > 1) {
                    newParams += "&"
                }
                newParams += "$key=${params.get(key)}"
            }
            newUrl += newParams
        }

        return newUrl
    }

    /**
     * 统计某个字符出现的次数
     * @param str String
     * @param cr String
     */
    fun countString(str: String?, cr: String?): Int {
        if (str == null || cr == null) {
            return 0
        }

        val i = str.length - str.replace(cr, "").length
        return i/2
    }

    /**
     *
     * @Title: getExternalName
     *
     * @Description: Get the url extension
     *
     * @param @param
     * url @param @return @return String @throws
     */
    fun getExternalName(url: String): String {
        var ext = ""
        if (!TextUtils.isEmpty(url)) {
            val idx = url.lastIndexOf('.')
            if (idx > 0) {
                ext = url.substring(idx)
            }
        }

        return ext
    }
}
