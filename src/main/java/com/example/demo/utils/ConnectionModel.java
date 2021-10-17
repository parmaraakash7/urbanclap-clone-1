package com.example.demo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionModel {
	static public int FIVE_KM = 2000;
	static public Connection getConnection() throws SQLException {
		Connection c = null;
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/urbanclap-clone1","postgres", "aakash@123");
		return c;
	}
}
