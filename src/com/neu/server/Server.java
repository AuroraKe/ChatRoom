package com.neu.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.neu.bean.FileBean;
import com.neu.bean.MessageBean;
import com.neu.util.MessageType;

public class Server {
	private static ServerSocket serverSocket;
	
	/**
	 * 存放在线的客户的用户名和通信地址
	 */
	private static HashMap<String, Socket> onlines;
	
	/**
	 * 服务端接收信息的端口号
	 */
	private static int port = 8888;
	
	/**
	 * 把上线用户放进set中，以便客户端读取并显示在用户列表中
	 */
	private static HashSet<String> onlineSet;
	
	static{
		try {
			serverSocket = new ServerSocket(port);
			onlines = new HashMap<>();
			onlineSet = new HashSet<>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class ServerThread implements Runnable{
		private Socket socket;
		private ObjectInputStream ois;
		MessageBean mb;
		
		public ServerThread(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try {
				//不断的从客户端接收数据
				while(true){
					if(!socket.isConnected() || socket.isInputShutdown()
							|| socket.isClosed()){
						System.out.println("用户意外退出...");
						return;
					}
					ois = new ObjectInputStream(socket.getInputStream());
					mb = (MessageBean) ois.readObject(); //从对象输入流中读取一个对象
					switch (mb.getType()) {
					//-1表示下线
					case MessageType.OFFLINE:{
						//将请求返回到客户端，使客户端退出
						MessageBean serverBean = new MessageBean();
						serverBean.setType(MessageType.OFFLINE);
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(serverBean);
						oos.flush();
						//通知所有客户端，并修改他们的在线列表
						MessageBean serverBean2 = new MessageBean();
						onlines.remove(mb.getUsername()); //把在线用户的socket移除
						/*
						 * @error 这里出现过bug，这里不能用HashSet的addAll()方法，这里是要移除
						 * 指定的用户；至于清楚指定的用户有很多方法
						 * */
						onlineSet.clear();
						onlineSet.addAll(onlines.keySet()); //更新在线用户
						serverBean2.setType(MessageType.ONLINE);
						serverBean2.setTime(mb.getTime());
						serverBean2.setUsername(mb.getUsername());
						serverBean2.setMessage(mb.getUsername()+"下线了");
						/*
						 * @error 由于onlines.keySet()生成的是set集合，所以还需要通过声明
						 * 一个HashSet来转一下
						 * */
						serverBean2.setOnlines(onlineSet); //设置在线用户
						sendAll(serverBean2);
						return;
					}
					//1表示上线
					case MessageType.ONLINE:{
						MessageBean serverBean = new MessageBean();
						onlines.put(mb.getUsername(), socket);
						onlineSet.addAll(onlines.keySet());
						serverBean.setType(MessageType.ONLINE);
						serverBean.setTime(mb.getTime());
						serverBean.setUsername(mb.getUsername());
						serverBean.setOnlines(onlineSet);
						serverBean.setMessage(mb.getUsername()+"上线了");
						sendAll(serverBean);
						break;
					}
					//MessageType.CHAT表示聊天
					case MessageType.CHAT:{
						MessageBean serverBean = new MessageBean();
						serverBean.setType(MessageType.CHAT);
						serverBean.setTime(mb.getTime());
						serverBean.setUsername(mb.getUsername());
						serverBean.setMessage(mb.getMessage());
						serverBean.setOnlines(mb.getOnlines());
						sendMessage(mb);
						break;
					}
					/**
					 * 发送文件的请求
					 */
					case MessageType.SEND_FILE_REQ:{
						MessageBean messageBean = new MessageBean();
						messageBean.setType(MessageType.SEND_FILE_REQ);
						messageBean.setTime(mb.getTime());
						messageBean.setUsername(mb.getUsername()); //who请求发送文件
						messageBean.setMessage(mb.getMessage());
						//将发送文件的请求发送给这些人
						messageBean.setOnlines(mb.getOnlines());
						
						//设置要发送的文件的属性
						FileBean fileBean = new FileBean();
						fileBean.setFileName(mb.getFileBean().getFileName());
						fileBean.setFileSize(mb.getFileBean().getFileSize());
						fileBean.setFilePath(mb.getFileBean().getFilePath());
						messageBean.setFileBean(fileBean);
						sendMessage(messageBean);
						break;
					}
					
					/**
					 * 发送文件
					 */
					case MessageType.ACCEPET_FILE_REQ:{
						MessageBean messageBean = new MessageBean();
						messageBean.setType(MessageType.ACCEPET_FILE_REQ);
						messageBean.setTime(mb.getTime());
						messageBean.setUsername(mb.getUsername()); //谁接受了请求
						messageBean.setOnlines(mb.getOnlines()); //要发送的人
						messageBean.setMessage(mb.getMessage());
						
						//设置要发送文件的属性（包括要发送的地址）
						FileBean fileBean = new FileBean();
						fileBean.setFileName(mb.getFileBean().getFileName());
						fileBean.setFileSize(mb.getFileBean().getFileSize());
						fileBean.setIp(mb.getFileBean().getIp());
						fileBean.setPort(mb.getFileBean().getPort());
						fileBean.setFilePath(mb.getFileBean().getFilePath());
						messageBean.setFileBean(fileBean);
						sendMessage(messageBean);
						break;
					}
					
					case MessageType.REFUSE_FILE_REQ:{
						MessageBean messageBean = new MessageBean();
						messageBean.setType(MessageType.REFUSE_FILE_REQ);
						messageBean.setTime(mb.getTime());
						messageBean.setUsername(mb.getUsername());
						messageBean.setMessage(mb.getMessage());
						messageBean.setOnlines(mb.getOnlines());
						sendMessage(messageBean);
						break;
					}
					
					default:
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				close();
			}
		}
		
		//向用户发送信息
		public void sendMessage(MessageBean messageBean) {
			HashSet<String> h = messageBean.getOnlines(); //获取目标客户（即列表中被选中的客户）
			Iterator<String> i = h.iterator(); //对选中的客户进行迭代
			while(i.hasNext()){
				String username = i.next(); //获取用户名
				if(onlineSet.contains(username)){
					Socket s = onlines.get(username);
					try {
						//如果s.getOutputStream()为空，则表明客户端意外退出
						//此时需要通知所有人，从列表中移除，并把所有人的列表信息更新。
						if(s.isClosed()){
							messageBean.setType(MessageType.ONLINE);
							onlines.remove(username); //去除这个人的在线信息
							onlineSet.clear(); //先清空，再添加
							onlineSet.addAll(onlines.keySet()); //更新在线用户
							messageBean.setOnlines(onlineSet);
							messageBean.setTime(mb.getTime());
							messageBean.setUsername(username);
							messageBean.setMessage(username + "下线了");
							sendAll(messageBean);
							return;
						}
						ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(messageBean);
						oos.flush();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "服务器连接异常", 
								"服务器提示", JOptionPane.ERROR_MESSAGE);
						System.out.println("服务器发送消息失败...");
						e.printStackTrace();
					}
				}
			}
		}
		
		//向所有在线用户发送消息
		public void sendAll(MessageBean mb) {
			//读取所有在线用户的通信地址socket，然后逐一的向他们发送封装好的信息
			//（主要包括好友上线消息和列表更新）
			Collection<Socket> s = onlines.values();
			Iterator<Socket> i = s.iterator(); //对所有在线的用户进行迭代
			//这里的ObjectOutputStream不能关闭，为什么？
			ObjectOutputStream oos;
			try {
				while(i.hasNext()){
					oos = new ObjectOutputStream(i.next().getOutputStream());
					oos.writeObject(mb);
					oos.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//关闭客户端、对象输入流
		private void close(){
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void start(){
		try {
			while(true){
				Socket socket = serverSocket.accept();
				ServerThread serverThread = new ServerThread(socket);
				new Thread(serverThread).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		new Server().start();
	}
}
