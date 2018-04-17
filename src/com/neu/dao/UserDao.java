package com.neu.dao;

import java.sql.SQLException;
import java.util.List;

import com.neu.bean.User;

public interface UserDao {
	/**
	 * �����û�����ѯ�����û�
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	List<User> select(String username, String password) throws SQLException;
	
	/**
	 * �����û�����ѯ�����������Ϣ(�û���Ψһ���û��������ظ�)
	 * @param username
	 * @return һ��T���͵Ķ���
	 * @throws SQLException 
	 */
	User selectByName(String username) throws SQLException;
	
	/**
	 * ����id�����û���
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	String selectNameById(int id) throws SQLException;
}
