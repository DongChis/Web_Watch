package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import context.DBContext;
import entity.DeletedProduct;
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

	public List<DeletedProduct> getRecentlyDeletedProducts() {
		List<DeletedProduct> deletedProducts = new ArrayList<>();
		String query = "SELECT product_id, product_name, deleted_by, deleted_at FROM recent_deleted_products ORDER BY deleted_at DESC"; // Sắp
																																		// xếp
																																		// theo
																																		// thời
																																		// gian
																																		// xóa

		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				int productId = rs.getInt("product_id");
				String productName = rs.getString("product_name");
				String deletedBy = rs.getString("deleted_by");
				Timestamp deletedAt = rs.getTimestamp("deleted_at");

				// Tạo đối tượng DeletedProduct và thêm vào danh sách
				DeletedProduct deletedProduct = new DeletedProduct(productId, productName, deletedBy, deletedAt);
				deletedProducts.add(deletedProduct);
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
		return deletedProducts; // Trả về danh sách các sản phẩm đã xóa
	}

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

	public boolean isAdmin(User user) {
		String query = "SELECT Role FROM Users WHERE UserID = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);

			ps.setInt(1, user.getUserID());

			rs = ps.executeQuery();

			if (rs.next()) {
				String roleInDb = rs.getString("Role");
				return "Admin".equals(roleInDb);
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
		return false;
	}

	public void signUp(String username, String password, String email) throws Exception {
		if (isUsernameTaken(username)) {
			throw new Exception("Tên người dùng đã tồn tại!");
		}
		String query = "INSERT INTO Users (Username, Password, Email, Role, CreatedAt) VALUES (?, ?, ?, 'Customer', GETDATE())";

		try {
			conn = new DBContext().getConnection();

			ps = conn.prepareStatement(query);
			System.out.println(ps.toString() + " prepareStatement");
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, email);

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			// throw new Exception("Lỗi trong quá trình đăng ký. Vui lòng thử lại sau.");
		}
	}

	public void insertProduct(String title, String name, String description, String price, String image,
			String gender) {
		String query = "insert into Products \n" + "VALUES (?,?,?,?,?,?)";
		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, title);
			ps.setString(2, name);
			ps.setString(3, description);
			ps.setString(4, price);
			ps.setString(5, "img/" + image);
			ps.setString(6, gender);

			ps.executeUpdate();
		} catch (Exception e) {
		}
	}

//	public void deleteProduct(String pid) {
//		String query = "delete from Products \n" + "where ProductID = ?";
//		try {
//			conn = new DBContext().getConnection();
//			ps = conn.prepareStatement(query);
//			ps.setString(1, pid);
//			ps.executeUpdate();
//		} catch (Exception e) {
//		}
//
//	}

	public void clearRecentDeletedProducts() {
		String query = "DELETE FROM recent_deleted_products"; // Truy vấn để xóa tất cả dữ liệu

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = new DBContext().getConnection(); // Kết nối tới cơ sở dữ liệu
			ps = conn.prepareStatement(query); // Tạo đối tượng PreparedStatement với truy vấn
			ps.executeUpdate(); // Thực thi truy vấn xóa dữ liệu
		} catch (Exception e) {
			e.printStackTrace(); // In ra thông tin lỗi nếu có
		} finally {
			// Đảm bảo đóng kết nối và tài nguyên
			try {
				if (ps != null)
					ps.close(); // Đóng PreparedStatement
				if (conn != null)
					conn.close(); // Đóng kết nối
			} catch (SQLException e) {
				e.printStackTrace(); // In ra thông tin lỗi nếu có
			}
		}
	}

