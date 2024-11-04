package dao;

import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import context.DBContext;
import entity.CartItem;
import entity.DeletedProduct;
import entity.Order;
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
						rs.getString("Description"), rs.getString("Price"), rs.getString("ImageURL"),
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
	
	public Order getOrderDetailByOrderID(int orderID) throws Exception {
        String orderQuery = "SELECT * FROM Orders1 WHERE OrderID = ?";
        String orderItemQuery = "SELECT * FROM OrderItems1 WHERE OrderID = ?";
        Order order = null;

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderQuery);
             PreparedStatement orderItemStmt = conn.prepareStatement(orderItemQuery)) {
             
            // Set order ID in the query
            orderStmt.setInt(1, orderID);
            ResultSet orderResultSet = orderStmt.executeQuery();

            // Retrieve order details
            if (orderResultSet.next()) {
                String customerName = orderResultSet.getString("CustomerName");
                String customerEmail = orderResultSet.getString("CustomerEmail");
                String customerPhone = orderResultSet.getString("CustomerPhone");
                String customerAddress = orderResultSet.getString("CustomerAddress");
                String paymentMethod = orderResultSet.getString("PaymentMethod");
                Timestamp orderDate = orderResultSet.getTimestamp("OrderDate");

                // Create an Order object
                order = new Order(orderID, new ArrayList<>(), customerName, customerEmail,
                                  customerPhone, customerAddress, paymentMethod, orderDate);
                
                // Retrieve order items
                orderItemStmt.setInt(1, orderID);
                ResultSet orderItemsResultSet = orderItemStmt.executeQuery();

                while (orderItemsResultSet.next()) {
                    String productId = orderItemsResultSet.getString("ProductID");
                    int quantity = orderItemsResultSet.getInt("Quantity");
                    double price = orderItemsResultSet.getDouble("Price");

                    // Assuming you have a method to retrieve a Product by its ID
                    Product product = getProductByID(productId); // Implement this method in your DAO
                    CartItem cartItem = new CartItem(product, quantity);
                    order.getCartItems().add(cartItem); // Add the CartItem to the order
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL error occurred: " + e.getMessage());
        }

        return order; // Return the order details (or null if not found)
    }
	

	// get all order

	public List<Order> getAllOrders() {
		List<Order> orders = new ArrayList<>();
		String orderQuery = "SELECT o.OrderID, o.CustomerName, o.CustomerEmail, "
				+ "o.CustomerPhone, o.CustomerAddress, o.PaymentMethod, " + "oi.ProductID, oi.Quantity, oi.Price "
				+ "FROM Orders1 o " + "JOIN OrderItems1 oi ON o.OrderID = oi.OrderID";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = new DBContext().getConnection(); // Get the database connection
			stmt = conn.prepareStatement(orderQuery);
			rs = stmt.executeQuery();

			while (rs.next()) {
				int orderID = rs.getInt("OrderID");
				System.out.println("Retrieved OrderID: " + orderID); // Debugging line

				String customerName = rs.getString("CustomerName");
				String customerEmail = rs.getString("CustomerEmail");
				String customerPhone = rs.getString("CustomerPhone");
				String customerAddress = rs.getString("CustomerAddress");
				String paymentMethod = rs.getString("PaymentMethod");
				Timestamp orderDate = getOrderDateById(orderID); // Ensure this method is defined correctly

				String productId = rs.getString("ProductID");
				int quantity = rs.getInt("Quantity");
				double price = rs.getDouble("Price");

				CartItem cartItem = new CartItem(getProductByID(productId), quantity);

				// Check if the order already exists in the list
				Order order = orders.stream().filter(o -> o.getOrderID() == orderID
						&& o.getCustomerName().equals(customerName) && o.getCustomerEmail().equals(customerEmail)
						&& o.getCustomerPhone().equals(customerPhone) && o.getCustomerAddress().equals(customerAddress)
						&& o.getPaymentMethod().equals(paymentMethod)).findFirst().orElse(null);

				if (order == null) {
					// If the order does not exist, create a new one
					List<CartItem> items = new ArrayList<>();
					items.add(cartItem);
					orders.add(new Order(orderID, items, customerName, customerEmail, customerPhone, customerAddress,
							paymentMethod, orderDate));
				} else {
					// If the order exists, add the CartItem to the existing order
					order.getCartItems().add(cartItem);
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL error occurred: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		} finally {
			// Close resources
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.err.println("Failed to close resources: " + e.getMessage());
			}
		}
		return orders;
	}

	public List<Product> getListProduct(int orderDetailID) {
		String query = "SELECT p.ProductID, p.Name, p.Price, p.ImageURL " + "FROM Products p "
				+ "JOIN OrderDetails op ON p.ProductID = op.ProductID " + "WHERE op.OrderDetailID = ?";

		List<Product> products = new ArrayList<>();

		try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setInt(1, orderDetailID);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Product product = new Product(rs.getInt("ProductID"), rs.getString("Title"), rs.getString("Name"),
							rs.getString("Description"), rs.getString("Price"), rs.getString("ImageURL"),
							rs.getString("Gender"));
					products.add(product);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return products;
	}

	public List<DeletedProduct> getRecentlyDeletedProducts() {
		List<DeletedProduct> deletedProducts = new ArrayList<>();
		String query = "SELECT product_id, product_name, deleted_by, deleted_at FROM Deleted_products ORDER BY deleted_at DESC"; // Sắp
																																	// //
																																	// x
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

	public Timestamp getOrderDateById(int orderId) {
		String query = "SELECT OrderDate FROM Orders1 WHERE OrderID = ?";
		Timestamp orderDate = null; // Initialize the order date variable

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = new DBContext().getConnection(); // Assuming DBContext manages database connections
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, orderId); // Set the OrderID parameter

			rs = stmt.executeQuery(); // Execute the query

			// If a result is found, get the order date
			if (rs.next()) {
				orderDate = rs.getTimestamp("OrderDate");
			}
		} catch (SQLException e) {
			System.err.println("SQL error occurred: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		} finally {
			// Close resources
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.err.println("Failed to close resources: " + e.getMessage());
			}
		}

		return orderDate; // Return the order date or null if not found
	}

	public void insertOrder(List<CartItem> cartItems, String customerName, String customerEmail, String customerPhone,
			String customerAddress, String paymentMethod) {
		String orderQuery = "INSERT INTO Orders1 (CustomerName, CustomerEmail, CustomerPhone, CustomerAddress, PaymentMethod, OrderDate) VALUES (?, ?, ?, ?, ?, ?)";
		String orderItemQuery = "INSERT INTO OrderItems1 (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";

		try (Connection conn = new DBContext().getConnection()) {
			conn.setAutoCommit(false); // Start transaction

// Insert customer information and get the generated order ID
			try (PreparedStatement orderStmt = conn.prepareStatement(orderQuery,
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				orderStmt.setString(1, customerName);
				orderStmt.setString(2, customerEmail);
				orderStmt.setString(3, customerPhone);
				orderStmt.setString(4, customerAddress);
				orderStmt.setString(5, paymentMethod);
				orderStmt.setTimestamp(6, new Timestamp(System.currentTimeMillis())); // Set current date

				int affectedRows = orderStmt.executeUpdate();

				if (affectedRows == 0) {
					throw new SQLException("Inserting order failed, no rows affected.");
				}

// Retrieve the generated order ID
				try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						long orderId = generatedKeys.getLong(1); // Get the order ID

						// Insert each item into the OrderItems1 table
						insertOrderItems(conn, orderId, cartItems);
						conn.commit(); // Commit transaction
						System.out.println("Order and order items successfully inserted.");
					} else {
						throw new SQLException("Inserting order failed, no ID obtained.");
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL error occurred: " + e.getMessage());
			//rollbackConnection(); // Call rollback method on error
		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}

	private void insertOrderItems(Connection conn, long orderId, List<CartItem> cartItems) throws SQLException {
		String orderItemQuery = "INSERT INTO OrderItems1 (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
		try (PreparedStatement orderItemStmt = conn.prepareStatement(orderItemQuery)) {
			for (CartItem item : cartItems) {
				if (item.getProduct() == null) {
					throw new IllegalArgumentException("Product cannot be null in CartItem.");
				}

				int productId = item.getProduct().getProductID();
				int quantity = item.getQuantity();
				double price = item.getPrice(); // Assumes price is already validated

				orderItemStmt.setLong(1, orderId); // Set the order ID
				orderItemStmt.setInt(2, productId);
				orderItemStmt.setInt(3, quantity);
				orderItemStmt.setDouble(4, price);
				orderItemStmt.addBatch(); // Add to batch

				System.out.println("Adding to batch - OrderID: " + orderId + ", ProductID: " + productId
						+ ", Quantity: " + quantity + ", Price: " + price);
			}
			orderItemStmt.executeBatch(); // Execute batch insert
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
		String query = "DELETE FROM Deleted_products"; // Truy vấn để xóa tất cả dữ liệu

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
		String update_delete = "UPDATE d\r\n" + "SET\r\n" + "    product_name = p.Name \r\n"
				+ "FROM Deleted_products d\r\n" + "JOIN Products p ON d.product_id = p.ProductID\r\n"
				+ "WHERE d.product_id = '?'";

		String insertDeletedQuery = "INSERT INTO Deleted_products (product_id, product_name, deleted_by, deleted_at) VALUES (?, ?, ?, ?)";

		try {
			conn = new DBContext().getConnection();
			conn.setAutoCommit(false); // Bắt đầu giao dịch

			// Lấy thông tin sản phẩm
			ps = conn.prepareStatement(selectQuery);
			ps.setString(1, pid);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				String productName = rs.getString("Name");

				String deletedBy = username;
				java.sql.Timestamp deletedAt = new java.sql.Timestamp(System.currentTimeMillis());

				ps = conn.prepareStatement(insertDeletedQuery);
				ps.setString(1, pid);
				ps.setString(2, productName);
				ps.setString(3, deletedBy);
				ps.setTimestamp(4, deletedAt);
				int rowsInserted = ps.executeUpdate();

				// luu thong tin de khoi phuc
				// Product product = DAO.instance.getProductByID(pid);

//	            if (rowsInserted > 0) {
//	            	  ps = conn.prepareStatement(update_delete);
//	            	  ps.setString(1,pid);
//	            	  ps.executeUpdate();
//	            }

				// ps.setString(1, product.getTitle());

//		        ps.setString(3, product.getDescription());
//		        ps.setString(4, product.getPrice());
//		        ps.setString(5, product.getImageURL());
//		        ps.setString(6, product.getGender());

				if (rowsInserted > 0) {
					ps = conn.prepareStatement(update_delete);
					// Xóa sản phẩm khỏi bảng Products
					ps = conn.prepareStatement(deleteQuery);
					ps.setString(1, pid);
					ps.executeUpdate();
					conn.commit(); // Cam kết giao dịch
					System.out.println("Xoa thanh cong! " + productName);
				} else {
					conn.rollback(); // Hoàn tác giao dịch nếu không thể chèn vào bảng recent_deleted_products
					System.out.println("Khong the chen san pham!");
				}
			} else {
				System.out.println("Khong tim thay san pham : " + pid);
				return; // Nếu không tìm thấy sản phẩm, thoát khỏi phương thức
			}
		} catch (Exception e) {
			e.printStackTrace(); // In ra lỗi nếu có
		} finally {
			// Đóng các tài nguyên (connection, prepared statement) nếu cần thiết
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
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

	public void restore(String pid) {
		String checkExists = "SELECT COUNT(*) FROM Products WHERE ProductID = ?";
		String insert = "SET IDENTITY_INSERT Products ON; "
				+ "INSERT INTO Products (ProductID, title, name, description, price, ImageURL, gender) " + "SELECT "
				+ "    d.product_id, " + "    d.product_name AS title, " + "    d.product_name AS name, "
				+ "    'No description' AS description, " + "    0.0 AS price, " + "    'No image' AS ImageURL, "
				+ "    'Unknown' AS gender " + "FROM Deleted_products d " + "WHERE d.product_id = ?; " + // Sử dụng dấu
																											// hỏi cho
																											// ID sản
																											// phẩm
				"SET IDENTITY_INSERT Products OFF;";

		String delete = "DELETE FROM Deleted_products WHERE product_id = ?";

		try {
			conn = new DBContext().getConnection();
			conn.setAutoCommit(false); // Bắt đầu giao dịch

			// Kiểm tra xem sản phẩm đã tồn tại hay chưa
			try (PreparedStatement psCheck = conn.prepareStatement(checkExists)) {
				psCheck.setString(1, pid);
				ResultSet rs = psCheck.executeQuery();
				rs.next();
				int count = rs.getInt(1);

				if (count > 0) {
					System.out.println("Sản phẩm đã tồn tại trong bảng Products. Không thể khôi phục.");
					return; // Không thực hiện chèn nếu sản phẩm đã tồn tại
				}
			}

			// Chuẩn bị câu lệnh insert
			try (PreparedStatement ps = conn.prepareStatement(insert)) {
				ps.setString(1, pid); // Gán giá trị cho ID sản phẩm
				ps.executeUpdate(); // Thực thi câu lệnh insert
				System.out.println("Khôi phục sản phẩm thành công!");
			}

			// Chuẩn bị và thực thi câu lệnh delete
			try (PreparedStatement psDelete = conn.prepareStatement(delete)) {
				psDelete.setString(1, pid); // Gán giá trị cho ID sản phẩm
				psDelete.executeUpdate(); // Thực thi câu lệnh delete
				System.out.println("Đã xóa sản phẩm khỏi Deleted_products!");
			}

			conn.commit(); // Cam kết giao dịch nếu cả hai thao tác thành công

		} catch (Exception e) {
			if (conn != null) {
				try {
					conn.rollback(); // Hoàn tác giao dịch nếu có lỗi
					System.out.println("Đã hoàn tác giao dịch do lỗi.");
				} catch (SQLException sqlEx) {
					System.out.println("Lỗi trong quá trình hoàn tác: " + sqlEx.getMessage());
				}
			}
			System.out.println("Lỗi trong phương thức restore: " + e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close(); // Đảm bảo kết nối được đóng
				}
			} catch (SQLException sqlEx) {
				System.out.println("Lỗi khi đóng kết nối: " + sqlEx.getMessage());
			}
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
				return new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7));
			}

		} catch (Exception e) {
		}
		return null;
	}

//a
	public static void main(String[] args) throws Exception {
		DAO d = new DAO();

		// System.out.println(d.getUserByUsername("chia"));
		// System.out.println(""+d.insertProduct(null, null, null, null, null, null));
		// d.signUp("dung1","1");
		// d.deleteProduct(session,"3002");

		// System.out.println(d.getOrderDateById(13));
		System.out.println(d.getOrderDetailByOrderID(15));

	}
}
