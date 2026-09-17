package com.boardone;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

// 데이터베이스를 연결하기 위한 클래스
public class ConnUtil {

	private static DataSource ds;

	static {
		try {
			Context init = new InitialContext();

			ds = (DataSource) init.lookup("java:comp/env/jdbc/myOracle");

		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}