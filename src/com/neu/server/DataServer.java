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
 * @author 千古传奇
 * 接收客户端发送的数据，并进行处理，得到一个对象后，传递给Writer进行写处理
 */
public class DataServer extends Thread{
	private static List<SelectionKey> pool = new LinkedList<>();
	
	//服务器端也需要维护一个在线用户列表，用来向定向的用户发送消息
	private static Map<String, SocketChannel> onlines; 
	
	private Reader reader = new Reader();
	
	private SerializeObject bto = new SerializeObject();
	
	//同步锁
//	private Lock lock = new ReentrantLock();
	public DataServer() {
		onlines = new HashMap<>();
	}
	
	/**
	 * 处理客户请求，管理用户的连接池，并唤醒队列中的线程进行处理
	 * @param sk 一个可以读取数据的SelectionKey
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
	 * 发送数据到其它客户端
	 * @param message
	 * @param sk
	 * @throws IOException 
	 */
	private void write(Message message, SocketChannel sc) throws IOException {
		int type = message.getType();
		switch (type) {
		case TEST:{
			System.out.println("DataServer收到消息...");
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
		
		case ONLINE:{ //客户端连接服务器
			//发送者的姓名，将其和该用户的SocketChannel放在一个Map中，可以根据用户名定向发送消息
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
