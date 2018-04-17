package com.neu.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;

import com.neu.dao.handler.BeanHandler;
import com.neu.dao.handler.BeanListHandler;
import com.neu.dao.query.BasicQuery;
import com.neu.dao.query.PetrichorDataSource;
import com.neu.dao.query.Query;
import com.neu.out.Student;

public class TestPetrichor {
	@Test
	public void test1(){
		DataSource dataSource = new PetrichorDataSource();
		String sql = "select * from student limit ?, ?";
		try {
			Connection connection = dataSource.getConnection();
			Query query = new BasicQuery();
			
//			Long value = query.queryForValue(connection, sql, Long.class);
			Student query2 = query.query(connection, sql, new BeanHandler<>(Student.class), 0, 20);
//			List<Student> list = query.queryList(connection, sql, new BeanListHandler<>(Student.class), 0, 20);
//			for(Student student : list){
//				System.out.println(student);
//			}
			System.out.println(query2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test2(){
		DataSource dataSource = new PetrichorDataSource();
		String sql = "select count(*) from student where id = ?";
		try {
			Connection connection = dataSource.getConnection();
			Query query = new BasicQuery();
			
			Long value = query.queryForValue(connection, sql, Long.class, 3);
			System.out.println(value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
