package com.neu.bean;

import java.io.Serializable;
import java.util.HashSet;

public class MessageBean implements Serializable{
	//版本序列号，如果有相同的版本序列号，则可以相互兼容
	private static final long serialVersionUID = 2017092520514401L;
	
	private int type; //1表示上线，2代表聊天
	private String time; //标记用户上线或发送信息的时间
	private String username; //用户名
	private String message; //要发送的消息
	/**
	 * 存放在线的客户或者发送消息的目标
	 */
	private HashSet<String> onlines;
	
	/**
	 * Record the properties of the file.
	 */
	private FileBean fileBean;
	
	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public HashSet<String> getOnlines() {
		return onlines;
	}

	/**
	 * @param onlines 存放在线的客户或者发送消息的目标
	 */
	public void setOnlines(HashSet<String> onlines) {
		this.onlines = onlines;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * @param username 信息的发起者
	 * 即用户A向用户B发送消息，{@code username}就代表用户A
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageBean [type=" + type + ", message=" + message + ", username=" + username
				+ "]";
	}
}
