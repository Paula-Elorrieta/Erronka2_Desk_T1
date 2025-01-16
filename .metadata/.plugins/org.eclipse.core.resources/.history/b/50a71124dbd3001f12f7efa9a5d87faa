package controlador.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Konexioa {
	
	private static final String URL = "jdbc:mysql://127.0.0.1:3307/elorbase";
	private static final String USER = "root"; 
	private static final String PASSWORD = ""; 
	private Connection connection;

	public Konexioa() {
		this.connection = null;
	}

	public Connection irekiKonexioa() {
		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Konexioa egin da.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errorea konexioa egiterakoan.");
		}
		return connection;
	}

	public void itxiKonexioa() {
		if (connection != null) {
			try {
				connection.close();
				System.out.println("Konexioa itxi da.");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errorea konexioa ixterakoan.");
			}
		} else {
			System.out.println("Ez dago konexiorik ixteko.");
		}
	}
}
