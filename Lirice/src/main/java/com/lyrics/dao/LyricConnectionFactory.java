package com.lyrics.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

public class LyricConnectionFactory {
	static String driverClassName = "com.mysql.jdbc.Driver";
	
	//for AWS RDS
	//static String connectionUrl = "jdbc:mysql://kask-db-instance.c1rialg0ee8x.us-east-1.rds.amazonaws.com:3306/lyrics";
	//static String jndiName = "java:jboss/datasources/lyrics";
	//static String dbUser = "kaskuser";
	//static String dbPwd = "kask4all";
	
	// For Local
//	static String connectionUrl = "jdbc:mysql://lcoalhost:3306/lyrics";
	static String jndiName = "java:jboss/datasources/lyrics";
//	static String dbUser = "root";
//	static String dbPwd = "root";
	static Connection conn = null;
	DataSource dataSource;
	private static LyricConnectionFactory connectionFactory = null;
	
	private LyricConnectionFactory() {
		
		// for Local Mysql driver
//		try {
//			Class.forName(driverClassName);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		
		
		//For RDS/Jboss/JNDI
		try {

			Context ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup(jndiName);
			// Class.forName(driverClassName);
		} catch (NamingException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public Connection getConnection() throws SQLException {
//		if(conn == null)
//		 conn = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
//		return conn;
//	}
	
	public Connection getConnection() throws SQLException {
		Connection conn = null;

		//for RDS/JNDI
		conn = dataSource.getConnection();

		  // for Local
		 //conn = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
		return conn;
	}
	
	public static LyricConnectionFactory getInstance() {
		if (connectionFactory == null) {
			synchronized (LyricConnectionFactory.class) {
				if (connectionFactory == null) {
					connectionFactory = new LyricConnectionFactory();
				}
			}
		}
		return connectionFactory;
	}

}
