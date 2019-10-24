package com.frameDesign.commonlib.uitls;


import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 运算工具
 *
 * @author liyong
 * @date 2017/12/21.
 */
public class ArithUtils {
    //默认运算精度
    private static final int DEF_DIV_SCALE = 2;

    /**
     * 加法运算。
     *
     * @param var 加数
     * @return 参数的和
     */
    public static BigDecimal add(BigDecimal... var) {
        BigDecimal bd = BigDecimal.ZERO;

        if (!CollectionUtils.INSTANCE.isEmpty(var)) {
            for (BigDecimal bigDecimal : var) {
                bigDecimal = bigDecimal == null ? BigDecimal.ZERO : bigDecimal;
                bd = bd.add(bigDecimal);
            }
        }

        return bd.setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal add(BigDecimal var, int var2) {
        return add(var, new BigDecimal(var2));
    }

    /**
     * 减法运算。
     *
     * @param var 第1个是被减数，其它是减数
     * @return 两个参数的差
     */
    public static BigDecimal subtract(BigDecimal... var) {
        if (!CollectionUtils.INSTANCE.isEmpty(var)) {
            BigDecimal bd = var[0];
            bd = bd == null ? BigDecimal.ZERO : bd;

            for (int i = 1; i < var.length; i++) {
                BigDecimal pms = var[i] == null ? BigDecimal.ZERO : var[i];
                bd = bd.subtract(pms);
            }

            return bd.setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    public static BigDecimal subtract(BigDecimal var, int var2) {
        return subtract(var, new BigDecimal(var2));
    }

    public static BigDecimal subtract(int var, int var2) {
        return subtract(new BigDecimal(var), new BigDecimal(var2));
    }

    /**
     * 乘法运算。
     *
     * @param var 乘数
     * @return 乘数的积
     */
    public static BigDecimal multiply(BigDecimal... var) {
        if (!CollectionUtils.INSTANCE.isEmpty(var)) {

            BigDecimal bd = new BigDecimal(1);

            for (int i = 0; i < var.length; i++) {
                if (var[i] == null || eqZero(bd)) {
                    return BigDecimal.ZERO;
                } else {
                    bd = bd.multiply(var[i]);
                }
            }
            return bd.setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    /**
     * 乘法运算。
     *
     * @param var 乘数
     * @return 乘数的积
     */
    public static BigDecimal multiply2(int type, BigDecimal... var) {
        if (!CollectionUtils.INSTANCE.isEmpty(var)) {

            BigDecimal bd = new BigDecimal(type);

            for (int i = 0; i < var.length; i++) {
                if (var[i] == null || eqZero(bd)) {
                    return BigDecimal.ZERO;
                } else {
                    bd = bd.multiply(var[i]);
                }
            }
            return bd.setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    public static BigDecimal multiply(BigDecimal var, long var2) {
        return multiply(var, new BigDecimal(var2));
    }

    public static BigDecimal multiply(BigDecimal var, double var2) {
        return multiply(var, new BigDecimal(var2));
    }

    public static BigDecimal multiplyAttDeposit(BigDecimal var, BigDecimal var2) {
        return var.multiply(var2);
    }

    /**
     * 除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param var 第1个是被除数，其它为除数
     * @return 参数的商
     */
    public static BigDecimal divide(BigDecimal... var) {
        return divide(BigDecimal.ROUND_HALF_UP, var);
    }

    /**
     * 除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，
     *
     * @param type 表示取整方式
     * @param var  第1个是被除数，其它为除数
     * @return 参数的商
     */

    public static BigDecimal divide(int type, BigDecimal... var) {
        if (!CollectionUtils.INSTANCE.isEmpty(var)) {
            BigDecimal bd = var[0];
            if (bd == null || eqZero(bd)) {
                return BigDecimal.ZERO;
            }

            for (int i = 1; i < var.length; i++) {
                if (var[i] == null || eqZero(var[i])) {
                    return BigDecimal.ZERO;
                } else {
                    bd = bd.divide(var[i], DEF_DIV_SCALE, type);
                }
            }

            return bd;
        }

        return BigDecimal.ZERO;
    }

    public static BigDecimal divide(BigDecimal var, int var2) {
        return divide(var, new BigDecimal(var2));
    }

    public static BigDecimal divide(int var, int var2) {
        return divide(new BigDecimal(var), new BigDecimal(var2));
    }

    public static BigDecimal divide(int var, float var2) {
        return divide(new BigDecimal(var), new BigDecimal(var2));
    }

    public static BigDecimal divide(BigDecimal var, BigDecimal var2) {
        if (var != null && var2 != null) {
            return divide(var, var2, 2);
        }
        return BigDecimal.ZERO;
    }

    /**
     * @param var
     * @param var2
     * @return
     */
    public static BigDecimal divide(BigDecimal var, BigDecimal var2, int scale) {
        if (var != null && var2 != null) {
            BigDecimal divide = var.divide(var2, scale, BigDecimal.ROUND_HALF_UP);
            return divide;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 两个数比较大小
     *
     * @param v1
     * @param v2
     * @return 0相等，大于0表示v1>v2,小于0表示v1<v2
     */
    public static int compareTo(BigDecimal v1, BigDecimal v2) {
        v1 = v1 == null ? BigDecimal.ZERO : v1;
        v2 = v2 == null ? BigDecimal.ZERO : v2;
        int result = v1.compareTo(v2);
        return result;
    }

    /**
     * 判断是否相等
     *
     * @param v1
     * @param v2
     * @return
     */
    public static boolean eq(BigDecimal v1, BigDecimal v2) {
        int result = compareTo(v1, v2);
        return result == 0;
    }

    /**
     * 是否等于0
     *
     * @param v1
     * @return
     */
    public static boolean eqZero(BigDecimal v1) {
        return eq(v1, BigDecimal.ZERO);
    }

    /**
     * 是否大于
     *
     * @param v1
     * @param v2
     * @return
     */
    public static boolean gt(BigDecimal v1, BigDecimal v2) {
        return compareTo(v1, v2) > 0;
    }

    /**
     * 是否大于0
     *
     * @param v1
     * @return
     */
    public static boolean gtZero(BigDecimal v1) {
        return gt(v1, BigDecimal.ZERO);
    }

    /**
     * v1是否大于等于v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static boolean gte(BigDecimal v1, BigDecimal v2) {
        return compareTo(v1, v2) >= 0;
    }

    /**
     * 是否大于等于0
     *
     * @param v1
     * @return
     */
    public static boolean gteZero(BigDecimal v1) {
        return gte(v1, BigDecimal.ZERO);
    }

    /**
     * v1是否小于v2
     *
     * @param v1
     * @return
     */
    public static boolean st(BigDecimal v1, BigDecimal v2) {
        return compareTo(v1, v2) < 0;
    }

    /**
     * 是否小于0
     *
     * @param v1
     * @return
     */
    public static boolean stZero(BigDecimal v1) {
        return st(v1, BigDecimal.ZERO);
    }


    /**
     * v1是否小于等于v2
     *
     * @param v1
     * @return
     */
    public static boolean ste(BigDecimal v1, BigDecimal v2) {
        return compareTo(v1, v2) <= 0;
    }

    /**
     * 是否小于等于0
     *
     * @param v1
     * @return
     */
    public static boolean steZero(BigDecimal v1) {
        return ste(v1, BigDecimal.ZERO);
    }


    /**
     * format, Description:
     *
     * @param number 需要格式化的数字
     * @return
     */
    public static String numberFormat(BigDecimal number) {
        if (number != null) {
            return formatDecimal(number, "0.00");
        }
        return "";
    }

    /**
     * 格式化字符
     *
     * @param str
     * @return
     */
    public static BigDecimal numberFormat(String str) {
        if (str == null || str.startsWith(".") || str.endsWith(".")) {
            return null;
        }
        try {
            BigDecimal big = new BigDecimal(formatDecimal(new BigDecimal(str), "0.00"));
            return big;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * format, Description:
     *
     * @param number 需要格式化的数字
     * @return
     */
    public static String numberFormat(Integer number) {
        if (number != null) {
            return formatDecimal(new BigDecimal(number), "0.00");
        }
        return "";
    }

    /**
     * 格式化为1位小数
     *
     * @param number
     * @return
     */
    public static String numberFormat2(BigDecimal number) {
        if (number != null) {
            return formatDecimal(number, "0.0");
        }

        return "";
    }

    /**
     * 格式化为整数
     *
     * @param number
     * @return
     */
    public static String numberFormatInt(BigDecimal number) {
        if (number != null) {
            return formatDecimal(number, "#");
        }

        return "";
    }

    /**
     * 格式化小数
     *
     * @param number
     * @param s      格式化样式
     * @return
     */
    private static String formatDecimal(BigDecimal number, String s) {
        DecimalFormat decimalFormat = new DecimalFormat(s);
        decimalFormat.setGroupingUsed(false);
        String format = decimalFormat.format(number);
        return format;
    }

//    /**
//     * 格式化万元，1万以上以万元显示，1万以下原始价格
//     *
//     * @param number
//     *            需要格式化的钱
//     * @return
//     */
//    public static String moneyWanFormat(BigDecimal number) {
//        if (number != null) {
//            if (ArithUtils.gte(number, new BigDecimal(10000))) {
//                return MyApplication.getInstance().getString(R.string.x_wan_money, ArithUtils.divide(number, 10000));
//            }
//            return MyApplication.getInstance().getString(R.string.x_money, ArithUtils.numberFormat(number));
//        }
//
//        return null;
//    }
//
//    /**
//     * 格式化元展示
//     *
//     * @param number
//     *            需要格式化的钱
//     * @return
//     */
//    public static String moneyYuanFormat(BigDecimal number) {
//        if (number != null) {
//            String my = numberFormat(number);
//            return MyApplication.getInstance().getString(R.string.x_money, my);
//        }
//
//        return null;
//    }
//
//    /**
//     * 格式化万元，1万以上以万元显示(针对卡车现车)
//     *
//     * @param number
//     *            需要格式化的钱
//     * @return
//     */
//    public static String carMoneyWanFormat(BigDecimal number) {
//        if (number != null) {
//                return MyApplication.getInstance().getString(R.string.x_wan_money, ArithUtils.divide(number, 10000));
//        }
//
//        return null;
//    }

}
