package com.neu.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author ǧ�Ŵ��� ��ȡ����IP��ַ
 */
public class LocalAddress {
	
	/**
	 * @return 
	 */
	public static InetAddress getLocalAddress() {
		InetAddress inetAddress = null;
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			// ������������ӿ�
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				// ��ȡ�ӿ��µ�����IP
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				// �ٴα������е�IP��ַ
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
			
			//���û�з���non-loop��ַ��ֻ������εķ���
			return InetAddress.getLocalHost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
