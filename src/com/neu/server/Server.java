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
	 * ������ߵĿͻ����û�����ͨ�ŵ�ַ
	 */
	private static HashMap<String, Socket> onlines;
	
	/**
	 * ����˽�����Ϣ�Ķ˿ں�
	 */
	private static int port = 8888;
	
	/**
	 * �������û��Ž�set�У��Ա�ͻ��˶�ȡ����ʾ���û��б���
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
				//���ϵĴӿͻ��˽�������
				while(true){
					if(!socket.isConnected() || socket.isInputShutdown()
							|| socket.isClosed()){
						System.out.println("�û������˳�...");
						return;
					}
					ois = new ObjectInputStream(socket.getInputStream());
					mb = (MessageBean) ois.readObject(); //�Ӷ����������ж�ȡһ������
					switch (mb.getType()) {
					//-1��ʾ����
					case MessageType.OFFLINE:{
						//�����󷵻ص��ͻ��ˣ�ʹ�ͻ����˳�
						MessageBean serverBean = new MessageBean();
						serverBean.setType(MessageType.OFFLINE);
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(serverBean);
						oos.flush();
						//֪ͨ���пͻ��ˣ����޸����ǵ������б�
						MessageBean serverBean2 = new MessageBean();
						onlines.remove(mb.getUsername()); //�������û���socket�Ƴ�
						/*
						 * @error ������ֹ�bug�����ﲻ����HashSet��addAll()������������Ҫ�Ƴ�
						 * ָ�����û����������ָ�����û��кܶ෽��
						 * */
						onlineSet.clear();
						onlineSet.addAll(onlines.keySet()); //���������û�
						serverBean2.setType(MessageType.ONLINE);
						serverBean2.setTime(mb.getTime());
						serverBean2.setUsername(mb.getUsername());
						serverBean2.setMessage(mb.getUsername()+"������");
						/*
						 * @error ����onlines.keySet()���ɵ���set���ϣ����Ի���Ҫͨ������
						 * һ��HashSet��תһ��
						 * */
						serverBean2.setOnlines(onlineSet); //���������û�
						sendAll(serverBean2);
						return;
					}
					//1��ʾ����
					case MessageType.ONLINE:{
						MessageBean serverBean = new MessageBean();
						onlines.put(mb.getUsername(), socket);
						onlineSet.addAll(onlines.keySet());
						serverBean.setType(MessageType.ONLINE);
						serverBean.setTime(mb.getTime());
						serverBean.setUsername(mb.getUsername());
						serverBean.setOnlines(onlineSet);
						serverBean.setMessage(mb.getUsername()+"������");
						sendAll(serverBean);
						break;
					}
					//MessageType.CHAT��ʾ����
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
					 * �����ļ�������
					 */
					case MessageType.SEND_FILE_REQ:{
						MessageBean messageBean = new MessageBean();
						messageBean.setType(MessageType.SEND_FILE_REQ);
						messageBean.setTime(mb.getTime());
						messageBean.setUsername(mb.getUsername()); //who�������ļ�
						messageBean.setMessage(mb.getMessage());
						//�������ļ��������͸���Щ��
						messageBean.setOnlines(mb.getOnlines());
						
						//����Ҫ���͵��ļ�������
						FileBean fileBean = new FileBean();
						fileBean.setFileName(mb.getFileBean().getFileName());
						fileBean.setFileSize(mb.getFileBean().getFileSize());
						fileBean.setFilePath(mb.getFileBean().getFilePath());
						messageBean.setFileBean(fileBean);
						sendMessage(messageBean);
						break;
					}
					
					/**
					 * �����ļ�
					 */
					case MessageType.ACCEPET_FILE_REQ:{
						MessageBean messageBean = new MessageBean();
						messageBean.setType(MessageType.ACCEPET_FILE_REQ);
						messageBean.setTime(mb.getTime());
						messageBean.setUsername(mb.getUsername()); //˭����������
						messageBean.setOnlines(mb.getOnlines()); //Ҫ���͵���
						messageBean.setMessage(mb.getMessage());
						
						//����Ҫ�����ļ������ԣ�����Ҫ���͵ĵ�ַ��
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
		
		//���û�������Ϣ
		public void sendMessage(MessageBean messageBean) {
			HashSet<String> h = messageBean.getOnlines(); //��ȡĿ��ͻ������б��б�ѡ�еĿͻ���
			Iterator<String> i = h.iterator(); //��ѡ�еĿͻ����е���
			while(i.hasNext()){
				String username = i.next(); //��ȡ�û���
				if(onlineSet.contains(username)){
					Socket s = onlines.get(username);
					try {
						//���s.getOutputStream()Ϊ�գ�������ͻ��������˳�
						//��ʱ��Ҫ֪ͨ�����ˣ����б����Ƴ������������˵��б���Ϣ���¡�
						if(s.isClosed()){
							messageBean.setType(MessageType.ONLINE);
							onlines.remove(username); //ȥ������˵�������Ϣ
							onlineSet.clear(); //����գ������
							onlineSet.addAll(onlines.keySet()); //���������û�
							messageBean.setOnlines(onlineSet);
							messageBean.setTime(mb.getTime());
							messageBean.setUsername(username);
							messageBean.setMessage(username + "������");
							sendAll(messageBean);
							return;
						}
						ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(messageBean);
						oos.flush();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "�����������쳣", 
								"��������ʾ", JOptionPane.ERROR_MESSAGE);
						System.out.println("������������Ϣʧ��...");
						e.printStackTrace();
					}
				}
			}
		}
		
		//�����������û�������Ϣ
		public void sendAll(MessageBean mb) {
			//��ȡ���������û���ͨ�ŵ�ַsocket��Ȼ����һ�������Ƿ��ͷ�װ�õ���Ϣ
			//����Ҫ��������������Ϣ���б���£�
			Collection<Socket> s = onlines.values();
			Iterator<Socket> i = s.iterator(); //���������ߵ��û����е���
			//�����ObjectOutputStream���ܹرգ�Ϊʲô��
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
		
		//�رտͻ��ˡ�����������
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
