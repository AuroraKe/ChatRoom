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
 * @author ǧ�Ŵ���
 * ��������߳�ִ�����ݿ����
 */
public class SqlThread<V> implements Callable<V>{
	
	/**
	 * ���Ҫִ�е�SQL��������
	 */
	private static List<SqlParam> sqlPool = new ArrayList<>();
	
	public SqlThread() {
	}
	
	private Lock lock = new ReentrantLock(); //��ʽ��
	private Condition condition = lock.newCondition(); //�����߳�ͨ��
 	
	/**
	 * ��Ҫִ�е����ݿ�������뵽sqlPool�У������������߳�
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
		Object obj = null; //Ҫ���صĽ��
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
	 * @param methodName Ҫִ�еķ�����
	 * @param object �����˷����Ķ���
	 * @param params �˷����Ĳ���
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
