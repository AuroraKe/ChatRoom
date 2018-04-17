package com.neu.dao;

import java.sql.SQLException;
import java.util.List;

import com.neu.bean.User;

public interface UserDao {
	/**
	 * 根据用户名查询所有用户
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	List<User> select(String username, String password) throws SQLException;
	
	/**
	 * 根据用户名查询对象的所有信息(用户名唯一，用户名不能重复)
	 * @param username
	 * @return 一个T类型的对象
	 * @throws SQLException 
	 */
	User selectByName(String username) throws SQLException;
	
	/**
	 * 根据id查找用户名
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	String selectNameById(int id) throws SQLException;
}
