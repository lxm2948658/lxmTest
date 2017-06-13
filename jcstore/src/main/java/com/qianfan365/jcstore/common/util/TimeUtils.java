package com.qianfan365.jcstore.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author pengbo
 */
public class TimeUtils {
    private static Log logger = LogFactory.getLog(TimeUtils.class);
//  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期格式化为+8时区时间
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatLocal(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }


    /**
     * 根据传入的格式字符格式化日期
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static Date format(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String dateStr = format.format(date);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return date;
        }
    }

    /**
     * 根据传入的格式字符格式化日期
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String formatAsString(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    /**
     * 根据传入的格式字符格式化日期
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date format(String dateStr, String formatStr) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(formatStr)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 当天的开始
     *
     * @param date
     * @return
     */
    public static Date formatDayBegin(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    /**
     * 当天的结束
     *
     * @param date
     * @return
     */
    public static Date formatDayEnd(Date date) {
        date = DateUtils.setHours(date, 23);
        date = DateUtils.setMinutes(date, 59);
        date = DateUtils.setSeconds(date, 59);
        return DateUtils.setMilliseconds(date, 999);
    }

    /**
     * 当天的结束(精确到秒)
     *
     * @param date
     * @return
     */
    public static Date formatDayEnd1(Date date) {
        date = DateUtils.setHours(date, 23);
        date = DateUtils.setMinutes(date, 59);
        return DateUtils.setSeconds(date, 59);
    }

    /**
     * 明天的开始
     *
     * @param date
     * @return
     */
    public static Date tommorow(Date date) {
        Date today = formatDayBegin(date);
        return DateUtils.addDays(today, 1);
    }


    /**
     * 判断当前时间规则 当前秒数 < maxseconds 当前秒数 > minseconds 当前分钟数 % minutes == 0
     *
     * @return
     */
    public static boolean isTimeOK(int maxseconds, Integer minseconds, int minutes) {
        Date date = new Date();
        long fragmentInSeconds = DateUtils.getFragmentInSeconds(date, Calendar.MINUTE);
        boolean ok = false;
        if (null == minseconds) {
            ok =
                    DateUtils.getFragmentInMinutes(date, Calendar.HOUR_OF_DAY) % minutes == 0
                            && fragmentInSeconds < maxseconds;
        } else {
            ok =
                    DateUtils.getFragmentInMinutes(date, Calendar.HOUR_OF_DAY) % minutes == 0
                            && fragmentInSeconds < maxseconds && fragmentInSeconds > minseconds;
        }
        return ok;
    }

    /**
     * 判断当前时间规则 当前秒数 < maxseconds 当前秒数 > minseconds 当前分钟数 % minutes == 0
     *
     * @return
     */
    public static boolean isTimeOK(int maxseconds, int minutes) {
        return isTimeOK(maxseconds, null, minutes);
    }

    /**
     * N 天前
     *
     * @param i
     * @return
     */
    public static Date nDaysBefore(int i) {
        return DateUtils.addDays(new Date(), -i);
    }

    /**
     * @param date1 开始日期
     * @param date2 结束日期
     * @return int
     * @throws ParseException
     */
    public static int getMonthSpace(Date date1, Date date2) {
        int result = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int yearSpace = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        result = yearSpace*12+c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        return  Math.abs(result);
    }
}
