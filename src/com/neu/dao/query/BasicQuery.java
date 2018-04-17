package com.neu.dao.query;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.neu.dao.handler.ResultSetHandler;

public class BasicQuery extends AbstractQuery{

	public BasicQuery() {
	}
	public BasicQuery(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public int update(Connection connection, String sql, Object... params) throws SQLException {
		return this.update(connection, true, sql, params);
	}
	
	@Override
	public int update(String sql, Object... params) throws SQLException {
		return this.update(super.prepareConnection(), true, sql, params);
	}

	/*
	 * 
	 * --------------------------------------------------------
	 * 
	 */
	
	@Override
	@Deprecated
	public int query(Connection connection, String sql, 
			Object... params) throws SQLException {
		return this.update(connection, true, sql, params);
	}
	
	/*
	 * 
	 * --------------------------------------------------------
	 * 
	 */
	
	@Override
	public <T> T query(Connection connection, String sql, ResultSetHandler<T> rsh, 
			Object... params) throws SQLException {
		return this.<T>query(connection, true, sql, rsh, params).get(0);
	}
	
	@Override
	public <T> T query(String sql, ResultSetHandler<T> rsh, 
			Object... params) throws SQLException {
		return this.<T>query(super.prepareConnection(), true, sql, rsh, params).get(0);
	}

	/*
	 * 
	 * --------------------------------------------------------
	 * 
	 */
	
	@Override
	public <T> List<T> queryList(Connection connection, String sql, 
			ResultSetHandler<T> rsh, Object... params) throws SQLException {
		return this.query(connection, true, sql, rsh, params);
	}
	@Override
	public <T> List<T> queryList(String sql, ResultSetHandler<T> rsh, Object... params) 
			throws SQLException {
		return this.query(super.prepareConnection(), true, sql, rsh, params);
	}

	/*
	 * 
	 * --------------------------------------------------------
	 * 
	 */
	
	@Override
	public <E> E queryForValue(Connection connection, String sql, Class<E> javaType, Object... params) 
			throws SQLException {
		E e = this.queryForValue(connection, true, sql, javaType, params).get(0);
		return e;
	}
	
	@Override
	public <E> E queryForValue(String sql, Class<E> javaType, Object... params) 
			throws SQLException {
		E e = this.queryForValue(super.prepareConnection(), true, sql, javaType, params).get(0);
		return e;
	}
	
	//-----------------------------------------------------
	@Override
	public <E> List<E> queryForList(Connection connection, String sql, Class<E> javaType, Object... params) throws SQLException {
		return this.queryForValue(connection, true, sql, javaType, params);
	}
	@Override
	public <E> List<E> queryForList(String sql, Class<E> javaType, Object... params) throws SQLException {
		return this.queryForValue(super.prepareConnection(), true, sql, javaType, params);
	}
	
	
	private <T> List<T> query(Connection connection, boolean connClose, String sql, 
			ResultSetHandler<T> rsh, Object... params) throws SQLException{
		if(connection == null){
			throw new SQLException("Null connection");
		}else if(sql == null){
			if(connClose)
				connection.close();
			throw new SQLException("Null sql statement");
		}else if(rsh == null){
			if(connClose)
				connection.close();
			throw new SQLException("Null Class");
		}else{
			CallableStatement cs = this.prepareCall(connection, sql);
			this.fillStatement(cs, params);
			ResultSet rs = cs.executeQuery();
			this.retriveOutParameter(cs, params);
			List<T> list = rsh.handler(rs);
			this.release(connection, cs, rs);
			return list;
		}
	}
	
	private int update(Connection connection, boolean connClose, String sql, 
			Object... params) throws SQLException{
		if(connection == null){
			throw new SQLException("Null connection");
		}else if(sql == null){
			if(connClose)
				connection.close();
			throw new SQLException("Null sql statement");
		}else{
			int rows = 0;
			CallableStatement cs = this.prepareCall(connection, sql);
			this.fillStatement(cs, params);
			rows = cs.executeUpdate();
			this.retriveOutParameter(cs, params);
			this.release(connection, cs, null);
			return rows;
		}
	}
	
	private <E> List<E> queryForValue(Connection connection, boolean connClose, String sql, Class<E> javaType, Object... params) 
			throws SQLException {
		if(connection == null){
			throw new SQLException("Null connection");
		}else if(sql == null){
			if(connClose)
				connection.close();
			throw new SQLException("Null sql statement");
		}else{
			List<E> es = new ArrayList<>();
			E e = null;
			CallableStatement cs = this.prepareCall(connection, sql);
			this.fillStatement(cs, params);
			ResultSet rs = cs.executeQuery();
			while(rs.next()){
				e = javaType.cast(rs.getObject(1));
				es.add(e);
			}
			this.retriveOutParameter(cs, params);
			this.release(connection, cs, rs);
			return es;
		}
	}
	
	/**
	 * ¹Ø±ÕConnection¡¢StatementºÍResultSet
	 * @param conn
	 * @param st
	 * @param rs
	 */
	private void release(Connection conn, Statement st, ResultSet rs){
		try {
			if (rs != null)
				rs.close();
			if(st != null)
				st.close();
			if(conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
