package com.neu.out;

public enum Type {
	ONLINE, //��ʾ�û�����
	
	CHAT, //���� 
	
	SEND_FILE_REQ, //�����ļ�����
	
	ACCEPET_FILE_REQ, //���ܽ����ļ�
	
	REFUSE_FILE_REQ, //�ܾ������ļ�
	
	OFFLINE, //����
	
	EXIT; //�˳� (0x00000040)
	
	private Type() {
	}
	private Type(int type) {
	}
}
