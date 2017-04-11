package com.appspy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBAccess {
	static Connection con;
	
	public static void createConnection(){
		try {			
			String dbConnectionString = Constants.DB_CONN_STRING;
			String dbUser = Constants.DB_USER;
			String dbPassword = Constants.DB_PASS;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(dbConnectionString, dbUser, dbPassword);   
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}
	public static int updateData(String q) {
		try {
			Statement st = con.createStatement();
			int r = st.executeUpdate(q);
			return r;
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println(e1.getMessage());
			return 0;
		}

	}

	public static ResultSet selectData(String q) {
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(q);
			return rs;
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
			return null;
		}

	}

}
