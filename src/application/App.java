package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import db.DB;
import db.DbException;
import db.DbIntegrityException;

public class App {

	public static void main(String[] args) {

		/*** colocar a fiuncao q quer executar) **/
		atualizarDados();

	}

	private static void recuperarDados() {

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
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
			DB.closeConnection();
		}

	}

	private static void inserirDados() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Connection conn = null;
		PreparedStatement st = null;

		conn = DB.getConnection();
		try {
			st = conn.prepareStatement(
					"insert into seller (name, email, birthdate, basesalary, departmentid) values (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, "Alan");
			st.setString(2, "alan@empresa.com");
			st.setDate(3, new java.sql.Date(sdf.parse("20/05/1982").getTime()));
			st.setDouble(4, 1236.23);
			st.setInt(5, 3);

			int RowsAffecteds = st.executeUpdate();
			System.out.println("Rows Affecteds: " + RowsAffecteds);
			if (RowsAffecteds > 0) {
				ResultSet rs = st.getGeneratedKeys();
				try {
					System.out.println("Chave(s) gerada(s): ");
					while (rs.next()) {
						System.out.println("  -- " + rs.getInt(1));
					}
				} finally {
					DB.closeResultSet(rs);
				}
			}

			DB.closeStatement(st);

			st = conn.prepareStatement("insert into department (name) values ('D1'),('D2')",
					Statement.RETURN_GENERATED_KEYS);
			RowsAffecteds = st.executeUpdate();
			if (RowsAffecteds > 0) {

				ResultSet rs = st.getGeneratedKeys();
				try {
					System.out.println("Chave(s) gerada(s): ");
					while (rs.next()) {
						System.out.println("  -- " + rs.getInt(1));
					}
				} finally {
					DB.closeResultSet(rs);
				}
			}

		} catch (SQLException |

				ParseException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}

	}

	private static void atualizarDados() {

		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DB.getConnection();

			st = conn.prepareStatement(
					"UPDATE seller " + "SET BaseSalary = BaseSalary + ? " + "WHERE " + "(DepartmentId = ?)");

			st.setDouble(1, 300.0);
			st.setInt(2, 2);

			int rowsAffected = st.executeUpdate();

			System.out.println("Rows affected: " + rowsAffected);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}

	}

	private static void deletarDados() {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DB.getConnection();

			st = conn.prepareStatement("DELETE FROM department " + "WHERE " + "Id = ?");

			st.setInt(1, 5);

			int rowsAffected = st.executeUpdate();

			System.out.println("Rows affected: " + rowsAffected);
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}

}
