package com.neu.dao.query;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class AbstractQuery implements Query {
	private DataSource dataSource;
	
	public AbstractQuery() {
	}
	public AbstractQuery(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	protected PreparedStatement preparedStatement(Connection connection, String sql) 
			throws SQLException{
		return connection.prepareStatement(sql);
	}

	protected CallableStatement prepareCall(Connection connection, String sql)
			throws SQLException{
		return connection.prepareCall(sql);
	}
	
	protected Connection prepareConnection(){
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public void fillStatement(PreparedStatement ps, Object... params) 
			throws SQLException{
		ParameterMetaData pmd = ps.getParameterMetaData();
		CallableStatement cs = null;
		if(params.length != pmd.getParameterCount())
			throw new SQLException("The number of parameter is wrong.");
		if(ps instanceof CallableStatement){
			cs = (CallableStatement) ps;
		}
		for (int i = 0; i < params.length; i++) {
			if(params[i] != null){
				if(cs != null && params[i] instanceof OutParameter){
					((OutParameter<?>)params[i]).register(cs, i + 1);
				}else{
					ps.setObject(i + 1, params[i]);
				}
			}else{
				int sqlType = pmd.getParameterType(i + 1);
				ps.setNull(i + 1, sqlType);
			}
		}
			
	}
	
	protected void retriveOutParameter(CallableStatement cs, Object... params)
			throws SQLException{
		for (int i = 0; i < params.length; i++) {
			if(params[i] instanceof OutParameter)
				((OutParameter<?>)params[i]).setValue(cs, i + 1);
		}
	}
}
