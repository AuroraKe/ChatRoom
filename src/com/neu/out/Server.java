package com.neu.out;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.neu.bean.Message;
import com.neu.client.Type;


public class Server{
	private static Selector selector; //选择器
	private SelectionKey sKey;
	//服务器端也需要维护一个在线用户列表，用来向定向的用户发送消息
	private static Map<String, SocketChannel> onlines; 
	
	public Server(int port) {
		onlines = new HashMap<>();
		try {
			init(port);
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init(int port) throws IOException {
		//生成选择器
		selector = Selector.open();
		//获得服务器套接字实例  
		ServerSocketChannel serverChannel = ServerSocketChannel.open();  
		//绑定端口号  
		serverChannel.socket().bind(new InetSocketAddress(port));  
		//设置为非阻塞  
		serverChannel.configureBlocking(false);  
		//将ServerSocketChannel注册到选择器，指定其行为为"等待接受连接"  
		sKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	private void listen() throws IOException{
		while(true){
			int num = selector.select();
			//选择一组已准备进行IO操作的通道的key
			if(num > 0){
				//从选择器上获取已选择的key的集合并进行迭代
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey sk = it.next();
					//若此key的通道是等待接受新的套接字连接
					if(sk.isAcceptable()){
						ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
						SocketChannel sc = ssc.accept();
						if(sc == null)
							continue;
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					}
					//若此key的通道的行为是"读" 
					else if(sk.isReadable()){
						readMsg(sk);
					}
					else if(sk.isWritable()){
						writeMsg(sk);
					}
					//集合2 = 集合1，给的是地址，因此修改集合2也会影响集合1
					it.remove();
				}
			}
		}
	}

	private void readMsg(SelectionKey sk) throws IOException {
		SocketChannel sc = (SocketChannel) sk.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] bytes = new byte[1024];
		while((sc.read(buffer)) > 0){
			buffer.flip();
			bytes = buffer.array().clone();
			buffer.clear();
		}
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
		Message message = new Message();
		try {
			message = (Message) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		int type = message.getType();
		System.out.println(message.getMessage());
		switch (type) {
		case Type.ONLINE:{ //客户端连接服务器
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while(it.hasNext()){
				SelectionKey key = it.next();
				if(key != sKey){
					key.attach(message);
					key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
				}
			}
			break;
		}

		case Type.CHAT: {
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while(it.hasNext()){
				SelectionKey key = it.next();
				if(key != sKey){
					key.attach(message);
					key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
				}
			}
			break;
		}
		default:
			break;
		}
	}
	
	private void writeMsg(SelectionKey sk) throws IOException {
		SocketChannel sc = (SocketChannel) sk.channel();
		Message message = (Message) sk.attachment();
		sk.attach(null);
		int type = message.getType();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		byte[] bs = baos.toByteArray();
		ByteBuffer bb = ByteBuffer.allocate(bs.length);
		bb.put(bs);
		switch (type) {
		case Type.CHAT:{
			bb.flip();
			sc.write(bb);
			bb.clear();
			break;
		}
		case Type.EXIT:{
			sk.cancel();
			sc.socket().close();
			sc.close();
			return;
		}
		default:
			break;
		}
		sk.interestOps(SelectionKey.OP_READ);
	}
	
	public static void main(String[] args) {
		new Server(8888);
	}
}
