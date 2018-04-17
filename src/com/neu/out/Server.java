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
	private static Selector selector; //ѡ����
	private SelectionKey sKey;
	//��������Ҳ��Ҫά��һ�������û��б�����������û�������Ϣ
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
		//����ѡ����
		selector = Selector.open();
		//��÷������׽���ʵ��  
		ServerSocketChannel serverChannel = ServerSocketChannel.open();  
		//�󶨶˿ں�  
		serverChannel.socket().bind(new InetSocketAddress(port));  
		//����Ϊ������  
		serverChannel.configureBlocking(false);  
		//��ServerSocketChannelע�ᵽѡ������ָ������ΪΪ"�ȴ���������"  
		sKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	private void listen() throws IOException{
		while(true){
			int num = selector.select();
			//ѡ��һ����׼������IO������ͨ����key
			if(num > 0){
				//��ѡ�����ϻ�ȡ��ѡ���key�ļ��ϲ����е���
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey sk = it.next();
					//����key��ͨ���ǵȴ������µ��׽�������
					if(sk.isAcceptable()){
						ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
						SocketChannel sc = ssc.accept();
						if(sc == null)
							continue;
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					}
					//����key��ͨ������Ϊ��"��" 
					else if(sk.isReadable()){
						readMsg(sk);
					}
					else if(sk.isWritable()){
						writeMsg(sk);
					}
					//����2 = ����1�������ǵ�ַ������޸ļ���2Ҳ��Ӱ�켯��1
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
		case Type.ONLINE:{ //�ͻ������ӷ�����
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
