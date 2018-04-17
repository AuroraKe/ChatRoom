package com.neu.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Man{
	void fly();
	void move();
}

//ί����
class SuperMan implements Man{

	@Override
	public void fly() {
		System.out.println("I can fly...");
	}

	@Override
	public void move() {
		System.out.println("I can move quickly...");
	}
	
}

//�̶��Ĵ����
class MyMethod{
	public void eat(){
		System.out.println("method one...------------");
	}
	public void drink(){
		System.out.println("method two...------------");
	}
}

//�����࣬�����ڵ���ί����֮ǰ������һЩ������
class MyInvocation implements InvocationHandler{
	Object object; //
	public void setObject(Object object) {
		this.object = object;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) 
			throws Throwable {
		MyMethod myMethod = new MyMethod();
		myMethod.eat();
		Object returnVal = method.invoke(object, args);
		myMethod.drink();
		return returnVal;
	}
}


//���ɴ������
class MyProxy{
	public static Object getMyInvocation(Object object){
		MyInvocation myInvocation = new MyInvocation();
		myInvocation.setObject(object);
		return Proxy.newProxyInstance(object.getClass().getClassLoader(), 
				object.getClass().getInterfaces(), myInvocation);
	}
	
}

/**
 * @author ǧ�Ŵ���
 * ����������
 */
public class Reflect {
	public static void main(String[] args) {
		Man man = new SuperMan();
		Man superMan = (Man) MyProxy.getMyInvocation(man);
		superMan.fly();
		superMan.move();
	}
}
