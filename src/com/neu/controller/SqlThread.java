package com.neu.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neu.bean.Message;
import com.neu.util.SimpleDate;

/**
 * @author 千古传奇
 * 启动多个线程执行数据库操作
 */
public class SqlThread<V> implements Callable<V>{
	
	/**
	 * 存放要执行的SQL语句的语句池
	 */
	private static List<SqlParam> sqlPool = new ArrayList<>();
	
	public SqlThread() {
	}
	
	private Lock lock = new ReentrantLock(); //显式锁
	private Condition condition = lock.newCondition(); //用于线程通信
 	
	/**
	 * 将要执行的数据库操作放入到sqlPool中，并唤醒其它线程
	 * @param sqlThread
	 */
	public void executeSql(SqlParam sqlParam){
		lock.lock();
		try {
			sqlPool.add(sqlPool.size(), sqlParam);
			condition.signalAll();
		} finally {
			lock.unlock();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public V call() throws Exception {
		lock.lock();
		Object obj = null; //要返回的结果
		SqlParam sqlParam;
		try {
			while(sqlPool.isEmpty())
				condition.await();
			sqlParam = sqlPool.remove(0);
			obj = handler(sqlParam.getMethodName(), sqlParam.getObject(), sqlParam.getClazzes(), sqlParam.getParams());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return (V) obj;
	}
	
	/**
	 * @param methodName 要执行的方法名
	 * @param object 包含此方法的对象
	 * @param params 此方法的参数
	 */
	private Object handler(String methodName, Object object, Class<?>[] parameterType, Object... params) {
		Class<? extends Object> mClass = object.getClass();
		Object returnVal = null;
		try {
			Method method = mClass.getDeclaredMethod(methodName, parameterType);
//			method.get
			method.setAccessible(true);
			returnVal = method.invoke(object, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	
	public static void main(String[] args) {
		MessageController controller = new MessageController();
//		UserController userController = new UserController();
		Message message = new Message();
		message.setType(1);
		message.setTime(SimpleDate.currentTime());
		message.setStatus(0);
		message.setUser_id(1);
		Class<?> [] mClasses = {Message.class};
		SqlParam sqlParam = new SqlParam("insert", controller, mClasses, message);
		SqlThread<Integer> sqlThread = new SqlThread<>();
		sqlThread.executeSql(sqlParam);
		FutureTask<Integer> task = new FutureTask<>(sqlThread);
		new Thread(task).start();
		try {
			System.out.println(task.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		sqlThread.start();
//		sqlThread.executeSql(sqlThread);
	}
}
