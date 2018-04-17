package com.neu.controller;

/**
 * 使用反射执行数据库的操作的方法时，需要使用的参数
 * @author 千古传奇
 */
public class SqlParam {
	/**
	 * 要执行的方法名
	 */
	private final String methodName;
	private final Object object;
	private final Object[] params;
	private final Class<?>[] clazzes;
	
	/**
	 * <p>如我要调用一个这样的方法：<em><blockquote>public int insert(Message message){}</blockquote></em>
	 * 其中的<em>clazzes</em>为参数的类型，作为一个数组输入，即
	 * <em><blockquote>Class<?>[] clazzes = {Message.class}</blockquote></em></p>
	 * 
	 * <p>我需要声明一个包含该方法的类的一个对象，作为参数<em>object</em>：
	 * <em><blockquote>MessageController messageController = 
	 * new MessageController()</blockquote></em>
	 * </p>
	 * 
	 * <p>所以最终结果为：<em><blockquote>SqlParam sqlParam = 
	 * new SqlParam("insert", messageController, mClasses, message);</blockquote></em></p>
	 * 
	 * @param methodName 要执行的方法名
	 * @param object 包含该方法的类一个对象
	 * @param clazzes 参数的运行时类对象
	 * @param params 该方法需要的参数
	 */
	public <T> SqlParam(String methodName, Object object, Class<?>[] clazzes, Object... params) {
		this.methodName = methodName;
		this.object = object;
		this.clazzes = clazzes;
		this.params = params;
	}

	public String getMethodName() {
		return methodName;
	}

	public Object getObject() {
		return object;
	}

	public Object[] getParams() {
		return params;
	}
	
	public Class<?>[] getClazzes() {
		return clazzes;
	}
}
