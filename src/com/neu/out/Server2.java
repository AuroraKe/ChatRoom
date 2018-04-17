package com.neu.out;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.neu.bean.Message;

public class Server2 implements Runnable{
	private Selector selector; //选择器
	private Map<String, SocketChannel> clientsMap;

	public Server2() {
		clientsMap = new HashMap<>();
		
	}
	
	@Override
	public void run() {
		try {
			init();
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void init() throws IOException {
		ServerSocketChannel ssChanel = ServerSocketChannel.open();
		ssChanel.configureBlocking(false); //转换成非阻塞模式
		ssChanel.bind(new InetSocketAddress(8888)); //监听端口
		
		selector = Selector.open();
		ssChanel.register(selector, SelectionKey.OP_ACCEPT); //将通道注册到选择器上
	}
	
	private void listen() throws IOException{
		while(selector.select() > 0){
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while(it.hasNext()){
				SelectionKey sk = it.next();
				it.remove();
				if(sk.isAcceptable()){
					ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
					SocketChannel sc = ssc.accept();
					if(sc == null)
						continue;
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);
				}else if(sk.isReadable()){
					Message attachment = (Message) sk.attachment();
					System.out.println(attachment.getMessage());
					SocketChannel sc = (SocketChannel) sk.channel();
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					while(sc.read(buffer) > 0){
						buffer.flip();
						System.out.println(new String(buffer.array()));
						Message message = new Message();
						message.setMessage(new String(buffer.array()));
						sendMessage(message, sk);
						buffer.clear();
					}
				}
			
			}
		}
	}

	
	private void sendMessage(Message message, SelectionKey sk) throws IOException{
		SocketChannel sChannel = (SocketChannel) sk.channel();
		String msg = message.getMessage();
		byte[] bytes = msg.getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		sChannel.write(buffer);
		buffer.clear();
	}
	
	public static void main(String[] args) {
//		ExecutorService threadPool = Executors.newFixedThreadPool(5);
//		threadPool.submit(new Server2());
		new Thread(new Server2()).start();
//		threadPool.shutdown();
//		new Server();
	}

}
