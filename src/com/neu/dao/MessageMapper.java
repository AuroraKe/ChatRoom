package com.neu.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.neu.bean.Message;
import com.neu.dao.query.BasicQuery;
import com.neu.dao.query.PetrichorDataSource;
import com.neu.dao.query.Query;

public class MessageMapper implements MessageDao{
	private DataSource dataSource = new PetrichorDataSource();
	private Query query = new BasicQuery(dataSource);
	
	@Override
	public int insert(Message message) throws SQLException {
		String sql = "insert into message(type, time, username, message, "
				+ "status, user_id, file_id) values(?, ?, ?, ?, ?, ?, ?)";
		int num = query.update(sql, message.getType(), message.getTime(), message.getUsername(), 
				message.getMessage(), message.getStatus(), message.getUser_id(), message.getFile_id());
		return num;
	}

}
