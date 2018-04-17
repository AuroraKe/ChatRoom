package com.neu.bean;

import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = -3179070718444120310L;
	
	private int messageId; //消息Bean的唯一标识
	private int type; //消息的类型，取值都来自于MessageType
	private String time; //发送消息的时间
	private String username; //接收消息的人
	private String message; //要发送的消息
	private int status; //消息的状态，0表示未发送，1表示已发送
	private int user_id; //发送消息人的id
	private int file_id; //要发送文件的id
	
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getFile_id() {
		return file_id;
	}
	public void setFile_id(int file_id) {
		this.file_id = file_id;
	}
	
	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", type=" + type + ", time=" + time + ", username=" + username
				+ ", message=" + message + ", status=" + status + ", user_id=" + user_id + ", file_id=" + file_id + "]";
	}
	
}
