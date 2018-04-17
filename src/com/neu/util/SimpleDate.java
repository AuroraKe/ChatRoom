package com.neu.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDate {
	public static String currentTime(){
		//小写的hh是12小时制的，HH是24小时制的
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	
}
