package com.neu.service;

import java.sql.SQLException;
import java.util.List;

import com.neu.bean.Online;
import com.neu.dao.OnlineDao;
import com.neu.dao.OnlineMapper;

public class OnlineService {
	private OnlineDao onlineDao = new OnlineMapper();
	
	public List<Online> selectAll() throws SQLException {
		return onlineDao.selectAll();
	}
	
	/**
	 * 查询所有在线用户的用户ID
	 * @return 用户ID组成的列表
	 * @throws SQLException
	 */
	public List<Integer> selectAllUid() throws SQLException {
		return onlineDao.selectAllUid();
	};
	
	public boolean selectByUid(int userId) throws SQLException {
		return onlineDao.selectByUid(userId);
	}
	
	public int insert(String ip, int port, int userId) throws SQLException{
		if(ip == null)
			throw new SQLException("ip不能为空！");
		return onlineDao.insert(ip, port, userId);
	}
	

	/**
	 * 根据用户的用户名执行DML
	 * @param username
	 * @return
	 * @throws SQLException 
	 */
	public boolean updateByUid(int user_id) throws SQLException{
		return onlineDao.updateByUid(user_id);
	}
}
