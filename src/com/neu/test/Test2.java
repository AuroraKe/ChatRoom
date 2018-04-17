package com.neu.test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.neu.util.SimpleDate;
import com.neu.util.UnitConversion;

public class Test2 {
	@Test
	public void test1(){
		System.out.println(Integer.MAX_VALUE);
		int i = (int) Math.pow(2, 31);
		System.out.println((long)Math.pow(2, 31));
		System.out.println(Long.MAX_VALUE);
		System.out.println((2046 >> 10)); //相当于2的10次方
		System.out.println(2<<21>>10>>10); 
		DecimalFormat df = new DecimalFormat("0.00");
		String string = df.format((float)(1579 >> 4));
		System.out.println(string);
	}
	@Test
	public void test2(){
		double percent = 31.0 / 100;
		NumberFormat nf = NumberFormat.getPercentInstance();
		//设置百分数精度，即保留两位小数
		nf.setMinimumFractionDigits(2);
		System.out.println(nf.format(percent));
		long g = 1l << 32;
		System.out.println(g / 1024 / 1024 /1024);
		System.out.println(Long.MAX_VALUE);
	}
	@Test
	public void test3(){
		int size = 2;
		if((size = size / 2) > 1){
			System.out.println(size);
		}else{
			System.out.println(size);
		}
		System.out.println(((long)2 << 32) * 100);
	}
	@Test
	public void test4(){
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			String ip = inetAddress.getHostAddress();
			System.out.println(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test5(){
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while(networkInterfaces.hasMoreElements()){
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				System.out.println(networkInterface.getName());
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while(inetAddresses.hasMoreElements()){
					InetAddress ip = inetAddresses.nextElement();
//					System.out.println(ip);
					if(ip != null && ip instanceof Inet4Address){
						System.out.println("本机的IP地址是：" + ip);
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test6(){
		InetAddress inetAddress = null;
		//获取所有的网络接口
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			//遍历所有网络接口
			while(networkInterfaces.hasMoreElements()){
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				//获取接口下的所有IP
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				//再次遍历所有的IP地址
				while(inetAddresses.hasMoreElements()){
					InetAddress address = inetAddresses.nextElement();
					if(!address.isLoopbackAddress()){
						if(address.isSiteLocalAddress()){
							System.out.println(address);
						}else{
//							inetAddress = address;
						}
					}
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
