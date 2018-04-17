package com.neu.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.junit.Test;

import com.neu.dao.handler.BeanHandler;
import com.neu.dao.query.AbstractQuery;
import com.neu.dao.query.BasicQuery;
import com.neu.dao.query.OutParameter;
import com.neu.dao.query.Query;
import com.neu.out.Student;

public class TestCallableStatement {
	@Test
	public void test1(){
		String url = "jdbc:mysql://localhost:3306/test?useSSL=true";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "wk");
        Connection connection = null;
        int i = 0;
        String sql = "call pro2(?, ?)";
        try {
            Driver driver = new com.mysql.jdbc.Driver();
			connection = driver.connect(url, info);
            System.out.println(connection);
        } catch (SQLException e) {
            System.out.println("数据库连接失败。。。");
            e.printStackTrace();
        }
        try {
			CallableStatement prepareCall = connection.prepareCall(sql);
			ParameterMetaData pmd = prepareCall.getParameterMetaData();
			while(i++ < pmd.getParameterCount()){
				System.out.println(pmd.getParameterMode(i));
			}
			prepareCall.setInt(1, 101);
			prepareCall.registerOutParameter(2, Types.INTEGER);
			int j = 0;
			prepareCall.execute();
			prepareCall.setObject(2, j);
//			int j = prepareCall.getInt(2);
			System.out.println(j);
		} catch (SQLException e) {
			e.printStackTrace();
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
		String sql = "select id, username from student where id = ?";
		String sql2 = "{call pro2(?, ?)}";
		try {
			Driver driver = new com.mysql.jdbc.Driver();
			connection = driver.connect(url, info);
			System.out.println(connection);
		} catch (SQLException e) {
			System.out.println("数据库连接失败。。。");
			e.printStackTrace();
		}
		try {
			OutParameter<Integer> out = new OutParameter<>(Types.INTEGER, Integer.class);
			Query query = new BasicQuery();
			query.query(connection, sql2, 100, out);
			System.out.println(out.getValue());
//			System.out.println(student);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
