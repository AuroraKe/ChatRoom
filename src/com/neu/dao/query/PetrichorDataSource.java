package com.neu.dao.query;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class PetrichorDataSource extends AbstractPetrichorDataSource{

	/**
	 * ���ʹ��Ĭ�Ϲ�������������Դ����ʹ��Ĭ���ļ�·��������·���µ�jdbc.properties��
	 */
	public PetrichorDataSource(){
		this("jdbc.properties");
	}
	public PetrichorDataSource(String fileName){
		super(fileName);
	}
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

}
