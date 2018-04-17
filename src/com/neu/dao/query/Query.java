package com.neu.dao.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.neu.dao.handler.ResultSetHandler;

public interface Query {
	/**
	 * @param connection ���ݿ�����
	 * @param sql Ҫִ�е�SQL��䣬�����Ǵ洢����
	 * @param params SQL����еĲ����б�
	 * @return ��Ӱ������������û�����ܵ�Ӱ���򷵻�-1
	 * @throws SQLException
	 */
	int update(Connection connection, String sql, Object...params) throws SQLException;
	
	/**
	 * @param sql Ҫִ�е�SQL��䣬�����Ǵ洢����
	 * @param params SQL����еĲ����б�
	 * @return ��Ӱ������������û�����ܵ�Ӱ���򷵻�-1
	 * @throws SQLException
	 */
	int update(String sql, Object...params) throws SQLException;
	 
	
	/*
	 * 
	 * --------------------------------------------------------
	 * 
	 */
	
	
	/**
	 * @param connection ���ݿ�����
	 * @param sql Ҫִ�е�SQL��䣬ר������ִ�д洢���̣����ִ�д洢���̣�����ʹ�����
	 * @param params SQL����еĲ����б�
	 * @return ����һ����Ӱ�������
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
	 * <p>�������ֵ�Ƕ��У��˷���Ҳֻȡ��һ��</p>
	 * <p>������ö��е�ֵ����ʹ�÷���<em>queryList</em></p>
	 * @param connection ���ݿ�����
	 * @param sql Ҫִ�е�SQL��䣬�����Ǵ洢����
	 * @param rsh �Զ�����з�װ���࣬��BeanHandler�����Ƿ�װ��һ��Bean���أ����װ��T�Ķ��󲢷���
	 * @param params SQL����еĲ����б�
	 * @return һ��T���͵Ķ��󣬾����ѯ�����һ�������Ȼֻ���ص�һ������
	 * @throws SQLException
	 */
	<T> T query(Connection connection, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException;
	
	/**
	 * <p>�������ֵ�Ƕ��У��˷���Ҳֻȡ��һ��</p>
	 * <p>������ö��е�ֵ����ʹ�÷���<em>queryList</em></p>
	 * @param sql Ҫִ�е�SQL��䣬�����Ǵ洢����
	 * @param rsh �Զ�����з�װ���࣬��BeanHandler�����Ƿ�װ��һ��Bean���أ����װ��T�Ķ��󲢷���
	 * @param params SQL����еĲ����б�
	 * @return һ��T���͵Ķ��󣬾����ѯ�����һ�������Ȼֻ���ص�һ������
	 * @throws SQLException
	 */
	<T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException;
	
	// --------------------------------------------------------
	
	/**
	 * @param connection ���ݿ�����
	 * @param sql Ҫִ�е�SQL��䣬�����Ǵ洢����
	 * @param rsh ��װ������BeanListHandler�����װ���б����е�Ԫ����һ����T�Ķ���Ȼ�󷵻��б�
	 * @param params SQL����еĲ����б�
	 * @return һ���б����е�����ΪT
	 * @throws SQLException
	 */
	<T> List<T> queryList(Connection connection, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException;
	/**
	 * @param sql Ҫִ�е�SQL��䣬�����Ǵ洢����
	 * @param rsh ��װ������BeanListHandler�����װ���б����е�Ԫ����һ����T�Ķ���Ȼ�󷵻��б�
	 * @param params SQL����еĲ����б�
	 * @return һ���б����е�����ΪT
	 * @throws SQLException
	 */
	<T> List<T> queryList(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException;
	
	/*
	 * 
	 * --------------------------------------------------------
	 * 
	 */
	
	
	/**
	 * <p>���ִ��DQL���ԣ����õĽ�����������У���ֻȡ��һ�е�ֵ��
	 * ���������������У���Ҳֻ���ص�һ�еĵ�һ��ֵ��</p>
	 * <p>������ö��е�ֵ��ʹ�÷���queryForList</p>
	 * @param connection ���ݿ�����
	 * @param sql Ҫִ�е�SQL��䣬�����Ǵ洢����
	 * @param javaType ��Ҫ���ص�Java�������ͣ���String.class��Integer.class��
	 * @param params SQL����еĲ����б�
	 * @return һ��ͳ��ֵ����ĳ���ֶε�ֵ
	 * @throws SQLException
	 */
	<E> E queryForValue(Connection connection, String sql, Class<E> javaType, Object... params) throws SQLException;
	
	/**
	 * <p>���ִ��DQL���ԣ����õĽ���������в�������ֻȡ��һ�е�ֵ��
	 * ���������������У���Ҳֻ���ص�һ�еĵ�һ��ֵ��</p>
	 * <p>������ö��е�ֵ��ʹ�÷���queryForList</p>
	 * @param sql Ҫִ�е�SQL��䣬�����Ǵ洢����
	 * @param javaType ��Ҫ���ص�Java�������ͣ���String.class��Integer.class��
	 * @param params SQL����еĲ����б�
	 * @return һ��ͳ��ֵ����ĳ���ֶε�ֵ
	 * @throws SQLException
	 */
	<E> E queryForValue(String sql, Class<E> javaType, Object... params) throws SQLException;

	//-----------------------------------------------------------
	
	/**
	 * ����ĳ���ֶ���ɵ��б�
	 * @param connection ���ݿ�����
	 * @param sql Ҫִ�е�SQL���
	 * @param javaType ��Ҫ���ص�Java�������ͣ��������ֵ�����ͣ�����Integer.class
	 * @param params SQL����еĲ����б�
	 * @return һ��ͳ��ֵ����ĳ���ֶε�ֵ��ɵ��б�
	 * @throws SQLException 
	 */
	<E> List<E> queryForList(Connection connection, String sql, Class<E> javaType, Object... params) throws SQLException;

	/**
	 * ����ĳ���ֶ���ɵ��б�
	 * @param sql Ҫִ�е�SQL���
	 * @param javaType ��Ҫ���ص�Java�������ͣ��緵��ֵ�����ͣ�����Integer.class
	 * @param params SQL����еĲ����б�
	 * @return һ��ͳ��ֵ����ĳ���ֶε�ֵ��ɵ��б�
	 * @throws SQLException
	 */
	<E> List<E> queryForList(String sql, Class<E> javaType, Object... params) throws SQLException;
}
