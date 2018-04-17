package com.neu.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

public class TestByteBuffer {
	@Test
	public void test1(){
		String s = "大家好，我叫小小小";
		ByteBuffer buffer = ByteBuffer.allocate(4);
		byte[] bytes = s.getBytes();
		int i = 0;
		while(i < bytes.length){
			buffer.put(bytes, i, i + 4);
			i = i + 4;
			buffer.flip();
			System.out.println(new String(buffer.array(), 0, buffer.array().length));
			buffer.clear();
			System.out.println(buffer.remaining());
		}
//		buffer.put("大家好，我叫小小小".getBytes());
//		buffer.flip();
//		buffer.clear();
	}
	
	@Test
	public void client() throws IOException{
		//1.建立缓冲区
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
		//2.切换成非阻塞模式
		sChannel.configureBlocking(false);
		
		//3.建立选择器
		Selector selector = Selector.open();
		//4.注册通道
		sChannel.register(selector, SelectionKey.OP_READ);
		
		//5.缓冲区并写入数据
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put("你好".getBytes());
		buffer.flip();
		//6.将缓冲区加入管道中
		sChannel.write(buffer);
		buffer.clear();
//		if(sChannel.isConnected()){
//		}
		
		while(selector.select() > 0){
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			SelectionKey sk = null;
			while(it.hasNext()){
				sk = it.next();
				it.remove();
				if(sk.isValid()){
					SocketChannel sc = (SocketChannel) sk.channel();
					if(sk.isConnectable()){
						sc.register(selector, SelectionKey.OP_READ);
						doWrite(sc);
					}
					else if(sk.isReadable()){
						ByteBuffer bb = ByteBuffer.allocate(1024);
						sc.read(bb);
						bb.flip();
						System.out.println(new String(bb.array()));
					}
				}
			}
		}
	}
	
	public void doWrite(SocketChannel sc) throws IOException{
		//5.缓冲区并写入数据
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		Scanner s = new Scanner(System.in);
		String line = "";
		while(s.hasNext()) {
			line = s.nextLine();
			buffer.put(line.getBytes());
			buffer.flip();
			//6.将缓冲区加入管道中
			sc.write(buffer);
			buffer.clear();
		}
	}
	
	@Test
	public void server() throws IOException{
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		ssChannel.configureBlocking(false);
		//监听该端口号
		ssChannel.bind(new InetSocketAddress(8888));
		
		Selector selector = Selector.open();
		//将通道注册到选择器，监听accept事件
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		while(selector.select() > 0){
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			SelectionKey sk = null;
			while(it.hasNext()){
				sk = it.next();
				it.remove();
				if(sk.isValid()){
					if(sk.isAcceptable()){
						ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
						SocketChannel sc = ssc.accept();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					}else if(sk.isReadable()){
						
					}
				}
			}
		}
	}
}
