package com.neu.dao.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.neu.dao.handler.ResultSetHandler;

public interface Query {
	/**
	 * @param connection 数据库连接
	 * @param sql 要执行的SQL语句，可以是存储过程
	 * @param params SQL语句中的参数列表
	 * @return 受影响的行数，如果没有行受到影响则返回-1
	 * @throws SQLException
	 */
	int update(Connection connection, String sql, Object...params) throws SQLException;
	
	/**
	 * @param sql 要执行的SQL语句，可以是存储过程
	 * @param params SQL语句中的参数列表
	 * @return 受影响的行数，如果没有行受到影响则返回-1
	 * @throws SQLException
	 */
	int update(String sql, Object...params) throws SQLException;
	 
	
	/*
	 * 
	 * --------------------------------------------------------
	 * 
	 */
	
	
	/**
	 * @param connection 数据库连接
	 * @param sql 要执行的SQL语句，专门用于执行存储过程，如果执行存储过程，建议使用这个
	 * @param params SQL语句中的参数列表
	 * @return 返回一个受影响的行数
	 * @throws SQLException
	 */
	@Deprecated
	int query(Connection connection, String sql, Object... params) throws SQLException;
	
	
	/*
	 * 
	 * --------------------------------------------------------
	 * 
	 */
	
	/**
	 * <p>如果返回值是多列，此方法也只取第一列</p>
	 * <p>如果想获得多列的值，请使用方法<em>queryList</em></p>
	 * @param connection 数据库连接
	 * @param sql 要执行的SQL语句，可以是存储过程
	 * @param rsh 对对象进行封装的类，如BeanHandler，就是封装成一个Bean返回，其封装成T的对象并返回
	 * @param params SQL语句中的参数列表
	 * @return 一个T类型的对象，就算查询结果是一组对象，仍然只返回第一个对象
	 * @throws SQLException
	 */
	<T> T query(Connection connection, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException;
	
	/**
	 * <p>如果返回值是多列，此方法也只取第一列</p>
	 * <p>如果想获得多列的值，请使用方法<em>queryList</em></p>
	 * @param sql 要执行的SQL语句，可以是存储过程
	 * @param rsh 对对象进行封装的类，如BeanHandler，就是封装成一个Bean返回，其封装成T的对象并返回
	 * @param params SQL语句中的参数列表
	 * @return 一个T类型的对象，就算查询结果是一组对象，仍然只返回第一个对象
	 * @throws SQLException
	 */
	<T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException;
	
	// --------------------------------------------------------
	
	/**
	 * @param connection 数据库连接
	 * @param sql 要执行的SQL语句，可以是存储过程
	 * @param rsh 封装对象，如BeanListHandler，其封装成列表，其中的元素是一个个T的对象，然后返回列表
	 * @param params SQL语句中的参数列表
	 * @return 一个列表，其中的类型为T
	 * @throws SQLException
	 */
	<T> List<T> queryList(Connection connection, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException;
	/**
	 * @param sql 要执行的SQL语句，可以是存储过程
	 * @param rsh 封装对象，如BeanListHandler，其封装成列表，其中的元素是一个个T的对象，然后返回列表
	 * @param params SQL语句中的参数列表
	 * @return 一个列表，其中的类型为T
	 * @throws SQLException
	 */
	<T> List<T> queryList(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException;
	
	/*
	 * 
	 * --------------------------------------------------------
	 * 
	 */
	
	
	/**
	 * <p>如果执行DQL语言，所得的结果集包含多列，其只取第一列的值；
	 * 如果结果集包含多列，其也只返回第一行的第一个值；</p>
	 * <p>如果想获得多列的值请使用方法queryForList</p>
	 * @param connection 数据库连接
	 * @param sql 要执行的SQL语句，可以是存储过程
	 * @param javaType 需要返回的Java数据类型，如String.class、Integer.class等
	 * @param params SQL语句中的参数列表
	 * @return 一个统计值，或某个字段的值
	 * @throws SQLException
	 */
	<E> E queryForValue(Connection connection, String sql, Class<E> javaType, Object... params) throws SQLException;
	
	/**
	 * <p>如果执行DQL语言，所得的结果集包含列参数，其只取第一列的值；
	 * 如果结果集包含多列，其也只返回第一行的第一个值；</p>
	 * <p>如果想获得多列的值请使用方法queryForList</p>
	 * @param sql 要执行的SQL语句，可以是存储过程
	 * @param javaType 需要返回的Java数据类型，如String.class、Integer.class等
	 * @param params SQL语句中的参数列表
	 * @return 一个统计值，或某个字段的值
	 * @throws SQLException
	 */
	<E> E queryForValue(String sql, Class<E> javaType, Object... params) throws SQLException;

	//-----------------------------------------------------------
	
	/**
	 * 返回某个字段组成的列表
	 * @param connection 数据库连接
	 * @param sql 要执行的SQL语句
	 * @param javaType 需要返回的Java数据类型，如果返回值是整型，则是Integer.class
	 * @param params SQL语句中的参数列表
	 * @return 一个统计值，或某个字段的值组成的列表
	 * @throws SQLException 
	 */
	<E> List<E> queryForList(Connection connection, String sql, Class<E> javaType, Object... params) throws SQLException;

	/**
	 * 返回某个字段组成的列表
	 * @param sql 要执行的SQL语句
	 * @param javaType 需要返回的Java数据类型，如返回值是整型，则是Integer.class
	 * @param params SQL语句中的参数列表
	 * @return 一个统计值，或某个字段的值组成的列表
	 * @throws SQLException
	 */
	<E> List<E> queryForList(String sql, Class<E> javaType, Object... params) throws SQLException;
}
