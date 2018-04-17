package com.neu.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.neu.bean.Message;
import com.neu.util.SerializeObject;


public class Connect {
	private static final String IP_ADDRESS = "172.28.210.152";//"172.28.210.152"; //��������IP��ַ
	private static final int PORT = 8888; //������������Ϣ�Ķ˿ں�
	private SocketChannel socketChannel;
	private SerializeObject bto = new SerializeObject();
	
//	private volatile boolean fireListen = false; //�Ƿ����ü���
//	private Reader reader = new Reader();
	
	//ʹ�þ�̬�ڲ���ʵ������ʽ�ĵ���ģʽ
	private static class LazyConnect{
		private static final Connect CONNECT = new Connect();
	}
	
	//����һ��ʵ��
	public static Connect getConnect(){
		return LazyConnect.CONNECT;
	}
	
	
	private Connect() {
		try {
			init();
		} catch (IOException e) {
			System.out.println("��������ʧ�ܣ����ڳ����ٴ�����");
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
	 * ��ʼ���ͻ���ͨ�ŵ�ͨ��
	 * @throws IOException
	 */
	private void init() throws IOException {
		socketChannel = SocketChannel.open(new InetSocketAddress(IP_ADDRESS, PORT));
		socketChannel.configureBlocking(false);
	}
	
	//�����Ϣ���б�
//	private List<Message> mList = new ArrayList<>();
	//���������ڻ�ȡ����
//	private void listen(boolean fire) throws IOException{
////		//д�߳�
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
//		//���߳�
//		new Thread(){
//			@Override
//			public void run() {
//				try {
//					while(fireListen){
//						byte[] bs = reader.read(socketChannel);
//						if(bs == null)
//							continue;
//						Message message = (Message) bto.deserialize(bs);
//						System.out.println("�ͻ��˵�message��" + message);
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

	//��������б�
//	public List<Message> getMList(){
//		return mList;
//	}
	
	//��������ͨ��
	public SocketChannel getSocketChannel(){
		return socketChannel;
	}
	
	/**
	 * �������ӷ�������������ӳɹ����ͷ���true���˷�����ִ����֮ǰ��һֱ����
	 * @return
	 */
	public boolean isConnect() {
		Message message = new Message();
		message.setType(Type.TEST);
		message.setMessage("��������...");
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
					System.out.println("��������ʧ�ܣ����ڳ����ٴ�����");
				}
			}
		}
		return connect;
	}
	
	public void sendMessage(Message message){
		try {
			sendMessage(message, 1);
		} catch (IOException e) {
			System.out.println("�������������Ϣʧ��...");
			e.printStackTrace();
		}
	}
	
	private void sendMessage(Message message, Object obj) throws IOException {
		message.setStatus(0);  //0��ʾ��Ϣ��δ���͵�Ŀ��ͻ�
		
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
