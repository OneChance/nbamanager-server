package com.zhstar.nbamanager.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {
	private static Calendar calendar;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfNoSplit = new SimpleDateFormat("yyyyMMdd");

	public static String getDateString(Date date) {
		return sdf.format(date);
	}

	public static String getDateStringNoSplit(Date date) {
		return sdfNoSplit.format(date);
	}

	public static Calendar getCalendar() {
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}
		return calendar;
	}

	public static Date getToday() {
		return getCalendar().getTime();
	}

	public static String getCurrentString() {
		return getDateString(getToday());
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
}
