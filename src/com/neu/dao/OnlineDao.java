package com.neu.dao;

import java.sql.SQLException;
import java.util.List;

import com.neu.bean.Online;

public interface OnlineDao {
	
	/**
	 * @return �������ߵ��û�
	 */
	List<Online> selectAll() throws SQLException;
	
	/**
	 * ��ѯ���������û����û�ID
	 * @return
	 * @throws SQLException
	 */
	List<Integer> selectAllUid() throws SQLException;
	
	/**
	 * ֻҪ�н������˵���û����ߣ�����true
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	boolean selectByUid(int userId) throws SQLException;
	
	/**
	 * ��online����������ݣ���������ip��ַ�ͽ������ݵĶ˿ں�
	 * @param ip �û���IP��ַ
	 * @param port �û��Ķ˿ں�
	 * @param userId �û���id
	 * @return 
	 * @throws SQLException 
	 */
	int insert(String ip, int port, int userId) throws SQLException;
	
	/**
	 * ����һ����������һ������ֵ
	 * @param arg1
	 * @return
	 * @throws SQLException
	 */
	boolean updateByUid(int arg1) throws SQLException;
}