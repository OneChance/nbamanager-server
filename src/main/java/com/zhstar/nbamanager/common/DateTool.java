package com.zhstar.nbamanager.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {
	private static Calendar calendar;
	private static SimpleDateFormat sdf;
	
	public static String getDateString(Date date){
		if(sdf==null){
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		return sdf.format(date);
	}
	
	public static Calendar getCalendar(){
		if(calendar==null){
			calendar = Calendar.getInstance();
		}
		return calendar;
	}
	
	public static Date getToday(){
		return getCalendar().getTime();
	}
}
