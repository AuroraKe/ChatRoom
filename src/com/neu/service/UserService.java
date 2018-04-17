package com.neu.service;

import java.sql.SQLException;
import java.util.List;

import com.neu.bean.User;
import com.neu.dao.UserDao;
import com.neu.dao.UserMapper;

public class UserService {
	private UserDao userDao = new UserMapper();
	
	public List<User> select(String username, String password) throws SQLException{
		List<User> users = userDao.select(username, password);
//		if(userDao.se(username, password) > 0)
//			return true;
		return users;
	}
	
	public User selectByName(String username) throws SQLException {
		if(username == null || username.trim() == "" || username.length() <= 0)
			return null;
		return userDao.selectByName(username);
	}
	
	public String selectNameById(int id) throws SQLException {
		return userDao.selectNameById(id);
	};
}
