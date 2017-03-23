package com.zhstar.nbamanager.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTool {
	
	private static SimpleDateFormat sdf;
	
	public static String getDateString(Date date){
		if(sdf==null){
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		return sdf.format(date);
	}
}
