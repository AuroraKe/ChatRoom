package com.neu.controller;

/**
 * ʹ�÷���ִ�����ݿ�Ĳ����ķ���ʱ����Ҫʹ�õĲ���
 * @author ǧ�Ŵ���
 */
public class SqlParam {
	/**
	 * Ҫִ�еķ�����
	 */
	private final String methodName;
	private final Object object;
	private final Object[] params;
	private final Class<?>[] clazzes;
	
	/**
	 * <p>����Ҫ����һ�������ķ�����<em><blockquote>public int insert(Message message){}</blockquote></em>
	 * ���е�<em>clazzes</em>Ϊ���������ͣ���Ϊһ���������룬��
	 * <em><blockquote>Class<?>[] clazzes = {Message.class}</blockquote></em></p>
	 * 
	 * <p>����Ҫ����һ�������÷��������һ��������Ϊ����<em>object</em>��
	 * <em><blockquote>MessageController messageController = 
	 * new MessageController()</blockquote></em>
	 * </p>
	 * 
	 * <p>�������ս��Ϊ��<em><blockquote>SqlParam sqlParam = 
	 * new SqlParam("insert", messageController, mClasses, message);</blockquote></em></p>
	 * 
	 * @param methodName Ҫִ�еķ�����
	 * @param object �����÷�������һ������
	 * @param clazzes ����������ʱ�����
	 * @param params �÷�����Ҫ�Ĳ���
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
