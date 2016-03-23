package com.wipe.zc.journey.util;

import java.util.Calendar;

/**
 * Created by hp on 2016/3/23.
 */
public class TimeUtil {

    /**
     * 毫秒值转换为时间
     */
    public static Calendar longToTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
    }

    /**
     * 判断是否是当天
     *
     * @param calendar
     * @return
     */
    public static boolean isToday(Calendar calendar) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && date
                == calendar.get(Calendar.DAY_OF_MONTH)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取格式化的时间
     *
     * @param calendar
     * @param needSecond
     * @return
     */
    public static String getFromatTime(Calendar calendar, boolean needSecond) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if (needSecond) {
            return hour + ":" + minute + ":" + second;
        } else {
            return hour + ":" + minute;
        }
    }

    /**
     * 获取格式化的日期
     *
     * @param calendar
     * @param needYear
     * @return
     */
    public static String getFromatDate(Calendar calendar, boolean needYear) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        if (needYear) {
            return year + ":" + month + ":" + date;
        } else {
            return month + ":" + date;
        }
    }


}
