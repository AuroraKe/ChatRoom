package com.neu.client;

public class Type {
	public static final int ONLINE = 0x00000001; //表示用户上线
	
	public static final int CHAT = 0x00000002; //聊天 
	
	public static final int SEND_FILE_REQ = 0x00000004; //发送文件请求
	
	public static final int ACCEPET_FILE_REQ = 0x00000008; //接受接收文件
	
	public static final int REFUSE_FILE_REQ = 0x00000010; //拒绝接收文件
	
	public static final int OFFLINE = 0x00000020; //离线
	
	public static final int EXIT = 0x00000040; //退出 (0x00000040)
	
	public static final int TEST = 0x00000080; //退出 (0x00000040)
}
