package com.neu.bean;

import java.io.Serializable;

public class Online implements Serializable{
	private static final long serialVersionUID = 2293012694518965462L;
	private int onlineId;
	private String ip; //�û���IP��ַ
	private int port; //�û�������Ϣ�Ķ˿ں�
	private int userId; //user���е�����
	public int getOnlineId() {
		return onlineId;
	}
	public void setOnlineId(int onlineId) {
		this.onlineId = onlineId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
}
