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
	 * 读取通道中的字节数据
	 * @param socketChannel
	 * @return 如果off等于0，即通道中没有数据，则返回null
	 * @throws IOException
	 */
	public byte[] read(SocketChannel socketChannel) throws IOException {
		lock.lock();
		byte[] data; //aka 1024 * 1024
		int off = 0; //当前data中的字节数
		try {
			SocketChannel sc = socketChannel;
			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
			data = new byte[BUFFER_SIZE << 10];
			int count = 0; //从通道中读取的字节数
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
	 * 数组扩容
	 * @param data 原数组
	 * @param size 增长的大小
	 */
	public byte[] grow(byte[] src, int size) {
		byte[] tmp = new byte[src.length + size];
		System.arraycopy(src, 0, tmp, 0, src.length);
		return tmp;
	}
}
