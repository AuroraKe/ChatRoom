package com.neu.dao.query;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * @author ǧ�Ŵ���
 *
 * @param <T>
 * ���ڶ���洢�����е��������
 */
public class OutParameter<T> {
	private final int sqlType;
	private final Class<T> javaType;
	private T value;
	
	public OutParameter(int sqlType, Class<T> javaType) {
		this.sqlType = sqlType;
		this.javaType = javaType;
	}
	
	/**
	 * @return ��ȡ���������ֵ
	 */
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public int getSqlType() {
		return sqlType;
	}

	public Class<T> getJavaType() {
		return javaType;
	}
	
	void setValue(CallableStatement cs, int index) throws SQLException{
		Object object = cs.getObject(index);
		this.value =  this.javaType.cast(object);
	}
	
	void register(CallableStatement cs, int index) throws SQLException{
		cs.registerOutParameter(index, sqlType);
		if(this.value != null)
			cs.setObject(index, value);
	}
}
