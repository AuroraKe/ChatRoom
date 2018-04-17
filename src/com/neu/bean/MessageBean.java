package com.neu.bean;

import java.io.Serializable;
import java.util.HashSet;

public class MessageBean implements Serializable{
	//�汾���кţ��������ͬ�İ汾���кţ�������໥����
	private static final long serialVersionUID = 2017092520514401L;
	
	private int type; //1��ʾ���ߣ�2��������
	private String time; //����û����߻�����Ϣ��ʱ��
	private String username; //�û���
	private String message; //Ҫ���͵���Ϣ
	/**
	 * ������ߵĿͻ����߷�����Ϣ��Ŀ��
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
	 * @param onlines ������ߵĿͻ����߷�����Ϣ��Ŀ��
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
	 * @param username ��Ϣ�ķ�����
	 * ���û�A���û�B������Ϣ��{@code username}�ʹ����û�A
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
