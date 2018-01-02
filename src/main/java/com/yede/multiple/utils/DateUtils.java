package com.yede.multiple.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ming on 2/17/17.
 */
public class DateUtils {

    public static boolean after(final Date date1, final Date date2) {
        return date1.after(date2);
    }

    public static boolean before(final Date date1, final Date date2) {
        return date1.before(date2);
    }

    public static Date plusYears(final Date date, final int year) {
        return new DateTime(date).plusYears(year).toDate();
    }

    public static Date plusMonths(final Date date, final int months) {
        return new DateTime(date).plusMonths(months).toDate();
    }

    public static Date plusWeeks(final Date date, final int weeks) {
        return new DateTime(date).plusWeeks(weeks).toDate();
    }

    public static Date plusDays(final Date date, final int days) {
        return new DateTime(date).plusDays(days).toDate();
    }

    public static Date plusHours(final Date date, final int hours) {
        return new DateTime(date).plusHours(hours).toDate();
    }

    public static Date plusMinutes(final Date date, final int minutes) {
        return new DateTime(date).plusMinutes(minutes).toDate();
    }

    public static Date plusSeconds(final Date date, final int seconds) {
        return new DateTime(date).plusSeconds(seconds).toDate();
    }

    public static Date plusMilliSeconds(final Date date, final int milliSeconds) {
        return new DateTime(date).plusMillis(milliSeconds).toDate();
    }

    public static Date minusYears(final Date date, final int years) {
        return new DateTime(date).minusYears(years).toDate();
    }

    public static Date minusMonths(final Date date, final int months) {
        return new DateTime(date).minusMonths(months).toDate();
    }

    public static Date minusWeeks(final Date date, final int weeks) {
        return new DateTime(date).minusWeeks(weeks).toDate();
    }

    public static Date minusDays(final Date date, final int days) {
        return new DateTime(date).minusDays(days).toDate();
    }

    public static Date minusHours(final Date date, final int hours) {
        return new DateTime(date).minusHours(hours).toDate();
    }

    public static Date minusMinutes(final Date date, final int minutes) {
        return new DateTime(date).minusMinutes(minutes).toDate();
    }

    public static Date minusSeconds(final Date date, final int seconds) {
        return new DateTime(date).minusSeconds(seconds).toDate();
    }

    /**
     * 得到格式化时间
     *
     * @param date
     * @return
     */
    public static String getStringDateShort(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

}
