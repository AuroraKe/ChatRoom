package com.neu.test;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import com.neu.dao.query.PetrichorDataSource;
import com.neu.out.Student;

public class TestDBUtils {
	 @Test
    public <T> void test4() throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String sql = "select * from student";
        DataSource dataSource = new PetrichorDataSource();
        List<List<Student>> execute = queryRunner.execute(dataSource.getConnection(), sql, new BeanListHandler<>(Student.class));
        
    }
}
