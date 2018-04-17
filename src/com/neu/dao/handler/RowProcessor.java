package com.neu.dao.handler;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RowProcessor<T> {
	public T createBean(ResultSet rs, Class<? extends T> tClass) throws SQLException {
		T bean = this.newInstance(tClass);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			//×Ö¶ÎÃû
			String labelName = rsmd.getColumnLabel(i + 1);
			try {
				Field field = bean.getClass().getDeclaredField(labelName);
				field.setAccessible(true);
				field.set(bean, rs.getObject(labelName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bean;
	}
	
	public T newInstance(Class<? extends T> tClass) {
		T t = null;
		try {
			t = tClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}
}
