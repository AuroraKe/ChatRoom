package com.neu.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import com.neu.dao.query.BasicQuery;
import com.neu.dao.query.PetrichorDataSource;
import com.neu.dao.query.Query;

public class TestExecute {
	@Test
	public void test1(){
		DataSource dataSource = new PetrichorDataSource();
		try {
			Connection connection = dataSource.getConnection();
			Query query = new BasicQuery();
//			String sql = "select * from student limit ?, ?";
			String sql = "select count(*) from student";
			PreparedStatement ps = connection.prepareStatement(sql);
//			ps.setInt(1, 0);
//			ps.setInt(2, 20);
			boolean b = ps.execute();
			ResultSet rs = null;
			if(b){
				rs = ps.getResultSet();
				while(rs.next())
					System.out.println(rs.getObject(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
