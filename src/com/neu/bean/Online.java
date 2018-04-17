package com.neu.bean;

import java.io.Serializable;

public class Online implements Serializable{
	private static final long serialVersionUID = 2293012694518965462L;
	private int onlineId;
	private String ip; //用户的IP地址
	private int port; //用户接受信息的端口号
	private int userId; //user表中的主键
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
