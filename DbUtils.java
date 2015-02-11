package it.unibo.csr.big.webpoleu.kc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {

	public static Connection connect(String sid, String username, String password){
		 
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} 
		
		catch (ClassNotFoundException e) {
			System.out.println("Oracle JDBC Driver not found.");
			e.printStackTrace();
			return null;
		}
 
		Connection connection = null;
 
		try {
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@137.204.74.10:1521:"+sid, username, password);
		} 
		catch (SQLException e) {
			try {
				connection = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:"+sid, username, password);
			}
			catch (SQLException e1) {
				System.out.println("Connection Failed! Check output console");
				e.printStackTrace();
				e1.printStackTrace();
				return null;
			}
		}
 
		if (connection == null) {
			System.out.println("Failed to make connection!");
		}
		
		return connection;
	}
}
