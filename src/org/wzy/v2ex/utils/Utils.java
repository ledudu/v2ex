package org.wzy.v2ex.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: zeyiwu
 * Date: 13-8-14
 * Time: 上午12:06
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public static String getTime(long times) {
        long currentTimes = System.currentTimeMillis();
        long detla = currentTimes / 1000 - times;
        long second = 1;
        long minute = 60;
        long hour = 60 * minute;
        long day = 24 * hour;
        long month = 30 * day;
        long year = 12 * month;

        if (detla < second) {
            return "刚刚";
        } else if (detla < minute) {
            return String.format("%d秒前", detla);
        } else if (detla < hour) {
            return String.format("%d分钟前", detla/minute);
        } else if (detla < day) {
            return String.format("%d小时前", detla/hour);
        } else if (detla < month) {
            return String.format("%d天前", detla/day);
        } else if (detla < year) {
            return String.format("%d月前", detla/month);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
            Date date = new Date(times * 1000);
            return simpleDateFormat.format(date);
        }
    }
}
