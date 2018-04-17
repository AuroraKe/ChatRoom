package com.neu.dao;

import java.sql.SQLException;
import java.util.List;

import com.neu.bean.Online;

public interface OnlineDao {
	
	/**
	 * @return 所有在线的用户
	 */
	List<Online> selectAll() throws SQLException;
	
	/**
	 * 查询所有在线用户的用户ID
	 * @return
	 * @throws SQLException
	 */
	List<Integer> selectAllUid() throws SQLException;
	
	/**
	 * 只要有结果，就说明用户在线，返回true
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	boolean selectByUid(int userId) throws SQLException;
	
	/**
	 * 向online表中添加数据，包括它的ip地址和接收数据的端口号
	 * @param ip 用户的IP地址
	 * @param port 用户的端口号
	 * @param userId 用户的id
	 * @return 
	 * @throws SQLException 
	 */
	int insert(String ip, int port, int userId) throws SQLException;
	
	/**
	 * 根据一个参数返回一个布尔值
	 * @param arg1
	 * @return
	 * @throws SQLException
	 */
	boolean updateByUid(int arg1) throws SQLException;
}