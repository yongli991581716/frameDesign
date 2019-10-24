package com.frameDesign.commonlib.uitls;

import kotlin.jvm.JvmStatic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by liyong on 2018/4/8.
 * 日期工具
 */

public class DateUtils {
    public static final String DATEFORMAT_yyyy_MM_dd_HHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String DATEFORMAT_yyyy_MM_dd_HHmmss2 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATEFORMAT_yyyy_MM_dd_HHmmssSSS = "yyyy-MM-dd-HH-mm-ss-SSS";
    public static final String DATEFORMAT_yyyy_MM_dd_HHmm = "yyyy/MM/dd HH:mm";
    public static final String DATEFORMAT_yyyyMMdd_HHmm = "yyyyMMdd HH:mm";
    public static final String DATEFORMAT_yyyyzMMzddz = "yyyy年MM月dd日";
    public static final String DATEFORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String DATEFORMAT_yyyyMMdd = "yyyyMMdd";
    public static final String DATEFORMAT_yyyyMMdd2 = "yyyy.MM.dd";
    public static final String DATEFORMAT_MM_dd_HHmm = "MM-dd HH:mm";
    public static final String DATEFORMAT_MMdd = "MMdd";
    public static final String DATEFORMAT_yyyy = "yyyy";
    public static final String DATEFORMAT_MM = "MM";
    public static final String DATEFORMAT_dd = "dd";
    public static final String DATEFORMAT_HHmm = "HH:mm";
    public static final String DATEFORMAT_HHmmss = "HH:mm:ss";
    public static final String DATEFORMAT_MM_dd = "MM-dd";
    public static final String DATEFORMAT_MMz_ddz = "MM月dd日";


    public final static long MINUTE_1 = 60 * 1000;// 1分钟
    public final static long MINUTE_5 = 5 * MINUTE_1;// 5分钟
    public final static long HOUR_1 = 60 * MINUTE_1;// 1小时
    public final static long DAY_1 = 24 * HOUR_1;// 1天
    public final static long WEEK_1 = 7 * DAY_1; // 1周
    public final static long MONTH_1 = 31 * DAY_1;// 月
    public final static long YEAR_1 = 12 * MONTH_1;// 年

    /**
     * 格式时间间隔
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        long current = System.currentTimeMillis();

        long offset = current - time;

        if (offset < MINUTE_1) {
            return "刚刚";
        } else if (offset < HOUR_1) {
            return "1小时前";
        } else {
            Calendar cal = Calendar.getInstance();
            /* 获取当前的年的天数 */
            int cdy = cal.get(Calendar.DAY_OF_YEAR);
            int cYear = cal.get(Calendar.YEAR);

            cal.setTimeInMillis(time);
            /* 获取指定时间年的天数 */
            int tdy = cal.get(Calendar.DAY_OF_YEAR);
            int tYear = cal.get(Calendar.YEAR);

            // 判断是否同年
            boolean sameYear = tYear == cYear;

