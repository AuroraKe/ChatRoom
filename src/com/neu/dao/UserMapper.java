package com.neu.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.neu.bean.User;
import com.neu.dao.handler.BeanHandler;
import com.neu.dao.handler.BeanListHandler;
import com.neu.dao.query.BasicQuery;
import com.neu.dao.query.PetrichorDataSource;
import com.neu.dao.query.Query;

public class UserMapper implements UserDao{
	private static DataSource dataSource = new PetrichorDataSource();
	private Query query = new BasicQuery(dataSource);
	
	private String sql;
	/**
	 * �����û����������ѯ�û��Ƿ���ڣ������򷵻ظ��û�����Ϣ
	 * @param username �û���
	 * @param password ����
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<User> select(String username, String password) throws SQLException{
		sql = "select * from user where username = ? and password = ?";
		List<User> users = query.queryList(sql, new BeanListHandler<>(User.class), username, password);
		return users;
	}

	/**
	 * �����û�����ѯ�û���Ϣ
	 */
	@Override
	public User selectByName(String username) throws SQLException {
		sql = "select * from user where username = ?";
		return query.query(sql, new BeanHandler<>(User.class), username);
	}

	@Override
	public String selectNameById(int id) throws SQLException {
		sql = "select username from user where id = ?";
		return query.queryForValue(sql, String.class, id);
	}
}