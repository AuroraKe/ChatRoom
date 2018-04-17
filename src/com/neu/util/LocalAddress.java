package com.neu.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author 千古传奇 获取本地IP地址
 */
public class LocalAddress {
	
	/**
	 * @return 
	 */
	public static InetAddress getLocalAddress() {
		InetAddress inetAddress = null;
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			// 遍历所有网络接口
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				// 获取接口下的所有IP
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				// 再次遍历所有的IP地址
				while (inetAddresses.hasMoreElements()) {
					InetAddress address = inetAddresses.nextElement();
					if (!address.isLoopbackAddress()) {
						if (address.isSiteLocalAddress()) {
							return address;
						} else {
							 inetAddress = address;
						}
					}
				}
			}
			if(inetAddress != null)
				return inetAddress;
			
			//如果没有发现non-loop地址，只能用最次的方案
			return InetAddress.getLocalHost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
