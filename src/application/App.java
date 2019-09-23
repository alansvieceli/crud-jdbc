package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;

public class App {

	public static void main(String[] args) {
		RecuperarDados();

	}
	
	private static void RecuperarDados() {
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
	
			conn = DB.getConnection();
			try {
				st = conn.createStatement();
				rs = st.executeQuery("select * from department");
				
				while (rs.next()) {
					System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
				}
				
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		
	}

}
