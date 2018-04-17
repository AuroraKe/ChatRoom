package com.neu.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.neu.bean.Message;
import static com.neu.client.Type.*;
import com.neu.util.SerializeObject;

/**
 * @author ǧ�Ŵ���
 * ���տͻ��˷��͵����ݣ������д����õ�һ������󣬴��ݸ�Writer����д����
 */
public class DataServer extends Thread{
	private static List<SelectionKey> pool = new LinkedList<>();
	
	//��������Ҳ��Ҫά��һ�������û��б�����������û�������Ϣ
	private static Map<String, SocketChannel> onlines; 
	
	private Reader reader = new Reader();
	
	private SerializeObject bto = new SerializeObject();
	
	//ͬ����
//	private Lock lock = new ReentrantLock();
	public DataServer() {
		onlines = new HashMap<>();
	}
	
	/**
	 * ����ͻ����󣬹����û������ӳأ������Ѷ����е��߳̽��д���
	 * @param sk һ�����Զ�ȡ���ݵ�SelectionKey
	 */
	public static void processRequest(SelectionKey sk){
		synchronized(pool){
			pool.add(pool.size(), sk);
			pool.notifyAll();
		}
	}
	
	@Override
	public void run() {
		while(true){
			SelectionKey sk;
			try {
				synchronized(pool){
					while(pool.isEmpty())
						pool.wait();
					sk = pool.remove(0);
				}
				SocketChannel sc = (SocketChannel) sk.channel();
				byte[] bs = reader.read(sc);
				if(bs == null)
					continue;
				Message message = (Message) bto.deserialize(bs);
				write(message, sc);
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	/**
	 * �������ݵ������ͻ���
	 * @param message
	 * @param sk
	 * @throws IOException 
	 */
	private void write(Message message, SocketChannel sc) throws IOException {
		int type = message.getType();
		switch (type) {
		case TEST:{
			System.out.println("DataServer�յ���Ϣ...");
			break;
		}
		case OFFLINE:{
			sendMsg(message);
			String name = message.getUsername();
			onlines.remove(name);
			message.setType(CHAT);
			sendAll(message);
			sc.socket().close();
			sc.close();
			break;
		}
		
		case ONLINE:{ //�ͻ������ӷ�����
			//�����ߵ�����������͸��û���SocketChannel����һ��Map�У����Ը����û�����������Ϣ
			String name = message.getUsername();
			onlines.put(name, sc);
			sendAll(message);
			break;
		}

		case CHAT: {
			sendMsg(message);
			break;
		}
		default:
			break;
		}
	}

	private void sendMsg(Message message) throws IOException {
		String username = message.getUsername();
		SocketChannel sc = onlines.get(username);
		byte[] bs = bto.serialize(message);
		ByteBuffer bb = ByteBuffer.allocate(bs.length);
		bb.put(bs);
		bb.flip();
		sc.write(bb);
		bb.clear();
	}

	private void sendAll(Message message) throws IOException {
		byte[] bs = bto.serialize(message);
		for(SocketChannel sc : onlines.values()){
			ByteBuffer buffer = ByteBuffer.allocate(bs.length);
			buffer.put(bs);
			buffer.flip();
			sc.write(buffer);
			buffer.clear();
		}
	}
	
	
}
