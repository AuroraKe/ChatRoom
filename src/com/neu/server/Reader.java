package com.neu.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Reader {
	
	public Reader() {
	}
	
//	private static class LazyReader{
//		private static final Reader READER = new Reader();
//	}
//	
//	public static Reader getReader(){
//		return LazyReader.READER;
//	}
	
	private Lock lock = new ReentrantLock();
	
	private static int BUFFER_SIZE = 1 << 10; //aka 1024
	/**
	 * ��ȡͨ���е��ֽ�����
	 * @param socketChannel
	 * @return ���off����0����ͨ����û�����ݣ��򷵻�null
	 * @throws IOException
	 */
	public byte[] read(SocketChannel socketChannel) throws IOException {
		lock.lock();
		byte[] data; //aka 1024 * 1024
		int off = 0; //��ǰdata�е��ֽ���
		try {
			SocketChannel sc = socketChannel;
			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
			data = new byte[BUFFER_SIZE << 10];
			int count = 0; //��ͨ���ж�ȡ���ֽ���
			while ((count = sc.read(buffer)) > 0) {
				buffer.clear();
				if ((off + count) > data.length)
					data = grow(data, BUFFER_SIZE << 10);
				byte[] buf = buffer.array();
				System.arraycopy(buf, 0, data, off, count);
				off += count;
			} 
		} finally {
			lock.unlock();
		}
		return off == 0 ?null : data;
	}

	
	/**
	 * ��������
	 * @param data ԭ����
	 * @param size �����Ĵ�С
	 */
	public byte[] grow(byte[] src, int size) {
		byte[] tmp = new byte[src.length + size];
		System.arraycopy(src, 0, tmp, 0, src.length);
		return tmp;
	}
}
