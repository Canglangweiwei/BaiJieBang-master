package com.jaydenxiao.common.commonutils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 通用工具类
 * Created by LiuPeng on 2015/8/11.
 */
@SuppressWarnings("ALL")
public class DateTimeUtil {

    private static final String DATE_FORMAT_CLIENT = "yyyy年M月d日";
    private static final String DATETIME_FORMAT_CLIENT = "yyyy年M月d日 HH:mm";
    private static final String DATETIME_FORMAT_SERVER = "yyyy-MM-dd HH:mm:SS";

    /**
     * 格式化为客户端格式的日期
     *
     * @param dateText yyyy-MM-dd HH:mm:ss 格式的字符串
     * @return yyyy年M月d日格式的字符串
     */
    public static String getClientDate(String dateText) {
        return formatDateTime(DATETIME_FORMAT_SERVER, dateText, DATE_FORMAT_CLIENT);
    }

    /**
     * 格式化为客户端格式的日期
     *
     * @param dateText yyyy-MM-dd HH:mm:ss 格式的字符串
     * @return yyyy年M月d日 HH:mm格式的字符串
     */
    public static String getClientDatetime(String dateText) {
        return formatDateTime(DATETIME_FORMAT_SERVER, dateText, DATETIME_FORMAT_CLIENT);
    }

    /**
     * 按照格式转换日期字符串<br/>
     * 如果出现异常，则原样返回数据
     *
     * @param fromFormat 原格式
     * @param fromText   原日期字符串，必须符合原格式
     * @param toFormat   目标格式
     * @return 目标格式的日期字符串
     */
    private static String formatDateTime(String fromFormat, String fromText, String toFormat) {
        SimpleDateFormat fromSDF = new SimpleDateFormat(fromFormat);
        SimpleDateFormat toSDK = new SimpleDateFormat(toFormat);
        try {
            Date date = fromSDF.parse(fromText);
            return toSDK.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return fromText;
        }
    }

    public static String getClientDateFormat(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取下一月份的开始日期
     */
    public static String getNextMonthDatetime(String f) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        return year + f + (month + 1) + f + "01";
    }

    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    /**
     * 获取中文日期
     */
    public static String getZwDate() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "年" + mMonth + "月" + mDay + "日" + "星期" + mWay;
    }

    /**
     * 获取输入时间的时间段描述，如刚刚、N分钟前、N小时前等等<br/>
     * 1分钟内显示刚刚<br/>
     * 1小时内显示XX分钟前<br/>
     * 1天内显示XX小时前<br/>
     * 超过一天显示yyyy/MM/dd格式日期
     *
     * @param dateString 日期字符串的格式必须为yyyy-MM-dd HH:mm:ss
     * @return 日期描述，或原始字符串
     */
    public static String getTimeDescription(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(dateString);
            Date nowDate = new Date();
            long diff = (nowDate.getTime() - date.getTime()) / 1000;
            if (diff < 60) {
                return "刚刚";
            }
            if (diff < 3600) {
                return (diff / 60) + "分钟前";
            }
            if (diff < 24 * 3600) {
                return (diff / 3600) + "小时前";
            }
            SimpleDateFormat returnSdf = new SimpleDateFormat("yyyy-MM-dd");
            return returnSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    /**
     * 从给定的格式的字符串中获取时间是上午还是下午
     *
     * @param fromFormat 时间格式
     * @param fromString 时间字符串
     * @return 0：上午、1：下午,-1：异常
     */
    public static int getAMPMFromString(String fromFormat, String fromString) {
        if (TextUtils.isEmpty(fromFormat)) {
            return -1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
        Date date;
        try {
            date = sdf.parse(fromString);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        return date.getHours() > 12 ? 1 : 0;
    }

    private static String getAMPMText(String fromFormat, String fromString) {
        int ampm = getAMPMFromString(fromFormat, fromString);
        if (ampm == 0) {
            return "AM";
        } else if (ampm == 1) {
            return "PM";
        } else {
            return null;
        }
    }
}
