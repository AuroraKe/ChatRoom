package com.neu.out;

public enum Type {
	ONLINE, //表示用户上线
	
	CHAT, //聊天 
	
	SEND_FILE_REQ, //发送文件请求
	
	ACCEPET_FILE_REQ, //接受接收文件
	
	REFUSE_FILE_REQ, //拒绝接收文件
	
	OFFLINE, //离线
	
	EXIT; //退出 (0x00000040)
	
	private Type() {
	}
	private Type(int type) {
	}
}
