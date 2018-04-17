package com.neu.dao.query;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

public abstract class AbstractPetrichorDataSource implements DataSource{
	private HashMap<String, String> jdbcProperties = new HashMap<>();
	
	/**
	 * 
	 */
	public AbstractPetrichorDataSource() {
	}
	public AbstractPetrichorDataSource(String fileName) {
		this.loadProperties(fileName);
	}
	
	@Override
	public Connection getConnection()
			throws SQLException {
		return this.getConnection(jdbcProperties.get("user"), 
				jdbcProperties.get("password"));
	}
	
	@Override
	public Connection getConnection(String username, String password)
		    throws SQLException {
		Connection connection = null;
		try {
			Class.forName(jdbcProperties.get("driverClass"));
			connection = DriverManager.getConnection(jdbcProperties.get("url"), 
					username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	private void loadProperties(String propertyName){
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader()
					.getResourceAsStream(propertyName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		jdbcProperties.put("driverClass", properties.getProperty("driverClass"));
		jdbcProperties.put("url", properties.getProperty("url"));
		jdbcProperties.put("user", properties.getProperty("user"));
		jdbcProperties.put("password", properties.getProperty("password"));
	}
}
