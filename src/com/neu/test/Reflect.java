package com.neu.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Man{
	void fly();
	void move();
}

//委托类
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

//固定的代码块
class MyMethod{
	public void eat(){
		System.out.println("method one...------------");
	}
	public void drink(){
		System.out.println("method two...------------");
	}
}

//代理类，即我在调用委托类之前可以做一些其它事
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


//生成代理对象
class MyProxy{
	public static Object getMyInvocation(Object object){
		MyInvocation myInvocation = new MyInvocation();
		myInvocation.setObject(object);
		return Proxy.newProxyInstance(object.getClass().getClassLoader(), 
				object.getClass().getInterfaces(), myInvocation);
	}
	
}

/**
 * @author 千古传奇
 * 面向切面编程
 */
public class Reflect {
	public static void main(String[] args) {
		Man man = new SuperMan();
		Man superMan = (Man) MyProxy.getMyInvocation(man);
		superMan.fly();
		superMan.move();
	}
}
