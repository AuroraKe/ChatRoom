package com.neu.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 千古传奇
 * 获取系统当前时间
 */
public class SimpleDate {
	
	/**
	 * @return 系统当前时间
	 */
	public static String getCurrentTime(){
		//小写的hh是12小时制的，HH是24小时制的
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
	long t = 0;
	public static void timing(long millis){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				long object = System.currentTimeMillis();
//				object = this.t;
//				long startMillis = System.currentTimeMillis();
				long endMillis = System.currentTimeMillis() + millis;
				while(true){
					if(endMillis <= System.currentTimeMillis()){
						endMillis = endMillis + millis;
						System.out.println((System.currentTimeMillis() - object));
						object = System.currentTimeMillis();
					}
				}
			}
		};
		new Thread(runnable).start();
	}
	
	public static void main(String[] args) {
		SimpleDate.timing(1000);
	}
}
