package com.linmama.dinning.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jingkang on 2017/3/14
 */

public class TimeUtils {
    public static final SimpleDateFormat DATE_FROMAT = new SimpleDateFormat("yyyy-MM-dd ");
    public static final SimpleDateFormat TIME_FROMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATE_FROMAT_Default = new SimpleDateFormat("yyyy-MM-dd");
    // public static final SimpleDateFormat DATE_TIME_FROMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds
     *
     * @param dateFormat SimpleDateFormat
     * @return the string of current time with specified SimpleDateFormat
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static String getYesterdayTimeInString() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return DATE_FROMAT_Default.format(cal.getTime());
    }

    public static String getTomorowTimeInString() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return DATE_FROMAT_Default.format(cal.getTime());
    }

    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public static int[] getLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        int year =  c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        return new int[]{year, month};
    }
}
