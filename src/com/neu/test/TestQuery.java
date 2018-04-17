package com.neu.test;

import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;

import com.neu.dao.handler.BeanHandler;
import com.neu.dao.handler.BeanListHandler;
import com.neu.dao.query.BasicQuery;
import com.neu.dao.query.OutParameter;
import com.neu.dao.query.PetrichorDataSource;
import com.neu.dao.query.Query;
import com.neu.out.Student;

public class TestQuery {
	@Test
	public void test1(){
		String url = "jdbc:mysql://localhost:3306/test?useSSL=true";
		Properties info = new Properties();
		info.put("user", "root");
		info.put("password", "wk");
		Connection connection = null;
		int i = 0;
		String sql = "select count(*) from student";
		String sql2 = "select username from student where id = ?";
		String sql3 = "insert into student(id, username, password, email) "
				+ "values(?, ?, ?, ?)";
		String sql4 = "delete from student where id = ?";
		try {
			Driver driver = new com.mysql.jdbc.Driver();
			connection = driver.connect(url, info);
		} catch (SQLException e) {
			System.out.println("数据库连接失败。。。");
			e.printStackTrace();
		}
		
		Query query = new BasicQuery();
		try {
			String value = query.queryForValue(connection, sql2, String.class, 3);
			int update = query.update(connection, sql3, 1, "wangke", "wk", "820594484@qq.com");
			System.out.println(value);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	@Test
	public void test2(){
		String url = "jdbc:mysql://localhost:3306/test?useSSL=true";
		Properties info = new Properties();
		info.put("user", "root");
		info.put("password", "wk");
		Connection connection = null;
		int i = 0;
		String sql = "{? = call myf1(?)}";
		
		try {
			Driver driver = new com.mysql.jdbc.Driver();
			connection = driver.connect(url, info);
		} catch (SQLException e) {
			System.out.println("数据库连接失败。。。");
			e.printStackTrace();
		}
		
		try {
			CallableStatement prepareCall = connection.prepareCall(sql);
			prepareCall.registerOutParameter(1, Types.INTEGER);
			prepareCall.setString(2, "wangke");
			prepareCall.execute();
			Object x = null;
			x= prepareCall.getObject(1);
//			prepareCall.setObject(1, x);
			System.out.println(x);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	DataSource dataSource = new PetrichorDataSource();
	@Test
	public void test3(){
		try {
			Connection connection = dataSource.getConnection();
			String sql = "{? = call myf1(?)}";
			String sql2 = "select count(*) from student";
			String sql3 = "update student set password = ? where id = ?";
			CallableStatement call = connection.prepareCall(sql);
			Query query = new BasicQuery();
			OutParameter<Integer> out = new OutParameter<>(Types.INTEGER, Integer.class);
			int i = query.query(connection, sql3, "wangke", 10);
			System.out.println(i);
//			System.out.println(out.getValue());
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4(){
		String sql = "select user_id from message";
		try {
			Query query = new BasicQuery(dataSource);
			List<Integer> values = query.queryForList(sql, Integer.class);
			System.out.println(values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public <T> void test5(){
		Class<Integer> iClass = Integer.class;
		try {
			Constructor<Integer> constructor = iClass.getDeclaredConstructor(int.class);
			System.out.println(constructor);
//			Integer integer = iClass.newInstance();
//			System.out.println(integer);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
