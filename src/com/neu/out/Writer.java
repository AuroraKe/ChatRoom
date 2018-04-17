package com.neu.out;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neu.bean.Message;
import com.neu.client.Type;

public class Writer extends Thread{
	private static List<SelectionKey> pool = new LinkedList<>();
	//��������Ҳ��Ҫά��һ�������û��б�����������û�������Ϣ
	private static Map<String, SocketChannel> onlines; 
	private static Message message;
	
	public Writer() {
		onlines = new HashMap<>();
	}
	
	public static void processRequest(Map<String, SocketChannel> onlines, Message message){
		Writer.message = message;
		synchronized(pool){
//			pool.add(sk);
			pool.notifyAll();
		}
	}
	
	@Override
	public void run() {
		while(true){
			try {
				SelectionKey sk;
				synchronized(pool){
					while(pool.isEmpty())
						pool.wait();
					sk = pool.remove(0);
				}
				write(sk);
			} catch (Exception e) {
			}
		}
	}

	private void write(SelectionKey sk) {
		int type = message.getType();
		System.out.println(message.getMessage());
		switch (type) {
		case Type.ONLINE:{ //�ͻ������ӷ�����
			SocketChannel sc = (SocketChannel) sk.channel();
			onlines.put(message.getUsername(), sc);
			break;
		}

		case Type.CHAT: {
			
			break;
		}
		default:
			break;
		}
	}
	
}
