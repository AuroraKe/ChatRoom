package com.neu.client;

public class Type {
	public static final int ONLINE = 0x00000001; //��ʾ�û�����
	
	public static final int CHAT = 0x00000002; //���� 
	
	public static final int SEND_FILE_REQ = 0x00000004; //�����ļ�����
	
	public static final int ACCEPET_FILE_REQ = 0x00000008; //���ܽ����ļ�
	
	public static final int REFUSE_FILE_REQ = 0x00000010; //�ܾ������ļ�
	
	public static final int OFFLINE = 0x00000020; //����
	
	public static final int EXIT = 0x00000040; //�˳� (0x00000040)
	
	public static final int TEST = 0x00000080; //�˳� (0x00000040)
}
