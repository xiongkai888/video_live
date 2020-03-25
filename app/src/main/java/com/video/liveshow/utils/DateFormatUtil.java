package com.video.liveshow.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by cxf on 2018/7/19.
 */

public class DateFormatUtil {

    private static SimpleDateFormat sFormat;

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

//
    public static boolean isNoSameDayOfMillis() {
        long ms2 = System.currentTimeMillis();
        long ms1 = SharedPreferencesUtil.getInstance().readTime();//上次弹框时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final long interval = ms1 - ms2;
        boolean isShow = (!(interval < MILLIS_IN_DAY && interval > -1L * MILLIS_IN_DAY && toDay(ms1) == toDay(ms2)) && hour >= 8);
        if (isShow){
            SharedPreferencesUtil.getInstance().saveTime(ms2);
        }
        return isShow;
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    static {
        sFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    }

    public static String getCurTimeString() {
        return sFormat.format(new Date());
    }

}
