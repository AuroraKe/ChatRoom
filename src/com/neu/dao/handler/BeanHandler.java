package com.neu.dao.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeanHandler<T> implements ResultSetHandler<T> {
	private RowProcessor<T> processor = new RowProcessor<>();
	
	//一个T类型的运行时类
	private final Class<? extends T> tClass;
	public BeanHandler(Class<? extends T> tClass) {
		this.tClass = tClass;
	}
	
	@Override
	public List<T> handler(ResultSet rs) throws SQLException {
//		this.toBean(rs, tClass);
		List<T> list = new ArrayList<>();
		list.add(this.toBean(rs, tClass));
		return list;
	}

	public T toBean(ResultSet rs, Class<? extends T> tClass) throws SQLException{
		T bean = null;
		if (rs.next()) {
			bean = this.processor.createBean(rs, tClass);
		}
		return bean;
	}
}
