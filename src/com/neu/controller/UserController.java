package com.neu.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.neu.bean.User;
import com.neu.service.UserService;

public class UserController {
	private UserService userService = new UserService();
	
	/**
	 * @param username �û���
	 * @param password ����
	 * @return ����û�����������ڣ��ͷ���true
	 */
	public boolean login(String username, String password){
		boolean tag = false;
		try {
			List<User> users = userService.select(username, password);
			if(users.size() > 0){
				tag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}
	
	public User selectByName(String username){
		User user = null;
		try {
			user = userService.selectByName(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
		
	}
	
	public String selectNameById(int id){
		String username = null;
		try {
			username = userService.selectNameById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return username;
	};
	
	/**
	 * �����û�id��Ӧ���û�����ɵ��б�
	 * @param listId ���û�id��ɵ��б�
	 * @return
	 */
	public List<String> selectListNameById(List<Integer> listId){
		List<String> listName = new ArrayList<>();
		for(int i = 0; i < listId.size(); i++){
			listName.add(selectNameById(listId.get(i)));
		}
		return listName;
	}
	
	//��������¼���ݣ�����ת����Ӧ�Ľ���
//	public boolean clientLogin(JLabel lblNewLabel, JButton login, String name, String pwd){
//		boolean tag = false;
//		Connect connect = new Connect();
//		lblNewLabel.setText("������...");
//		//ʹ��¼��ťʧЧ
//		login.setEnabled(false);
//		if(connect.isConnect()){
//			User user = selectByName(name);
//			if(user != null){
//				int id = user.getId();
//				if(!onlineController.selectByUid(id)){
//					if(login(name, pwd)){
//						tag = true;
//					}else{
//						login.setEnabled(true);
//						lblNewLabel.setText("�û������������");
//						tag = false;
//					}
//				}else{
//					login.setEnabled(true);
//					lblNewLabel.setText("�û������ߣ�");
//				}
//			}else{
//				login.setEnabled(true);
//				lblNewLabel.setText("���û������ڣ�");
//			}
//		}else{
//			login.setEnabled(true);
//			lblNewLabel.setText("���ӷ�����ʧ��");
//			tag = false; 
//		}
//		return tag;
//	}
}
