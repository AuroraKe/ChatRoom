package com.neu.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 千古传奇
 * 目标对象实现的接口，用JDK来生成代理对象一定要实现一个借口
 */
interface Map{
	boolean next();
	boolean previous();
}

//委托类（被代理类）
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
 * @author 千古传奇
 * 实现自己的InvocationHandler
 */
class MyInvocationHandler implements InvocationHandler{
	private Object target; //被代理类对象
	public MyInvocationHandler(Object target) {
		this.target = target;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//在执行目标方法之前
		System.out.println("before the method");
		//执行目标方法
		Object returnVal = method.invoke(target, args);
		//在执行目标方法之后
		System.out.println("after the method \n");
		return returnVal;
	}
	
	//通过委托类对象获得代理类对象
	public Object getProxy(){
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), 
				target.getClass().getInterfaces(), this);
	}
}

public class DynamicProxy {
	public static void main(String[] args) {
		//获得被代理类对象
		HashMap hashMap = new HashMap();
		MyInvocationHandler handler = new MyInvocationHandler(hashMap);
		//获得一个代理类对象
		Map map = (Map) handler.getProxy();
		
		//调用目标方法
		System.out.println(map.next());
	}
}
