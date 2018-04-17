package com.neu.service;

import java.sql.SQLException;

import com.neu.bean.Message;
import com.neu.dao.MessageDao;
import com.neu.dao.MessageMapper;

public class MessageService {
	private MessageDao messageDao = new MessageMapper();
	
	public int insert(Message message) throws SQLException{
		return message == null ? 0 : messageDao.insert(message);
	}
}
