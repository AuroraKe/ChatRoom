package com.neu.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.neu.bean.Online;
import com.neu.dao.handler.BeanListHandler;
import com.neu.dao.query.BasicQuery;
import com.neu.dao.query.PetrichorDataSource;
import com.neu.dao.query.Query;

public class OnlineMapper implements OnlineDao{
	private DataSource dataSource = new PetrichorDataSource();
	private Query query = new BasicQuery(dataSource);
	private String sql;
	
	@Override
	public List<Online> selectAll() throws SQLException {
		sql = "select online_id onlineId, ip, port, user_id userId from online";
		return query.queryList(sql, new BeanListHandler<>(Online.class));
	}
	
	@Override
	public List<Integer> selectAllUid() throws SQLException {
		sql = "select user_id from online";
		return query.queryForList(sql, Integer.class);
	}

	@Override
	public boolean selectByUid(int userId) throws SQLException {
		sql = "select count(*) from online where user_id = ?";
		Long value = query.queryForValue(sql, Long.class, userId);
		return (value > 0);
	}
	
	public int insert(String ip, int port, int userId) throws SQLException{
		sql = "insert into online(ip, port, user_id) values(?, ?, ?)";
		int column = query.update(sql, ip, port, userId);
		return column;
	}

	@Override
	public boolean updateByUid(int arg1) throws SQLException {
		sql = "delete from online where user_id = ?";
		int value = query.update(sql, arg1);
		return value > 0;
	}

}