            if (sameYear) {
                if (cdy == tdy) {
                    /* 如相等, 表示在同一天, 返回: HH:mm */
                    return dateToString(cal.getTime(),
                            DATEFORMAT_HHmm);
                } else {
                    /* 表示同年, 但不同天, 返回: MM-dd */
                    return dateToString(cal.getTime(),
                            DATEFORMAT_MM_dd);
                }
            } else {
                /* 表示不同年, 返回 yyyy-MM-dd */
                return dateToString(cal.getTime(),
                        DATEFORMAT_yyyy_MM_dd);
            }
        }
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String dateToString(long date, String format) {
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatDate(String date, String format) {
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        String str = "";
        try {
            Date date2 = sdf.parse(date);
            str = sdf.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static long dateTolong(String str_date) {
        SimpleDateFormat sdf = getSimpleDateFormat(DATEFORMAT_yyyy_MM_dd_HHmmss);
        long time = 0;
        try {
            Date date = sdf.parse(str_date);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }

    public static long yyMMddToLong(String strDate) {
        try {
            Date dt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(strDate);
            return dt.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public static long dateTolong(String str_date, String pars) {
        SimpleDateFormat sdf = getSimpleDateFormat(str_date);
        long time = 0;
        try {
            Date date = sdf.parse(pars);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static long date2time(String formatDate, String format) {
        SimpleDateFormat sdf = getSimpleDateFormat(format);
        long time = 0;
        try {
            Date date = sdf.parse(formatDate);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static long date2timeYMD(String formatDate) {
        return date2time(formatDate, DATEFORMAT_yyyy_MM_dd);
    }

    public static long dateTolong(int year, int month, int day, int hour, int minute) {
        Calendar date = getCalendar();
        date.set(year, month, day, hour, minute);
        long time = date.getTimeInMillis();
        return time;
    }

    /**
     * 月份要减1
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static long dateTolong(int year, int month, int day) {
        Calendar date = getCalendar();
        date.set(year, month, day, 0, 0, 0);
        long time = date.getTimeInMillis();
        return time;
    }

    /**
     * 当前日期往后推得日期
     *
     * @param day
     * @return
     */
    public static long curPusherTolong(int day) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, day);
        return calendar.getTime().getTime();
    }

    /**
     * Function:比较两个时间的大小 格式是yyyy-MM-dd HH:mm
     *
     * @param date1 ：时间1
     * @param date2 ：时间2
     * @return true：表示date1<date2,反之则date1>date2
     */
    public static boolean compareDate(String date1, String date2) {

        SimpleDateFormat sdf = getSimpleDateFormat(DATEFORMAT_yyyy_MM_dd_HHmm);
        try {
            long time1 = sdf.parse(date1).getTime();
            long time2 = sdf.parse(date2).getTime();
            return time1 - time2 > 0 ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insuranceCompareDate(String date1, String date2, String format) {

        SimpleDateFormat sdf = getSimpleDateFormat(format);
        try {
            long time1 = sdf.parse(date1).getTime();
            long time2 = sdf.parse(date2).getTime();
            return time1 - time2 >= 0 ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean compareDate(String date1, String date2, String format) {

        SimpleDateFormat sdf = getSimpleDateFormat(format);
        try {
            long time1 = sdf.parse(date1).getTime();
            long time2 = sdf.parse(date2).getTime();
            return time1 - time2 > 0 ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String milliSecondToTime(long ms) {// 将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1;
        int mi = ss * 60;
        int hh = mi * 60;
        long hour = ms / hh;
        long minute = (ms - hour * hh) / mi;
        if (hour < 1) {
            return minute + "分钟";
        }
        return hour + "小时" + minute + "分钟";
    }

    /**
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        if (days < 1) {
            if (hours < 1) {
                return +minutes + "分" + seconds + "秒";
            } else if (hours >= 1) {
                return hours + "时" + minutes + "分" + seconds + "秒";
            }
        } else {
            return days + "天" + hours + "时" + minutes + "分" + seconds + "秒";
        }

        return days + "天" + hours + "时" + minutes + "分" + seconds + "秒";
    }

//	/**
//	 *
//	 * @param
//	 * @return 该毫秒数转换为 时 分 后的格式
//	 * @author fy.zhang
//	 */
//	public static String formatDuringTime(long mss) {
//
//		long days = mss / (1000 * 60 * 60 * 24);
//		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//
//		if (days < 1) {
//			if (hours < 1) {
//				return null;
//			} else if (hours >= 1) {
//				return "0" + "-" + hours;
//			}
//		} else {
//			return days + "-" + hours;
//		}
//
//		return days + "-" + hours;
//	}

    /**
     * 判断当前日期是星期几
     * <p>
     * 设置的需要判断的时间 //格式如2012-09-08
     *
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     * @deprecated 此方法以对特定数据做了适配
     */
    @Deprecated
    public static int getWeek(long time) {
        Calendar c = getCalendar();
        c.setTimeInMillis(time);

        int iWeek = c.get(Calendar.DAY_OF_WEEK);

        if (iWeek == 1) {
            return 7;
        }

        return iWeek - 1;
    }

    /**
     * 第几年
     *
     * @param time
     * @return
     */
    public static int getYear(long time) {
        Calendar c = getCalendar();
        c.setTimeInMillis(time);

        int year = c.get(Calendar.YEAR);

        return year;
    }

    /**
     * 第几月分
     *
     * @param time
     * @return
     */
    public static int getMonth(long time) {
        Calendar c = getCalendar();
        c.setTimeInMillis(time);

        int month = c.get(Calendar.MONTH);

        return month;
    }

    /**
     * 第几月天
     *
     * @param time
     * @return
     */
    public static int getDayOfMonth(long time) {
        Calendar c = getCalendar();
        c.setTimeInMillis(time);

        int day = c.get(Calendar.DAY_OF_MONTH);

        return day;
    }

    /**
     * 第几时
     *
     * @param time
     * @return
     */
    public static int getHour(long time) {
        Calendar c = getCalendar();
        c.setTimeInMillis(time);

        int hour = c.get(Calendar.HOUR_OF_DAY);

        return hour;
    }

    /**
     * 分
     *
     * @param time
     * @return
     */
    public static int getMinus(long time) {
        Calendar c = getCalendar();
        c.setTimeInMillis(time);

        int minus = c.get(Calendar.MINUTE);

        return minus;
    }

    /**
     * 获取指定格式的时间戳
     *
     * @param time
     * @return
     */
    public static long getUploadTime(String time) {
        time = time.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
        try {
            Date d = format.parse(time);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String convertDateMMdd(long time) {
        return convertDate(DATEFORMAT_MMdd, time);
    }

    public static String convertDateyyyy_MM_dd(long time) {
        return convertDate(DATEFORMAT_yyyy_MM_dd, time);
    }

    public static String convertDateyyyyzMMzddz(long time) {
        return convertDate(DATEFORMAT_yyyyzMMzddz, time);
    }

    public static String convertDateyyyyMMdd(long time) {
        return convertDate(DATEFORMAT_yyyyMMdd, time);
    }

    public static String convertDotDateyyyyMMdd(long time) {
        return convertDate(DATEFORMAT_yyyyMMdd2, time);
    }

    public static String convertDateyyyyMMddHHmmss(long time) {
        return convertDate(DATEFORMAT_yyyy_MM_dd_HHmmss, time);
    }

    public static String convertDateyyyyMMddHHmmssSSS(long time) {
        return convertDate(DATEFORMAT_yyyy_MM_dd_HHmmssSSS, time);
    }

    public static String convertDateyyyyMMdd_HHmm(long time) {
        return convertDate(DATEFORMAT_yyyyMMdd_HHmm, time);
    }

    public static String convertDateyyyyHHmm(long time) {
        return convertDate(DATEFORMAT_HHmm, time);
    }

    public static String convertDateyyyyMMddHHmm(long time) {
        return convertDate(DATEFORMAT_yyyy_MM_dd_HHmm, time);
    }

    public static String convertDateMMzddz(long time) {
        return convertDate(DATEFORMAT_MMz_ddz, time);
    }


    public static String convertDate(String format, long time) {

        if (time > 0) {
            SimpleDateFormat sdf = getSimpleDateFormat(format);
            String strDate = sdf.format(new Date(time));
            return strDate;
        }

        return "";
    }

    public static String getCurrentDateToString() {
        SimpleDateFormat sdf = getSimpleDateFormat(DATEFORMAT_yyyy_MM_dd);
        String strDate = sdf.format(new Date());
        return strDate;
    }

    /**
     * 得到今天的时间戳
     *
     * @return
     */
    public static long getToday() {
        GregorianCalendar curdar = new GregorianCalendar(Locale.getDefault());
        GregorianCalendar todayDar = new GregorianCalendar(curdar.get(GregorianCalendar.YEAR),
                curdar.get(GregorianCalendar.MONTH), curdar.get(GregorianCalendar.DAY_OF_MONTH));

//        Date dt = todayDar.getTime();
        long time = todayDar.getTimeInMillis();
        return time;
    }

    public static int getYear() {
        GregorianCalendar curdar = new GregorianCalendar(Locale.getDefault());
        return curdar.get(GregorianCalendar.YEAR);
    }

    public static int getMoth() {
        GregorianCalendar curdar = new GregorianCalendar(Locale.getDefault());
        return curdar.get(GregorianCalendar.MONTH) + 1;
    }

    public static int getDay() {
        GregorianCalendar curdar = new GregorianCalendar(Locale.getDefault());
        return curdar.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * 得到本周的时间戳
     *
     * @return
     */
    public static long getThisWeek() {
        GregorianCalendar curdar = new GregorianCalendar(Locale.getDefault());
        GregorianCalendar thisweekDar = new GregorianCalendar(curdar.get(GregorianCalendar.YEAR),
                curdar.get(GregorianCalendar.MONTH),
                curdar.get(GregorianCalendar.DAY_OF_MONTH) - (curdar.get(GregorianCalendar.DAY_OF_WEEK) - 2));

        Date dt = thisweekDar.getTime();
        long time = thisweekDar.getTimeInMillis();
        return time;
    }

    /**
     * 得到本月的时间戳
     *
     * @return
     */
    public static long getThisMonth() {
        GregorianCalendar curdar = new GregorianCalendar(Locale.getDefault());

        GregorianCalendar thisyearDar = new GregorianCalendar(curdar.get(GregorianCalendar.YEAR),
                curdar.get(GregorianCalendar.MONTH), 1);

        Date dt = thisyearDar.getTime();
        long time = thisyearDar.getTimeInMillis();
        return time;
    }

    /**
     * 得到本季度的时间戳
     *
     * @return 季度的开始时间 与 结束时间
     */
    public static long[] getThisQuater() {
        GregorianCalendar curdar = new GregorianCalendar(Locale.getDefault());
        GregorianCalendar quater1 = new GregorianCalendar(curdar.get(GregorianCalendar.YEAR),
                GregorianCalendar.JANUARY, 1);
        GregorianCalendar quater2 = new GregorianCalendar(curdar.get(GregorianCalendar.YEAR),
                GregorianCalendar.APRIL, 1);
        GregorianCalendar quater3 = new GregorianCalendar(curdar.get(GregorianCalendar.YEAR),
                GregorianCalendar.JULY, 1);
        GregorianCalendar quater4 = new GregorianCalendar(curdar.get(GregorianCalendar.YEAR),
                GregorianCalendar.OCTOBER, 1);
        GregorianCalendar nextYear = new GregorianCalendar(curdar.get(GregorianCalendar.YEAR) + 1,
                GregorianCalendar.JANUARY, 1);

        Date dt1 = nextYear.getTime();

        long time1 = quater1.getTimeInMillis();
        long time2 = quater2.getTimeInMillis();
        long time3 = quater3.getTimeInMillis();
        long time4 = quater4.getTimeInMillis();
        long time5 = nextYear.getTimeInMillis();

        long time = System.currentTimeMillis();
        if (time > time4) {
            return new long[]{time4, time5};
        } else if (time > time3) {
            return new long[]{time3, time4};
        } else if (time > time2) {
            return new long[]{time2, time3};
        } else {
            return new long[]{time1, time2};
        }

    }

    /**
     * 得到本年的时间戳
     *
     * @return
     */
    public static long getThisYear() {
        GregorianCalendar curdar = new GregorianCalendar(Locale.getDefault());

        GregorianCalendar thisyearDar = new GregorianCalendar(curdar.get(GregorianCalendar.YEAR),
                GregorianCalendar.JANUARY, 1);

        Date dt = thisyearDar.getTime();
        long time = thisyearDar.getTimeInMillis();
        return time;
    }

//    public static int getAgeByBirthday(Date birthday) {
//        Calendar cal = Calendar.getInstance();
//
//        if (cal.before(birthday)) {
//            throw new IllegalArgumentException(
//                    "The birthDay is before Now.It's unbelievable!");
//        }
//
//        int yearNow = cal.get(Calendar.YEAR);
//        int monthNow = cal.get(Calendar.MONTH) + 1;
//        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
//
//        cal.setTime(birthday);
//        int yearBirth = cal.get(Calendar.YEAR);
//        int monthBirth = cal.get(Calendar.MONTH) + 1;
//        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
//
//        int age = yearNow - yearBirth;
//
//        if (monthNow <= monthBirth) {
//            if (monthNow == monthBirth) {
//                // monthNow==monthBirth
//                if (dayOfMonthNow < dayOfMonthBirth) {
//                    age--;
//                }
//            } else {
//                // monthNow>monthBirth
//                age--;
//            }
//        }
//        return age;
//    }
//
//    /**
//     * 返回文字描述的日期
//     *
//     * @param time
//     * @return
//     */
//    public static String getTimeFormatText(long time) {
//        if (time <= 0) {
//            return null;
//        }
//
//        long diff = System.currentTimeMillis() - time;
//        long r = 0;
////		if (diff > YEAR_1) {
////			r = (diff / YEAR_1);
////			return r + "年前";
////		}
////		if (diff > MONTH_1) {
////			r = (diff / MONTH_1);
////			return r + "个月前";
////		}
//        if (diff > DAY_1) {
//            r = (diff / DAY_1);
//
//            if (r >= 5) {
//                return dateToString(time, DATEFORMAT_yyyy_MM_dd);
//            } else {
//                return r + "天前";
//            }
//
//        }
//        if (diff > HOUR_1) {
//            r = (diff / HOUR_1);
//            return r + "个小时前";
//        }
//        if (diff > MINUTE_1) {
//            r = (diff / MINUTE_1);
//            return r + "分钟前";
//        }
//        return "刚刚发布";
//    }

    //	public static String getRelativeTime(long time,long curtTime) {
//		if (time <= 0) {
//			return null;
//		}
//
//		long diff = curtTime - time;
//		long r = 0;
//		if (diff > YEAR_1) {
//			r = (diff / YEAR_1);
//			return r + "年前";
//		}
//		if (diff > MONTH_1) {
//			r = (diff / MONTH_1);
//			return r + "个月前";
//		}
//		if (diff > DAY_1) {
//			r = (diff / DAY_1);
//			return r + "天前";
//		}
//		if (diff > HOUR_1) {
//			r = (diff / HOUR_1);
//			return r + "个小时前";
//		}
//		if (diff > MINUTE_1) {
//			r = (diff / MINUTE_1);
//			return r + "分钟前";
//		}
//		return "刚刚";
//	}
//    public static String getTimeFormatText(long time) {
//        if (time <= 0) {
//            return null;
//        }
//
//        long diff = System.currentTimeMillis() - time;
//        long r = 0;
////		if (diff > YEAR_1) {
////			r = (diff / YEAR_1);
////			return r + "年前";
////		}
////		if (diff > MONTH_1) {
////			r = (diff / MONTH_1);
////			return r + "个月前";
////		}
//        if (diff > DAY_1) {
//            r = (diff / DAY_1);
//
//            if (r >= 5) {
//                return dateToString(time, DATEFORMAT_yyyy_MM_dd);
//            } else {
//                return r + "天前";
//            }
//
//        }
//        if (diff > HOUR_1) {
//            r = (diff / HOUR_1);
//            return r + "个小时前";
//        }
//        if (diff > MINUTE_1) {
//            r = (diff / MINUTE_1);
//            return r + "分钟前";
//        }
//        return "刚刚发布";
//    }
    public static void main(String args[]) {
        System.out.print(getRelativeTime(1530240480000l));
    }

    /**
     * 相对时间
     * <p>
     * 1.1.1  时间显示规则
     * 1、  时间间隔低于10分钟，则显示为“刚刚”；
     * 2、  时间间隔超过10分钟，未超过1个小时且未跨天，则显示多少分钟前，如59分钟前；
     * 3、  时间间隔超过1个小时且未跨天，则显示多少小时前，如1小时前；
     * 4、  时间间隔跨天且未跨年，显示月、日，MM-DD；
     * 5、  时间间隔跨天且跨年，则显示年、月、日，如2015-04-13。
     * 6、  以上规则从上到下，权重升高，即优先满足最后面的规则。
     *
     * @param time
     * @return
     */
    @JvmStatic
    public static String getRelativeTime(long time) {
        //long curtTime = getToday();
        if (time <= 0) {
            return null;
        }

        long diff = System.currentTimeMillis() - time;
        long r = 0;
        if (diff > YEAR_1) {
//            r = (diff / YEAR_1);
//            return r + "年前";
            return convertDateyyyy_MM_dd(time);
        } else if (diff > MONTH_1) {
//            r = (diff / MONTH_1);
//            return r + "个月前";
//            return convertDateyyyy_MM_dd(time);
            return convertDate(DATEFORMAT_MM_dd, time);
        } else if (diff > DAY_1) {
//            r = (diff / DAY_1);
//            return r + "天前";
            return convertDate(DATEFORMAT_MM_dd, time);
        } else if (diff > HOUR_1) {
            r = (diff / HOUR_1);
            return r + "小时前";
        } else if (diff > MINUTE_1 * 10) {
            r = (diff / MINUTE_1);
            return r + "分钟前";
        }

        return "刚刚";
    }

    /**
     * 聊天 相对时间
     * <p>
     * 1.1.1  时间显示规则
     * 1、  今天显示 时分秒
     * 2、  不是今天显示 年月日 时分秒
     *
     * @param time
     * @return
     */
    @JvmStatic
    public static String getChatTimeFormatText(long time) {
        long today = getToday();
        if (time >= today) {
            return convertDate(DATEFORMAT_HHmmss, time);
        }

        return convertDate(DATEFORMAT_yyyy_MM_dd_HHmmss2, time);
    }

    /**
     * 获取本周的开始时间
     *
     * @return
     * @author zhukai
     * @date 2016年7月28日 下午5:57:23
     */
    public static Long getWeekStart() {
        Calendar cal = getCalendar();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        return cal.getTimeInMillis();
    }

    /**
     * 获取本周的结束时间
     *
     * @return
     * @author zhukai
     * @date 2016年7月28日 下午6:06:15
     */
    public static Long getWeekEnd() {
        Calendar cal = getCalendar();
        cal.setTimeInMillis(getWeekStart());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 59);
        return cal.getTimeInMillis();
    }

    public static String secondToMinute(long time) {
        long minute = time / 60;
        long second = time % 60;

        String strMin = String.format("%02d", minute);
        String strSec = String.format("%02d", second);

        return strMin + ":" + strSec;
    }

    private static Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
//        c.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return c;
    }

    private static SimpleDateFormat getSimpleDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf;
    }

    /**
     * 检查两个时间戳, 是否在同一周
     *
     * @param ref 参考时间, 伊塔
     * @param tar 目标时间
     * @return -1 表示reference不是同一周, 且target指示的时间小于目标时间
     * 0 表示两个时间在同一周
     * 1 与-1对立, 不在同一周, 且大于目标时间
     */
    public static int checkInWeek(long ref, long tar) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ref);

        int refYear = cal.get(Calendar.YEAR);
        int refWeek = cal.get(Calendar.WEEK_OF_YEAR);

        cal.setTimeInMillis(tar);

        int tarYear = cal.get(Calendar.YEAR);
        int tarWeek = cal.get(Calendar.WEEK_OF_YEAR);

        if (tarYear == refYear) {
            if (tarWeek == refWeek) {
                return 0;
            } else {
                return tarWeek > refWeek ? 1 : -1;
            }
        } else {
            return tarYear > refYear ? 1 : -1;
        }
    }

    /**
     * 检查两个时间戳, 是否在同一月
     * <p>
     * 设计同{@link #checkInWeek(long, long)}
     *
     * @param ref
     * @param tar
     * @return
     */
    public static int checkInMonth(long ref, long tar) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ref);

        int refYear = cal.get(Calendar.YEAR);
        int refMonth = cal.get(Calendar.MONTH);

        cal.setTimeInMillis(tar);

        int tarYear = cal.get(Calendar.YEAR);
        int tarMonth = cal.get(Calendar.MONTH);

        if (tarYear == refYear) {
            if (tarMonth == refMonth) {
                return 0;
            } else {
                return tarMonth > refMonth ? 1 : -1;
            }
        } else {
            return tarYear > refYear ? 1 : -1;
        }
    }

    /**
     * 获取月份的中文描述
     *
     * @param month
     */
    public static String getMonthDesc(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return "一月";
            case Calendar.FEBRUARY:
                return "二月";
            case Calendar.MARCH:
                return "三月";

            case Calendar.APRIL:
                return "四月";
            case Calendar.MAY:
                return "五月";
            case Calendar.JUNE:
                return "六月";

            case Calendar.JULY:
                return "七月";
            case Calendar.AUGUST:
                return "八月";
            case Calendar.SEPTEMBER:
                return "九月";

            case Calendar.OCTOBER:
                return "十月";
            case Calendar.NOVEMBER:
                return "十一月";
            case Calendar.DECEMBER:
                return "十二月";
        }

        return "未知时间";
    }

    /**
     * 计算年龄
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int caculateAge(int year, int month, int day) {
        int curYear = getYear();//当前年
        int curMonth = getMoth();//当前月
        int curDay = getDay();//当前日

        long timeMill = dateTolong(year, month - 1, day);//当天
        long curTimeMill = (System.currentTimeMillis() / DAY_1) * DAY_1;//今天，去除时分秒

        if (timeMill > curTimeMill) {//错误的时间 0岁
            return 0;
        } else if (year == curYear) {//当年肯定 1岁
            return 1;
        }

        int age = curYear - year;
        if (curMonth > month || (curMonth == month && curDay > day)) {
            age++;
        }

        return age;
    }
}
