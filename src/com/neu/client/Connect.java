package com.neu.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.neu.bean.Message;
import com.neu.util.SerializeObject;


public class Connect {
	private static final String IP_ADDRESS = "172.28.210.152";//"172.28.210.152"; //服务器的IP地址
	private static final int PORT = 8888; //服务器接受信息的端口号
	private SocketChannel socketChannel;
	private SerializeObject bto = new SerializeObject();
	
//	private volatile boolean fireListen = false; //是否启用监听
//	private Reader reader = new Reader();
	
	//使用静态内部类实现懒汉式的单例模式
	private static class LazyConnect{
		private static final Connect CONNECT = new Connect();
	}
	
	//返回一个实例
	public static Connect getConnect(){
		return LazyConnect.CONNECT;
	}
	
	
	private Connect() {
		try {
			init();
		} catch (IOException e) {
			System.out.println("连接主机失败，正在尝试再次连接");
		}
	}
	
//	public void fireListen(boolean fire){
//		fireListen = fire;
//		try {
//			listen(fireListen);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 初始化客户端通信的通道
	 * @throws IOException
	 */
	private void init() throws IOException {
		socketChannel = SocketChannel.open(new InetSocketAddress(IP_ADDRESS, PORT));
		socketChannel.configureBlocking(false);
	}
	
	//存放信息的列表
//	private List<Message> mList = new ArrayList<>();
	//监听，用于获取数据
//	private void listen(boolean fire) throws IOException{
////		//写线程
////		new Thread() {
////			@Override
////			public void run() {
////				Scanner scanner = new Scanner(System.in);
////				while (scanner.hasNext()) {
////					String string = scanner.nextLine();
////					Message message = new Message();
////					message.setType(Type.CHAT);
////					message.setUser_id(1);
////					message.setMessage(string);
////					message.setUsername("wang");
////					sendMessage(message);
////				}
////				scanner.close();
////			}
////		}.start();
//		
//		//读线程
//		new Thread(){
//			@Override
//			public void run() {
//				try {
//					while(fireListen){
//						byte[] bs = reader.read(socketChannel);
//						if(bs == null)
//							continue;
//						Message message = (Message) bto.deserialize(bs);
//						System.out.println("客户端的message：" + message);
//						mList.add(mList.size(), message);
////						if(message != null)
////							System.out.println(message.getMessage());
////						ByteBuffer buffer = ByteBuffer.allocate(1024);
////						byte[] b = new byte[1024];
////						int count = 0;
////						int off = 0;
////						while ((count = socketChannel.read(buffer)) > 0) {
////							buffer.flip();
////							byte[] tmp = new byte[count];
////							tmp = buffer.array();
////							System.arraycopy(tmp, 0, b, off, count);
////							buffer.clear();
////							ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(b));
////							Message message = new Message();
////							try {
////								message = (Message) ois.readObject();
////							} catch (ClassNotFoundException e) {
////								e.printStackTrace();
////							}
////						}
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			};
//		}.start();
//	}

	//返回这个列表
//	public List<Message> getMList(){
//		return mList;
//	}
	
	//返回网络通道
	public SocketChannel getSocketChannel(){
		return socketChannel;
	}
	
	/**
	 * 尝试连接服务器，如果连接成功，就返回true，此方法在执行完之前会一直阻塞
	 * @return
	 */
	public boolean isConnect() {
		Message message = new Message();
		message.setType(Type.TEST);
		message.setMessage("测试连接...");
		message.setUsername("wang");
		boolean connect = false;
		int i = 0;
		while (i++ < 5) {
			try {
				this.sendMessage(message);
				connect = true;
				break;
			} catch (Exception e) {
				connect = false;
				try {
					Thread.sleep(3000);
					init();
				} catch (Exception e1) {
					System.out.println("连接主机失败，正在尝试再次连接");
				}
			}
		}
		return connect;
	}
	
	public void sendMessage(Message message){
		try {
			sendMessage(message, 1);
		} catch (IOException e) {
			System.out.println("向服务器发送消息失败...");
			e.printStackTrace();
		}
	}
	
	private void sendMessage(Message message, Object obj) throws IOException {
		message.setStatus(0);  //0表示信息还未发送到目标客户
		
		byte[] bytes = bto.serialize(message);
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.flip();
		socketChannel.write(buffer);
		buffer.clear();
	}
	
	public void close(){
		try {
			socketChannel.socket().close();
			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		new Connect().fireListen = true;
		
	}
}
