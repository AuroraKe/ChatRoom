package com.neu.controller;


import java.sql.SQLException;
import java.util.List;

import com.neu.bean.Online;
import com.neu.service.OnlineService;

public class OnlineController {
	private OnlineService onlineService = new OnlineService();
	private boolean tag = false;
	
	
	public List<Online> selectAll(){
		List<Online> onlines = null;
		try {
			onlines = onlineService.selectAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return onlines;
	}
	
	/**
	 * ��ѯ���������û����û�ID
	 * @return �û�ID��ɵ��б�
	 * @throws SQLException
	 */
	public List<Integer> selectAllUid() throws SQLException {
		return onlineService.selectAllUid();
	};
	
	
	/**
	 * �û�����
	 * ���û���id��ip�ͽ�����Ϣ�˿ںŷ�װ���������ݿ�
	 * @param ip
	 * @param port
	 * @param userId
	 * @return
	 */
	public synchronized boolean online(String ip, int port, int userId){
		int column = 0;
		try {
			column = onlineService.insert(ip, port, userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return column > 0;
	}
	
	public synchronized boolean selectByUid(int userId){
		tag = false;
		try {
			tag = onlineService.selectByUid(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}
	
	/**
	 * �û�����
	 * �����û����û���ɾ�����û�
	 * @param username �û���
	 * @return
	 */
	public synchronized boolean offLine(int user_id){
		tag = false;
		try {
			tag = onlineService.updateByUid(user_id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}
}
