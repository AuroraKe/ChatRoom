package com.neu.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDate {
	public static String currentTime(){
		//Сд��hh��12Сʱ�Ƶģ�HH��24Сʱ�Ƶ�
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	
}
