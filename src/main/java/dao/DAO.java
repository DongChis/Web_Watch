package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import context.DBContext;
import entity.Product;
import entity.User;

public class DAO {

	private static DAO instance;

	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;

	private DAO() {
	}

	public static synchronized DAO getInstance() {
		if (instance == null) {
			instance = new DAO();
		}
		return instance;
	}

	// Phương thức để lấy tất cả các sản phẩm
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM Products";
		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				products.add(new Product(rs.getInt("ProductID"), rs.getString("Title"), rs.getString("Name"),
						rs.getString("Description"), rs.getDouble("Price"), rs.getString("ImageURL"),
						rs.getString("Gender")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return products;
	}

	// Kiểm tra xem người dùng đã tồn tại
	public User getUserByUsername(String username) {
		String query = "SELECT * FROM Users WHERE Username = ?";
		try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new User(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Password"),
							rs.getString("Email"), rs.getString("Role"), rs.getDate("CreatedAt"));
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	// Phương thức để đăng nhập
	public User login(String user, String pass) {
		String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, user);
			ps.setString(2, pass);
			rs = ps.executeQuery();
			if (rs.next()) {
				return new User(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Password"),
						rs.getString("Email"), rs.getString("Role"), rs.getDate("CreatedAt"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void signUp(String username, String password) throws Exception {
		if (isUsernameTaken(username)) {
			throw new Exception("Tên người dùng đã tồn tại!");
		}

		 String query = "INSERT INTO Users (Username, Password, Email, Role, CreatedAt) VALUES (?, ?, NULL, 'Customer', GETDATE())";
	

		try {
			conn = new DBContext().getConnection();
			
			ps = conn.prepareStatement(query);
			System.out.println(ps.toString() + "prepareStatement");
			ps.setString(1, username);
			ps.setString(2, password); 

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Lỗi trong quá trình đăng ký. Vui lòng thử lại sau.");
		}
	}

	private boolean isUsernameTaken(String username) {
		String query = "SELECT * FROM Users WHERE Username = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, username);
			rs = ps.executeQuery();

			return rs.next();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}