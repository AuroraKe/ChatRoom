package com.neu.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.neu.dao.query.PetrichorDataSource;

public class TestC3P0 {
	@Test
	public void getConnection() throws SQLException{
		DataSource cpds = new ComboPooledDataSource("c3p0");
		Connection connection = cpds.getConnection();
		String sql = "select * from student where id = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
//		ps.seto
		System.out.println(connection);
		
	}
	
	@Test
	public void getConnection2(){
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().
					getResourceAsStream("jdbc.properties"));
			String driverClass = properties.getProperty("driverClass");
			String url = properties.getProperty("url");
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");
			Connection connection = DriverManager.getConnection(url, user, password);
			//1.¼ÓÔØÇý¶¯
			Class.forName(driverClass);
			System.out.println(connection);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return null;
	}
	
	public static void main(String[] args) {
		TestC3P0 c3p0 = new TestC3P0();
		c3p0.getConnection2();
	}
}
