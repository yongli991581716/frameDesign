
package com.frameDesign.commonlib.views.assist;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * 限制小数位数
 *
 * @author liyong
 */
public class DecimalInputFilter implements InputFilter {

    private static final String DOT = ".";
    private static final String DECIMAL_STR = "-0123456789.";
    /**
     * 小数点后几位有效数字
     */
    private int mDigit = 2;

    private double mMaxValue = 100000000;

    // private static final String DEFAULT_DECIMAL = "00";

    public DecimalInputFilter(int digit) {
        mDigit = digit;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        try {
            if (end != 0) {
                // 首轮过滤非数字和点号的字符串
                for (int i = start; i < end; i++) {
                    if (!DECIMAL_STR.contains(source.toString().subSequence(i, i + 1))) {
                        return "";
                    }
                }

                if (mDigit == 0 && source.toString().endsWith(DOT)) {
                    return source.subSequence(0, source.length() - 1);
                }
                // 输入数字和点字符串时
                for (int i = start; i < end; i++) {
                    if (source.toString().equals(DOT)) {
                        // 包含小数点号时
                        if (dstart == 0 || dest.toString().contains(DOT)) {
                            // 在首位或者原字符串包含小数点号
                            return "";
                        }
                        // else if (dstart == dest.length()) {
                        // source = source.toString() + DEFAULT_DECIMAL;
                        // }
                    }
                    // else {
                    // if (dest.toString().length() == 0) {
                    // source = source.toString() + DOT + DEFAULT_DECIMAL;
                    // }
                    // }

                    // 将输入后的字符串拼接成完整字符串
                    String value = dest.toString().substring(0, dstart) + source.toString();
                    if (dest.length() != dstart) {
                        value += dest.toString().substring(dstart);
                    }


                    //去除当01 情况
                    if (validate(value))
                        return "";

                    // 比较输入大小不得超过mMaxValue
                    if (Double.valueOf(value) < mMaxValue) {
                        int index = value.indexOf(".");
                        // 1、不包含小数时；2、若包含小数，则小数位数不得超过2位
                        if (index == -1 || value.substring(index + 1).length() <= mDigit) {
                            continue;
                        }

                    }
                    return source.subSequence(start, i);
                }
            } else if (!dest.toString().equals("")) {
                // 删除字符时
                if (dstart == dend) {
                    // 无变化
                    return "";
                }
                // 将删除后的字符串拼接成完整字符串
                String value = dest.toString().substring(0, dstart) + source.toString();
                if (dest.length() != (dstart + 1)) {
                    value += dest.toString().substring(dstart + 1);
                }

                //去除当01 情况
                if (validate(value))
                    return dest.subSequence(dstart, dend);

                if (!TextUtils.isEmpty(value)) {
                    // 比较输入大小超过mMaxValue,则置为删除前的字符
                    // || value.indexOf(DOT) == 0
                    if (Double.valueOf(value) >= mMaxValue) {
                        return dest.subSequence(dstart, dend);
                    }
                    // else if (value.indexOf(DOT) == value.length() - 1) {
                    // source = DEFAULT_DECIMAL;
                    // }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return source;
    }

    /**
     * 检验00及以上开头或者01等开头
     *
     * @author liyong
     * @date 2016/12/2 11:04
     */
    private boolean validate(String value) {
        Pattern pattern = Pattern.compile("^0{2,}.*");
        return (value.startsWith(DOT) || value.startsWith("0") && Double.valueOf(value) >= 1) || pattern.matcher(value).matches();
    }

    /**
     * 控制editText的输入格式
     *
     * @param editText 控制的目标输入框
     * @param digit    控制输入框格式为几位小数
     */
    public static DecimalInputFilter filter(EditText editText, int digit) {
        InputFilter[] filters = editText.getFilters();
        int length = filters.length;

        DecimalInputFilter decimalInputFilter;
        if (length > 0 && filters[length - 1] instanceof DecimalInputFilter) {
            decimalInputFilter = new DecimalInputFilter(digit);
            filters[length - 1] = decimalInputFilter;
        } else {
            InputFilter[] inputFilters = new InputFilter[length + 1];
            for (int i = 0; i < length; i++) {
                inputFilters[i] = filters[i];
            }
            decimalInputFilter = new DecimalInputFilter(digit);
            inputFilters[length] = decimalInputFilter;
            editText.setFilters(inputFilters);
        }
        return decimalInputFilter;
    }

    /**
     * 控制editText的输入格式
     *
     * @param editText 控制的目标输入框
     * @param maxValue maxValue 最大值
     */
    public static void filterValue(EditText editText, double maxValue) {
        DecimalInputFilter decimalInputFilter = filter(editText, 2);
        decimalInputFilter.mMaxValue = maxValue;
    }

    public static void filter(EditText editText) {
        filter(editText, 2);
    }

    public static void filterInteger(EditText editText) {
        filter(editText, 0);
    }
}
