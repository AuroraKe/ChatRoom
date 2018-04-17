package com.neu.dao.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BeanListHandler<T> implements ResultSetHandler<T>{
	private RowProcessor<T> processor = new RowProcessor<>();
	
	private final Class<T> tClass;
	
	public BeanListHandler(Class<T> tClass) {
		this.tClass = tClass;
	}
	
	@Override
	public List<T> handler(ResultSet rs) throws SQLException {
		return this.toBeanList(rs, tClass);
	}
	
	public List<T> toBeanList(ResultSet rs, Class<T> tClass) throws SQLException{
		List<T> beanList = new ArrayList<>();
		while (rs.next()) {
			this.processor.createBean(rs, tClass);
			beanList.add(this.processor.createBean(rs, tClass));
		}
		return beanList;
	}

	

}
