package com.zhstar.nbamanager.common;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class DateTool {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdfNoSplit = new SimpleDateFormat("yyyyMMdd");

    public static String getDateString(Date date) {
        return sdf.format(date);
    }

    public static String getDateStringNoSplit(Date date) {
        return sdfNoSplit.format(date);
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static Date getToday() {
        return getCalendar().getTime();
    }

    public static String getCurrentString() {
        return LocalDate.now().toString();
    }

    public static String getCurrentStringNoSplit() {
        return getDateStringNoSplit(getToday());
    }

    public static int getNowYear() {
        return getCalendar().get(Calendar.YEAR);
    }

    public static int getNowMonth() {
        return getCalendar().get(Calendar.MONTH) + 1;
    }

    public static String getNowMonthString() {
        return String.format("%02d", getNowMonth());
    }

    public static String getNDaysAfter(int n) {
        return LocalDate.now().plusDays(n).toString();
    }

    public static boolean isBefore(String date1, String date2) {
        return LocalDate.parse(date1).isBefore(LocalDate.parse(date2));
    }

    public static int getHour() {
        return LocalTime.now().getHour();
    }

    public static void main(String[] args) {
        //System.out.println(LocalTime.parse("20140304"));
    }
}
