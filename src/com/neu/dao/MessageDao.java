package com.neu.dao;

import java.sql.SQLException;

import com.neu.bean.Message;

public interface MessageDao {
	
	/**
	 * �����ݿ��в����¼��Ϣ��������Ϣ��
	 * @param message
	 * @return ��Ӱ�������
	 * @throws SQLException
	 */
	int insert(Message message) throws SQLException;
}
