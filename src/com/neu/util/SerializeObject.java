package com.neu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeObject {
	/**
	 * 将对象序列化
	 * @param object
	 * @return 返回一个字节数组
	 * @throws IOException
	 */
	public byte[] serialize(Object object) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		return baos.toByteArray();
	}
	
	/**
	 * 将序列化后的字节转换成一个对象
	 * @param bs
	 * @throws IOException 
	 */
	public Object deserialize(byte[] bs) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bs);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object object = null;
		try {
			object = ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}
}
