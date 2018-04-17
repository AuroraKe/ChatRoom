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
	 * 查询所有在线用户的用户ID
	 * @return 用户ID组成的列表
	 * @throws SQLException
	 */
	public List<Integer> selectAllUid() throws SQLException {
		return onlineService.selectAllUid();
	};
	
	
	/**
	 * 用户上线
	 * 将用户的id、ip和接受信息端口号封装，存入数据库
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
	 * 用户下线
	 * 根据用户的用户名删除该用户
	 * @param username 用户名
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
