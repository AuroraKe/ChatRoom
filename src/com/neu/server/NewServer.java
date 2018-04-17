package com.neu.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewServer {
	private static Selector selector; //选择器
	private static final int MAX_THREADS = 1<<2; //启动的读写线程的数量，aka 8
	
	public NewServer(int port) {
		try {
			init(port);
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void init(int port) throws IOException{
		//生成选择器
		selector = Selector.open();
		//获得服务器套接字实例  
		ServerSocketChannel serverChannel = ServerSocketChannel.open();  
		//绑定端口号  
		serverChannel.socket().bind(new InetSocketAddress(port));  
		//设置为非阻塞  
		serverChannel.configureBlocking(false);  
		//将ServerSocketChannel注册到选择器，指定其行为为"等待接受连接"  
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	private void listen() throws IOException{
		while(true){
			int num = selector.select();
			if(num > 0){
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey sk = it.next();
					if(sk.isAcceptable()){
						ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
						SocketChannel sc = ssc.accept();
						if(sc == null)
							continue;
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					}else if(sk.isReadable()){
//						sk.channel();
//						byte[] bs = new Reader().read((SocketChannel) sk.channel());
//						if(bs == null)
//							continue;
						DataServer.processRequest(sk);
					}
					it.remove();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(MAX_THREADS);
		exec.execute(new DataServer());
		exec.shutdown();
		new NewServer(8888);
	}
}