//	public void deleteProduct(String pid, String username) {
//		String selectQuery = "SELECT ProductID, [Name] FROM Products WHERE ProductID = ?";
//		String deleteQuery = "DELETE FROM Products WHERE ProductID = ?";
//		String deleteRecentDeletedQuery = "DELETE FROM recent_deleted_products WHERE product_id = ?";
//		String insertDeletedQuery = "INSERT INTO recent_deleted_products"
//				+ " (product_id, product_name, deleted_by, deleted_at) VALUES (?, ?, ?, ?)";
//		try {
//			conn = new DBContext().getConnection();
//			conn.setAutoCommit(false);
//			ps = conn.prepareStatement(selectQuery);
//			ps.setString(1, pid);
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				int productId = rs.getInt("ProductID");
//				String productName = rs.getString("Name");
//				
//				 // Xóa các bản ghi trong bảng recent_deleted_products
//	            ps = conn.prepareStatement(deleteRecentDeletedQuery);
//	            ps.setInt(1, productId);
//	            ps.executeUpdate();
//	            
//				String deletedBy = username;
//				java.sql.Timestamp deletedAt = new java.sql.Timestamp(System.currentTimeMillis());
//				ps = conn.prepareStatement(insertDeletedQuery);
//				ps.setInt(1, productId);
//				ps.setString(2, productName);
//				ps.setString(3, deletedBy);
//				ps.setTimestamp(4, deletedAt);
//				int rowsInserted = ps.executeUpdate();
//				if (rowsInserted > 0) {
//					ps = conn.prepareStatement(deleteQuery);
//					ps.setString(1, pid);
//					ps.executeUpdate();
//					conn.commit();
//					System.out.println("Xoa thanh cong! " + productName);
//				} else {
//					conn.rollback();
//					System.out.println("Không thể chèn sản phẩm vào bảng recent_deleted_products.");
//				}
//			} else {
//				System.out.println("Không tìm thấy sản phẩm với ID: " + pid);
//				return;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//			try {
//				if (ps != null)
//					ps.close();
//				if (conn != null)
//					conn.close();
//			} catch (SQLException ex) {
//				ex.printStackTrace();
//			}
//		}
//	}
	
	public void deleteProduct(String pid, String username) {
	    String selectQuery = "SELECT ProductID, [Name] FROM Products WHERE ProductID = ?";
	    String deleteQuery = "DELETE FROM Products WHERE ProductID = ?";
	    //String deleteRecentDeletedQuery = "DELETE FROM recent_deleted_products WHERE product_id = ?";
	    String insertDeletedQuery = "INSERT INTO recent_deleted_products (product_id, product_name, deleted_by, deleted_at) VALUES (?, ?, ?, ?)";

	    try {
	        conn = new DBContext().getConnection();
	        conn.setAutoCommit(false); // Bắt đầu giao dịch
	        
	        // Lấy thông tin sản phẩm
	        ps = conn.prepareStatement(selectQuery);
	        ps.setString(1, pid);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            int productId = rs.getInt("ProductID");
	            String productName = rs.getString("Name");

	            // Xóa các bản ghi trong bảng recent_deleted_products có liên quan đến sản phẩm
//	            ps = conn.prepareStatement(deleteRecentDeletedQuery);
//	            ps.setInt(1, productId);
//	            ps.executeUpdate(); // Xóa tất cả các bản ghi liên quan đến productId
	            
	            // Chèn vào bảng recent_deleted_products
	            String deletedBy = username;
	            java.sql.Timestamp deletedAt = new java.sql.Timestamp(System.currentTimeMillis());
	            ps = conn.prepareStatement(insertDeletedQuery);
	            ps.setInt(1, productId);
	            ps.setString(2, productName);
	            ps.setString(3, deletedBy);
	            ps.setTimestamp(4, deletedAt);
	            int rowsInserted = ps.executeUpdate();
	            
	            if (rowsInserted > 0) {
	                // Xóa sản phẩm khỏi bảng Products
	                ps = conn.prepareStatement(deleteQuery);
	                ps.setString(1, pid);
	                ps.executeUpdate();
	                conn.commit(); // Cam kết giao dịch
	                System.out.println("Xóa thành công! " + productName);
	            } else {
	                conn.rollback(); // Hoàn tác giao dịch nếu không thể chèn vào bảng recent_deleted_products
	                System.out.println("Không thể chèn sản phẩm vào bảng recent_deleted_products.");
	            }
	        } else {
	            System.out.println("Không tìm thấy sản phẩm với ID: " + pid);
	            return; // Nếu không tìm thấy sản phẩm, thoát khỏi phương thức
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // In ra lỗi nếu có
	    } finally {
	        // Đóng các tài nguyên (connection, prepared statement) nếu cần thiết
	        try {
	            if (ps != null) ps.close();
	            if (conn != null) conn.close();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	}



	public void editProduct(String title, String name, String description, String price, String image, String pid) {
		String query = "update Products set [Title] = ?,[Name] = ?,Description = ?,\r\n"
				+ "[Price] = ?,[ImageURL] = ? where ProductID = ?";
		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(query);
			ps.setString(1, title);
			ps.setString(2, name);
			ps.setString(3, description);
			ps.setString(4, price);
			ps.setString(5, "img/" + image);
			ps.setString(6, pid);
			ps.executeUpdate();
		} catch (Exception e) {
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

	public Product getProductByID(String id) {
		String query = "SELECT * FROM Products WHERE ProductID = ?";
		try {
			conn = new DBContext().getConnection();// mo ket noi voi sql
			ps = conn.prepareStatement(query);
			ps.setString(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				return new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5),
						rs.getString(6), rs.getString(7));
			}

		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		DAO d = new DAO();

		// System.out.println(d.getUserByUsername("chia"));
		// System.out.println(""+d.insertProduct(null, null, null, null, null, null));
		// d.signUp("dung1","1");
		// d.deleteProduct(session,"3002");

		// System.out.println(d.getRecentlyDeletedProducts());
		// System.out.println(d.getProductByID("1"));
	}
}
