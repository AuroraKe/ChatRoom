package com.neu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeObject {
	/**
	 * ���������л�
	 * @param object
	 * @return ����һ���ֽ�����
	 * @throws IOException
	 */
	public byte[] serialize(Object object) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		return baos.toByteArray();
	}
	
	/**
	 * �����л�����ֽ�ת����һ������
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
