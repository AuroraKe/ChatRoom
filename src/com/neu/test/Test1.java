package com.neu.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.neu.bean.Online;
import com.neu.client.Connect;
import com.neu.controller.OnlineController;
import com.neu.server.Reader;
import com.neu.util.LocalAddress;

public class Test1 {
	private OnlineController onlineController = new OnlineController();
	
	@Test
	public void server() throws IOException{
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		ssChannel.configureBlocking(false);
		ssChannel.socket().bind(new InetSocketAddress(8888));
		Selector selector = Selector.open();
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		while(selector.select() > 0){
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			
			while(it.hasNext()){
				SelectionKey sk = it.next();
				it.remove();
				if(sk.isAcceptable()){
					ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
					SocketChannel sc = ssc.accept();
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_WRITE);
//					sk.interestOps(SelectionKey.OP_READ | sk.interestOps());
					sk.cancel();
					sk.interestOps(SelectionKey.OP_WRITE | sk.interestOps());
					System.out.println("测试接受成功");
				}
				if(sk.isReadable()){
					System.out.println("测试读成功");
					
				}
				if(sk.isWritable()){
					System.out.println("测试写成功");
					
				}
				System.out.println(sk);
			}
		}
	}
	
	@Test
	public void client() throws IOException{
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
//		sChannel.configureBlocking(false);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put("Hello".getBytes());
		buffer.flip();
		sChannel.write(buffer);
		buffer.clear();
	}
	
	@Test
	public void test2() throws IOException{
		SocketChannel sc = SocketChannel.open();
		Reader reader2 = new Reader();
		System.out.println(reader2);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					reader2.read(sc);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		reader2.read(sc);
	}
	
	@Test
	public void test3() throws IOException{
		Reader reader = new Reader();
		SocketChannel sc = SocketChannel.open();
		System.out.println(reader);
		reader.read(sc);
		
	}
	
	@Test
	public void test4() throws IOException{
		System.out.println(0b10 ^ 0b01);
	}
}
