package com.neu.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ǧ�Ŵ���
 * Ŀ�����ʵ�ֵĽӿڣ���JDK�����ɴ������һ��Ҫʵ��һ�����
 */
interface Map{
	boolean next();
	boolean previous();
}

//ί���ࣨ�������ࣩ
class HashMap implements Map{
	private static final int i = 0;
	int j = i + 1;
	@Override
	public boolean next() {
		System.out.println(j <<= 1);
		return false;
	}

	@Override
	public boolean previous() {
		System.out.println(j >>= 1);
		return false;
	}
	
}

/**
 * @author ǧ�Ŵ���
 * ʵ���Լ���InvocationHandler
 */
class MyInvocationHandler implements InvocationHandler{
	private Object target; //�����������
	public MyInvocationHandler(Object target) {
		this.target = target;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//��ִ��Ŀ�귽��֮ǰ
		System.out.println("before the method");
		//ִ��Ŀ�귽��
		Object returnVal = method.invoke(target, args);
		//��ִ��Ŀ�귽��֮��
		System.out.println("after the method \n");
		return returnVal;
	}
	
	//ͨ��ί��������ô��������
	public Object getProxy(){
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(), this);
	}
}

public class DynamicProxy {
	public static void main(String[] args) {
		//��ñ����������
		HashMap hashMap = new HashMap();
		MyInvocationHandler handler = new MyInvocationHandler(hashMap);
		//���һ�����������
		Map map = (Map) handler.getProxy();
		
		//����Ŀ�귽��
		System.out.println(map.next());
	}
}
