package com.neu.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.neu.bean.User;
import com.neu.service.UserService;

public class UserController {
	private UserService userService = new UserService();
	
	/**
	 * @param username 用户名
	 * @param password 密码
	 * @return 如果用户名和密码存在，就返回true
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
	 * 返回用户id对应的用户名组成的列表
	 * @param listId 由用户id组成的列表
	 * @return
	 */
	public List<String> selectListNameById(List<Integer> listId){
		List<String> listName = new ArrayList<>();
		for(int i = 0; i < listId.size(); i++){
			listName.add(selectNameById(listId.get(i)));
		}
		return listName;
	}
	
	//负责检验登录数据，并跳转到相应的界面
//	public boolean clientLogin(JLabel lblNewLabel, JButton login, String name, String pwd){
//		boolean tag = false;
//		Connect connect = new Connect();
//		lblNewLabel.setText("连接中...");
//		//使登录按钮失效
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
//						lblNewLabel.setText("用户名或密码错误！");
//						tag = false;
//					}
//				}else{
//					login.setEnabled(true);
//					lblNewLabel.setText("用户已在线！");
//				}
//			}else{
//				login.setEnabled(true);
//				lblNewLabel.setText("该用户不存在！");
//			}
//		}else{
//			login.setEnabled(true);
//			lblNewLabel.setText("连接服务器失败");
//			tag = false; 
//		}
//		return tag;
//	}
}
