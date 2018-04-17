package com.neu.dao.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ResultSetHandler<T> {
	List<T> handler(ResultSet rs) throws SQLException;
}
