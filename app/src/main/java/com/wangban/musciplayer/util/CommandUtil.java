package com.wangban.musciplayer.util;

import java.util.Date;
import java.text.SimpleDateFormat;

public class CommandUtil {
	private static SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
	private static Date date;
	public static String getTime(long millisecond){
		date=new Date(millisecond);
		return sdf.format(date);
	}
}
