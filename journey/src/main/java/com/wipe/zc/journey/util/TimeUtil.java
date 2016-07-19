package com.wipe.zc.journey.util;

import java.util.Calendar;

/**
 * 时间工具包
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
     * @param calendar    用于判断的Calendar对象
     * @return            判断结果
     */
    public static boolean isToday(Calendar calendar) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && date
                == calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取格式化的时间
     *
     * @param calendar      进行格式化的Calendar对象
     * @param needSecond    格式是否需要秒单位
     * @return              格式化后时间  XX:XX:XX
     */
    public static String getFromatTime(Calendar calendar, boolean needSecond) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String min ;
        if(minute < 10){
            min = "0"+minute;
        }else{
            min = String.valueOf(minute);
        }
        int second = calendar.get(Calendar.SECOND);
        if (needSecond) {
            return hour + ":" + min + ":" + second;
        } else {
            return hour + ":" + min;
        }
    }

    /**
     * 获取格式化的日期
     *
     * @param calendar      进行格式化的Calendar对象
     * @param needYear      格式是否需要年单位
     * @return              格式化后时间 XXXX-XX-XX
     */
    public static String getFromatDate(Calendar calendar, boolean needYear) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        if (needYear) {
            return year + "-" + month + "-" + date;
        } else {
            return month + "-" + date;
        }
    }

    /**
     * 比较两个Calendar对象之间时间差
     * @param now       当前年
     * @param last      之前年
     * @return -1:显示年月时分  0：显示月时分   1：显示时分    2：不显示
     *
     */
    public static int compareCalendar10(Calendar now, Calendar last) {
        int year1 = now.get(Calendar.YEAR);
        int month1 = now.get(Calendar.MONTH) + 1;
        int date1 = now.get(Calendar.DAY_OF_MONTH);
        int hour1 = now.get(Calendar.HOUR_OF_DAY);
        int minute1 = now.get(Calendar.MINUTE);

        int year2 = last.get(Calendar.YEAR);
        int month2 = last.get(Calendar.MONTH) + 1;
        int date2 = last.get(Calendar.DAY_OF_MONTH);
        int hour2 = last.get(Calendar.HOUR_OF_DAY);
        int minute2 = last.get(Calendar.MINUTE);

        if (year1 > year2) {   //小于当前年，一年前通话
            return -1;
        } else {
            if (month1 > month2) {     //小于当前月，一月前通话
                return 0;
            } else {
                if (date1 > date2) {       //小于当前日，一天前通话
                    return 0;
                } else {
                    if (hour1 > hour2) {   //一小时前
                        return 1;
                    } else {
                        if (minute1 - minute2 > 10) {  //10分钟前
                            return 1;
                        } else {
                            return 2;
                        }
                    }
                }
            }
        }
    }


}
