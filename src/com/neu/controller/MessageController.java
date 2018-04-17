package com.neu.controller;

import java.sql.SQLException;

import com.neu.bean.Message;
import com.neu.service.MessageService;

public class MessageController {
	private MessageService messageService = new MessageService();
	
	public int insert(Message message){
		int num = 0;
		try {
			num = messageService.insert(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
}
