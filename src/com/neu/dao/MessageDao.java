package com.neu.dao;

import java.sql.SQLException;

import com.neu.bean.Message;

public interface MessageDao {
	
	/**
	 * 向数据库中插入登录信息和聊天信息等
	 * @param message
	 * @return 受影响的行数
	 * @throws SQLException
	 */
	int insert(Message message) throws SQLException;
}
