package com.zhstar.nbamanager.common;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTool {

    public static String getCurrentString() {
        return LocalDate.now().toString();
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

    }
}
