package com.gmc.main.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date getLastDateOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

//	public static void main(String[] args) {
//		Date date = new Date();
//		System.out.println(date);
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		calendar.add(Calendar.DAY_OF_YEAR, 1);
//		calendar.add(Calendar.DAY_OF_WEEK, -1);
//		calendar.set(Calendar.HOUR_OF_DAY, 23);
//		calendar.set(Calendar.MINUTE, 59);
//		calendar.set(Calendar.SECOND, 59);
//		calendar.set(Calendar.MILLISECOND, 999);
//		System.out.println(calendar.getTime());
//	}
}
