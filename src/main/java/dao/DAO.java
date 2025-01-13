package dao;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import context.DBContext;
import entity.AuditLog;
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

	public List<Product> getProductsByPage(int page, int pageSize) {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM Products ORDER BY ProductID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			int offset = (page - 1) * pageSize;
			stmt.setInt(1, offset);
			stmt.setInt(2, pageSize);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					products.add(new Product(rs.getInt("ProductID"), rs.getString("Title"), rs.getString("Name"),
							rs.getString("Description"), rs.getDouble("Price"), rs.getString("ImageURL"),
							rs.getString("Gender")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		return products;
	}

	public int getTotalProducts() {
		String query = "SELECT COUNT(*) FROM Products";
		try (Connection conn = new DBContext().getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		return 0;
	}
	public List<Product> getFilteredProducts(String priceRange, String gender, String searchQuery, int page, int pageSize) {
	    List<Product> products = new ArrayList<>();
	    StringBuilder query = new StringBuilder("SELECT * FROM Products WHERE 1=1");

	    // Thêm điều kiện khoảng giá
	    if (priceRange != null && !priceRange.isEmpty()) {
	        String[] prices = priceRange.split("-");
	        if (prices.length == 2) {
	            query.append(" AND Price BETWEEN ? AND ?");
	        } else if (priceRange.endsWith("-")) {
	            query.append(" AND Price >= ?");
	        }
	    }

	    // Thêm điều kiện tìm trong Description với giới tính
	    if (gender != null && !gender.isEmpty()) {
	    	 query.append(" AND Gender LIKE ?");
	    }

	    // Thêm điều kiện tìm kiếm theo tên sản phẩm
	    if (searchQuery != null && !searchQuery.isEmpty()) {
	    	 query.append(" AND Name LIKE ? ");
	    }
	   

	    // Thêm điều kiện phân trang (OFFSET và FETCH NEXT) OR Title LIKE ?
	    query.append(" ORDER BY ProductID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

	    try {
	        conn = new DBContext().getConnection();
	        ps = conn.prepareStatement(query.toString());

	        int index = 1;

	        // Gán giá trị khoảng giá
	        if (priceRange != null && !priceRange.isEmpty()) {
	            String[] prices = priceRange.split("-");
	            if (prices.length == 2) {
	                ps.setDouble(index++, Double.parseDouble(prices[0]));
	                ps.setDouble(index++, Double.parseDouble(prices[1]));
	            } else if (priceRange.endsWith("-")) {
	                ps.setDouble(index++, Double.parseDouble(priceRange.replace("-", "")));
	            }
	        }

	        // Gán giá trị tìm trong Description với giới tính
	        if (gender != null && !gender.isEmpty()) {
	            ps.setString(index++, "%" + gender + "%");
	        }

	        // Gán giá trị tìm kiếm theo tên
	        if (searchQuery != null && !searchQuery.isEmpty()) {
	            ps.setString(index++, "%" + searchQuery + "%");
	        }

	        // Gán giá trị phân trang
	        ps.setInt(index++, (page - 1) * pageSize);
	        ps.setInt(index++, pageSize);

	        rs = ps.executeQuery();
	        while (rs.next()) {
	            products.add(new Product(
	                    rs.getInt("ProductID"),
	                    rs.getString("Title"),
	                    rs.getString("Name"),
	                    rs.getString("Description"),
	                    rs.getDouble("Price"),
	                    rs.getString("ImageURL"),
	                    rs.getString("Gender")
	            ));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    return products;
	}




	public int getTotalFilteredProducts(String priceRange, String gender, String searchQuery) {
	    int total = 0;
	    StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM Products WHERE 1=1");

	    // Thêm điều kiện khoảng giá
	    if (priceRange != null && !priceRange.isEmpty()) {
	        String[] prices = priceRange.split("-");
	        if (prices.length == 2) {
	            query.append(" AND Price BETWEEN ? AND ?");
	        } else if (priceRange.endsWith("-")) {
	            query.append(" AND Price >= ?");
	        }
	    }

	    // Thêm điều kiện tìm trong Description với giới tính
	    if (gender != null && !gender.isEmpty()) {
	        query.append(" AND Gender LIKE ?");
	    }

	    // Thêm điều kiện tìm kiếm theo Name hoặc Title
	    if (searchQuery != null && !searchQuery.isEmpty()) {
	        query.append(" AND Name LIKE ? ");
	    }
	  

	    try {
	        // Kết nối đến cơ sở dữ liệu
	        conn = new DBContext().getConnection();
	        ps = conn.prepareStatement(query.toString());

	        int index = 1;

	        // Gán giá trị khoảng giá
	        if (priceRange != null && !priceRange.isEmpty()) {
	            String[] prices = priceRange.split("-");
	            if (prices.length == 2) {
	                ps.setDouble(index++, Double.parseDouble(prices[0]));
	                ps.setDouble(index++, Double.parseDouble(prices[1]));
	            } else if (priceRange.endsWith("-")) {
	                ps.setDouble(index++, Double.parseDouble(priceRange.replace("-", "")));
	            }
	        }

	        // Gán giá trị tìm trong Description với giới tính
	        if (gender != null && !gender.isEmpty()) {
	            ps.setString(index++, "%" + gender + "%");
	        }

	        // Gán giá trị tìm kiếm theo Name và Title
	        if (searchQuery != null && !searchQuery.isEmpty()) {
	            ps.setString(index++, "%" + searchQuery + "%"); // Gán cho Name
	            ps.setString(index++, "%" + searchQuery + "%"); // Gán cho Title
	        }

	        // Thực thi câu lệnh
	        rs = ps.executeQuery();
	        if (rs.next()) {
	            total = rs.getInt(1); // Lấy tổng số sản phẩm
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // Đóng tài nguyên
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    return total;
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

	public void updateOrderStatus(int orderID, String status) {
		String sql = "UPDATE Orders SET status = ? WHERE orderID = ?";
		try {
			conn = new DBContext().getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ps.setInt(2, orderID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean cancelOrder(int orderID) {
		String sql = "UPDATE Orders1 SET OrderStatus = 'cancel' WHERE orderID = ?";

		try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, orderID);
			int rowsUpdated = stmt.executeUpdate();

			return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getOrderStatusByOrderID(int orderID) {
		String sql = "SELECT OrderStatus FROM Orders1 WHERE OrderID = ?";
		String orderStatus = null;

		try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, orderID); // Thiết lập giá trị cho tham số orderID
			ResultSet rs = stmt.executeQuery();

			// Kiểm tra kết quả truy vấn
			if (rs.next()) {
				orderStatus = rs.getString("OrderStatus"); // Lấy giá trị của OrderStatus
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return orderStatus; // Trả về giá trị OrderStatus (hoặc null nếu không tìm thấy)
	}

	public Map<Integer, Boolean> getOrderEditStatus() {
		Map<Integer, Boolean> editStatusMap = new HashMap<>();
		String query = "SELECT DISTINCT OrderID FROM OrderAuditLog";

		try (Connection conn = new DBContext().getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int orderId = rs.getInt("OrderID");
				editStatusMap.put(orderId, true); // Đánh dấu đơn hàng này đã bị chỉnh sửa
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return editStatusMap;
	}

	public String getOrderSignatureByOrderID(int orderID) {
		String getOrderHashQuery = "SELECT Signature FROM Orders1 WHERE OrderID = ?";
		String signature = null;

		// Establishing a connection and executing the query
		try (Connection conn = new DBContext().getConnection();
				PreparedStatement stmt = conn.prepareStatement(getOrderHashQuery)) {

			stmt.setInt(1, orderID); // Set the OrderID parameter
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					signature = rs.getString("Signature"); // Get the signature from the result set
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Return null or handle error as needed
		}

		return signature;
	}

	public List<AuditLog> getAuditLogsForOrder(int orderId) {
		List<AuditLog> logs = new ArrayList<>();
		String query = "SELECT * FROM OrderAuditLog WHERE OrderID = ? ORDER BY ChangeTime DESC";

		try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				AuditLog log = new AuditLog();
				log.setOrderID(rs.getInt("OrderID"));
				log.setChangedColumn(rs.getString("ChangedColumn"));
				log.setOldValue(rs.getString("OldValue"));
				log.setNewValue(rs.getString("NewValue"));
				log.setChangedBy(rs.getString("ChangedBy"));
				log.setChangeTime(rs.getTimestamp("ChangeTime"));
				logs.add(log);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logs;
	}

	public boolean updateOrder(Order updatedOrder, int orderID, int userID) throws Exception {
		String updateOrderQuery = "UPDATE Orders1 SET CustomerName = ?, CustomerEmail = ?, CustomerPhone = ?, CustomerAddress = ?, PaymentMethod = ?, OrderDate = ?, Signature = ? WHERE OrderID = ?";
		String updateOrderItemQuery = "UPDATE OrderItems1 SET Quantity = ?, Price = ? WHERE OrderID = ?";
		String getOrderHashQuery = "SELECT Signature FROM Orders1 WHERE OrderID = ?";
		String getPublicKeyQuery = "SELECT PublicKey FROM KeyManagement WHERE UserID = ?"; // Query để lấy public key
		String setEditedFlagQuery = "UPDATE Orders1 SET Edited = ? WHERE OrderID = ?"; // Query to set Edited flag

		boolean isUpdated = false;
		Connection conn = null;

		try {
			conn = new DBContext().getConnection();
			conn.setAutoCommit(false); // Start transaction

			// Get current signature (hash) from the database
			String hash = getOrderHashByOrderID(orderID);

			// Get the signature from the order
			String sign = getOrderSignatureByOrderID(orderID);

			// Get the public key from KeyManagement based on UserID
			String publicKeyString = getPublicKeyByUserID(userID);

			// Decode the signature using the public key
			String decodedSignature = decodeSignature(sign, publicKeyString);

			// Check if the signature is valid (if needed)
			// if (!isSignatureValid(decodedSignature, updatedOrder)) {
			// throw new Exception("Invalid signature. The order data has been tampered
			// with.");
			// }

			// Update order information and order items
			try (PreparedStatement orderStmt = conn.prepareStatement(updateOrderQuery);
					PreparedStatement orderItemStmt = conn.prepareStatement(updateOrderItemQuery)) {

				// Update main order information
				orderStmt.setString(1, updatedOrder.getCustomerName());
				orderStmt.setString(2, updatedOrder.getCustomerEmail());
				orderStmt.setString(3, updatedOrder.getCustomerPhone());
				orderStmt.setString(4, updatedOrder.getCustomerAddress());
				orderStmt.setString(5, updatedOrder.getPaymentMethod());
				orderStmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
				orderStmt.setString(7, updatedOrder.getSign());
				orderStmt.setInt(8, orderID);

				int orderRowsUpdated = orderStmt.executeUpdate();

				// Update order items
				for (CartItem item : updatedOrder.getCartItems()) {
					orderItemStmt.setInt(1, item.getQuantity());
					orderItemStmt.setDouble(2, item.getPrice());
					orderItemStmt.setInt(3, orderID);
					orderItemStmt.addBatch(); // Add to batch
				}

				int[] orderItemsRowsUpdated = orderItemStmt.executeBatch(); // Execute batch

				// Check if the order has been modified
				boolean isEdited = !hash.equals(decodedSignature);
				if (isEdited) {
					System.out.println("Order has been edited directly in the database!");
				}

				// If the update was successful, set the Edited column to true
				if (isUpdated = orderRowsUpdated > 0
						&& orderItemsRowsUpdated.length == updatedOrder.getCartItems().size()) {
					try (PreparedStatement updateEditedStmt = conn.prepareStatement(setEditedFlagQuery)) {
						updateEditedStmt.setBoolean(1, true); // Set Edited to true
						updateEditedStmt.setInt(2, orderID);
						updateEditedStmt.executeUpdate();
					}
				}

				// Commit transaction if everything is successful
				conn.commit();
			}

		} catch (SQLException e) {
			System.err.println("SQL error occurred: " + e.getMessage());
			e.printStackTrace();
			if (conn != null) {
				conn.rollback(); // Rollback on error
			}
			throw new Exception("Database operation failed", e);

		} catch (Exception e) {
			System.err.println("Error occurred: " + e.getMessage());
			e.printStackTrace();
			if (conn != null) {
				conn.rollback(); // Rollback on error
			}
			throw new Exception("An error occurred while updating the order.", e);

		} finally {
			if (conn != null) {
				conn.close(); // Close connection
			}
		}

		return isUpdated;
	}

	public List<AuditLog> getAuditLogsForUserOrders(int userId) {
		List<AuditLog> logs = new ArrayList<>();
		String query = "SELECT al.* FROM OrderAuditLog al " + "JOIN Orders1 o ON al.OrderID = o.OrderID "
				+ "WHERE o.UserID = ? " + "ORDER BY al.ChangeTime DESC";

		try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				AuditLog log = new AuditLog();
				log.setAuditID(rs.getInt("AuditID"));
				log.setOrderID(rs.getInt("OrderID"));
				log.setChangedColumn(rs.getString("ChangedColumn"));
				log.setOldValue(rs.getString("OldValue"));
				log.setNewValue(rs.getString("NewValue"));
				log.setChangedBy(rs.getString("ChangedBy"));
				log.setChangeTime(rs.getTimestamp("ChangeTime"));
				logs.add(log);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logs;
	}

	public List<AuditLog> getAuditLogs(int orderId) {
		List<AuditLog> logs = new ArrayList<>();
		String query = "SELECT * FROM OrderAuditLog WHERE OrderID = ? ORDER BY ChangeTime DESC";

		try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				AuditLog log = new AuditLog();
				log.setAuditID(rs.getInt("AuditID"));
				log.setOrderID(rs.getInt("OrderID"));
				log.setChangedColumn(rs.getString("ChangedColumn"));
				log.setOldValue(rs.getString("OldValue"));
				log.setNewValue(rs.getString("NewValue"));
				log.setChangedBy(rs.getString("ChangedBy"));
				log.setChangeTime(rs.getTimestamp("ChangeTime"));
				logs.add(log);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logs;
	}

	public String decodeSignature(String signature, String publicKeyString) throws Exception {
		if (signature == null || publicKeyString == null) {
			throw new IllegalArgumentException("Signature or public key cannot be null");
		}

		try {
			// Convert public key from string to PublicKey object
			PublicKey publicKey = getPublicKeyFromString(publicKeyString);

			// Initialize cipher for decryption with RSA
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);

			// Decode the base64 encoded signature
			byte[] decodedBytes = Base64.getDecoder().decode(signature);

			// Decrypt the signature
			byte[] decryptedBytes = cipher.doFinal(decodedBytes);

			// Create hash of the decrypted data (SHA-256)
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(decryptedBytes);

			return bytesToHex(hashBytes);
		} catch (BadPaddingException e) {
			throw new BadPaddingException("Decryption failed: likely due to invalid padding or mismatched key.");
		} catch (InvalidKeyException e) {
			throw new InvalidKeyException("Invalid public key for decryption.");
		} catch (IllegalBlockSizeException e) {
			throw new IllegalBlockSizeException("Decryption failed: block size mismatch.");
		} catch (NoSuchAlgorithmException e) {
			throw new NoSuchAlgorithmException("SHA-256 or RSA algorithm not found.");
		} catch (Exception e) {
			throw new Exception("Failed to decode the signature: " + e.getMessage(), e);
		}
	}

	private PublicKey getPublicKeyFromString(String publicKeyString) throws Exception {
		// Assuming you have a method to get the public key from a string
		// Example:
		byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private boolean isSignatureValid(String decodedSignature, Order updatedOrder) throws NoSuchAlgorithmException {
		// Kiểm tra chữ ký hợp lệ, ví dụ so sánh với hash mới của đơn hàng
		String newHash = generateHash(updatedOrder);
		return decodedSignature.equals(newHash); // Nếu chữ ký giải mã trùng với hash mới của đơn hàng
	}

	/**
	 * Tạo chữ ký hash cho đơn hàng.
	 */
	private String generateHash(Order order) throws NoSuchAlgorithmException {
		// Chuẩn bị dữ liệu
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append(order.getCustomerName()).append(order.getCustomerEmail()).append(order.getCustomerPhone())
				.append(order.getCustomerAddress()).append(order.getPaymentMethod()).append(order.getOrderDate());

		// Thêm thông tin CartItems
		for (CartItem item : order.getCartItems()) {
			dataBuilder.append(item.getQuantity()).append(item.getPrice());
		}

		// Tạo MessageDigest với SHA-256
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashBytes = digest.digest(dataBuilder.toString().getBytes(StandardCharsets.UTF_8));

		// Chuyển đổi hash thành chuỗi hex
		StringBuilder hexString = new StringBuilder();
		for (byte b : hashBytes) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}

		return hexString.toString();
	}

	public String getOrderHashByOrderID(int orderID) {
		String query = "SELECT OrderHash FROM Orders1 WHERE OrderID = ?";

		try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, orderID); // Đặt OrderID vào câu truy vấn
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getString("OrderHash"); // Trả về giá trị OrderHash
				} else {
					throw new SQLException("No OrderHash found for OrderID: " + orderID);
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL error occurred: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
		return null; // Trả về null nếu xảy ra lỗi
	}

	public boolean isEdited(Order order, String publicKey, int orderID) {
		try {
			String currentHash = getOrderHashByOrderID(orderID);
			System.out.println("hash1: " + currentHash);// Hàm generateHash đã được định nghĩa

			System.out.println("hash2: " + decodeSignature(order.getSign(), publicKey));

			return !currentHash.equals(decodeSignature(order.getSign(), publicKey)); // So sánh với hash ban đầu
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void deleteOrder(String orderID, String username) throws Exception {
		String deleteQuery = "DELETE FROM Orders1 WHERE OrderID = ?";

		// Establish database connection
		try (Connection conn = new DBContext().getConnection();
				PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

			// Set the OrderID parameter
			stmt.setString(1, orderID);

			// Execute the DELETE query
			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Order with ID " + orderID + " has been deleted successfully.");
			} else {
				System.out.println("No order found with ID " + orderID);
			}
		} catch (Exception e) {
			System.err.println("SQL error occurred: " + e.getMessage());
		}
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
				order = new Order(orderID, new ArrayList<>(), customerName, customerEmail, customerPhone,
						customerAddress, paymentMethod, orderDate);

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

	public List<Order> getOrdersByPage(int page, int pageSize, int userID) {

		List<Order> orders = new ArrayList<>();
		Map<Integer, Order> orderMap = new HashMap<>();

		String orderQuery = """
				    SELECT o.OrderID, o.CustomerName, o.CustomerEmail,
				           o.CustomerPhone, o.CustomerAddress, o.PaymentMethod,
				           o.OrderDate, o.Signature, o.Edited, o.StatusReport,
				           oi.ProductID, oi.Quantity, oi.Price
				    FROM Orders1 o
				    JOIN OrderItems1 oi ON o.OrderID = oi.OrderID
				    ORDER BY o.OrderDate DESC, o.OrderID DESC
				    OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
				""";

		try (Connection conn = new DBContext().getConnection();
				PreparedStatement stmt = conn.prepareStatement(orderQuery)) {

			int offset = (page - 1) * pageSize; // Tính OFFSET
			stmt.setInt(1, offset);
			stmt.setInt(2, pageSize);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					// Lấy thông tin đơn hàng
					int orderID = rs.getInt("OrderID");
					String customerName = rs.getString("CustomerName");
					String customerEmail = rs.getString("CustomerEmail");
					String customerPhone = rs.getString("CustomerPhone");
					String customerAddress = rs.getString("CustomerAddress");
					String paymentMethod = rs.getString("PaymentMethod");
					Timestamp orderDate = rs.getTimestamp("OrderDate");
					String signature = rs.getString("Signature");
					boolean edited = rs.getBoolean("Edited");
					String statusReport = rs.getString("StatusReport");
					String key = getPublicKeyByUserID(userID);
					System.out.println("public key : " + key);
					// Lấy thông tin sản phẩm
					String productId = rs.getString("ProductID");
					int quantity = rs.getInt("Quantity");
					double price = rs.getDouble("Price");

					// Tạo đối tượng CartItem
					CartItem cartItem = new CartItem(getProductByID(productId), quantity);

					// Kiểm tra nếu đơn hàng đã tồn tại trong map
					Order order = orderMap.get(orderID);

					if (order == null) {
						List<CartItem> items = new ArrayList<>();
						items.add(cartItem);
						order = new Order(orderID, items, customerName, customerEmail, customerPhone, customerAddress,
								paymentMethod, orderDate, signature, edited,statusReport);

						orders.add(order);
						orderMap.put(orderID, order);
					} else {
						order.getCartItems().add(cartItem);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		return orders;
	}

	public String getPublicKeyByUserID(int userID) {
		String query = "SELECT PublicKey FROM KeyManagement WHERE UserID = ?";

		try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, userID); // Đặt UserID vào câu truy vấn
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getString("PublicKey"); // Trả về giá trị PublicKey
				} else {
					throw new SQLException("No PublicKey found for UserID: " + userID);
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL error occurred: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
		return null; // Trả về null nếu xảy ra lỗi
	}

	public int getTotalOrders() {
		String query = "SELECT COUNT(*) FROM Orders1";
		try (Connection conn = new DBContext().getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		return 0;
	}

	public List<Order> getHisOrders(int userId) {

		List<Order> orders = new ArrayList<>();
		Map<Integer, Order> orderMap = new HashMap<>(); // Để theo dõi đơn hàng theo OrderID

		// Cập nhật câu truy vấn SQL để lọc theo userId
		String orderQuery = "SELECT o.OrderID, o.CustomerName, o.CustomerEmail, "
				+ "o.CustomerPhone, o.CustomerAddress, o.PaymentMethod, " + "o.OrderDate, o.Signature,o.Edited,o.StatusReport,"
				+ "oi.ProductID, oi.Quantity, oi.Price " + "FROM Orders1 o "
				+ "JOIN OrderItems1 oi ON o.OrderID = oi.OrderID " + "WHERE o.UserID = ?"; // Thêm điều kiện lọc theo
																							// UserID

		try (Connection conn = new DBContext().getConnection();
				PreparedStatement stmt = conn.prepareStatement(orderQuery)) {

			// Thiết lập tham số userId vào câu lệnh SQL
			stmt.setInt(1, userId); // Truyền userId vào câu truy vấn

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int orderID = rs.getInt("OrderID");
					String customerName = rs.getString("CustomerName");
					String customerEmail = rs.getString("CustomerEmail");
					String customerPhone = rs.getString("CustomerPhone");
					String customerAddress = rs.getString("CustomerAddress");
					String paymentMethod = rs.getString("PaymentMethod");
					Timestamp orderDate = rs.getTimestamp("OrderDate"); // Lấy trực tiếp từ kết quả
					String signature = rs.getString("Signature");
					boolean edited = rs.getBoolean("Edited");
					String statusReport = rs.getString("StatusReport");

					// Lấy thông tin sản phẩm
					String productId = rs.getString("ProductID"); // Giả sử ProductID là một chuỗi
					int quantity = rs.getInt("Quantity");
					double price = rs.getDouble("Price");

					// Tạo CartItem cho sản phẩm này
					CartItem cartItem = new CartItem(getProductByID(productId), quantity);

					// Kiểm tra xem đơn hàng đã tồn tại trong map chưa
					Order order = orderMap.get(orderID);
					if (order == null) {
						// Nếu đơn hàng chưa tồn tại, tạo đơn hàng mới
						List<CartItem> items = new ArrayList<>();
						items.add(cartItem);
						order = new Order(orderID, items, customerName, customerEmail, customerPhone, customerAddress,
								paymentMethod, orderDate, signature, edited,statusReport);
						orders.add(order);
						orderMap.put(orderID, order); // Thêm đơn hàng mới vào map
					} else {
						// Nếu đơn hàng đã tồn tại, chỉ cần thêm CartItem vào đơn hàng
						order.getCartItems().add(cartItem);
					}
				}
			}

		} catch (SQLException e) {
			System.err.println("SQL error occurred: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
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
							rs.getString("Description"), rs.getDouble("Price"), rs.getString("ImageURL"),
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

	public User getUserByID(int id) {
		String query = "SELECT * FROM Users WHERE UserID = ?";
		try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, id);
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

	public boolean insertOrder(List<CartItem> cartItems, String customerName, String customerEmail,
			String customerPhone, String customerAddress, String paymentMethod, int userID) {

		String orderQuery = "INSERT INTO Orders1 (CustomerName, CustomerEmail, CustomerPhone, CustomerAddress, PaymentMethod, OrderDate, UserID, Edited) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		String orderItemQuery = "INSERT INTO OrderItems1 (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";

		try (Connection conn = new DBContext().getConnection()) {
			conn.setAutoCommit(false); // Start transaction

			try (PreparedStatement orderStmt = conn.prepareStatement(orderQuery,
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				orderStmt.setString(1, customerName);
				orderStmt.setString(2, customerEmail);
				orderStmt.setString(3, customerPhone);
				orderStmt.setString(4, customerAddress);
				orderStmt.setString(5, paymentMethod);
				orderStmt.setTimestamp(6, new Timestamp(System.currentTimeMillis())); // Current timestamp
				orderStmt.setInt(7, userID);
				orderStmt.setBoolean(8, false);

				int affectedRows = orderStmt.executeUpdate();

				if (affectedRows == 0) {
					throw new SQLException("Inserting order failed, no rows affected.");
				}

				// Get the generated OrderID
				try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						long orderId = generatedKeys.getLong(1); // Retrieve OrderID

						// Insert each item into OrderItems1 table
						insertOrderItems(conn, orderId, cartItems);
						conn.commit(); // Commit transaction
						System.out.println("Order and order items successfully inserted.");
						return true;
					} else {
						throw new SQLException("Inserting order failed, no ID obtained.");
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL error occurred: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}

		return false;
	}

	private String getPublicKeyFromUserID(Connection conn, int userID) throws SQLException {
		String query = "SELECT PublicKey FROM KeyManagement WHERE UserID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, userID);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getString("PublicKey"); // Trả về khóa công khai
				}
			}
		}
		return null; // Trả về null nếu không tìm thấy
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
			ps.setString(5, image);
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
				return new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5),
						rs.getString(6), rs.getString(7));
			}

		} catch (Exception e) {
		}
		return null;
	}

	public boolean isEmailVerified(int userId) throws Exception {
		boolean isVerified = false;
		String sql = "SELECT emailVerified FROM users WHERE userId = ?";

		try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId); // Gắn userId vào câu truy vấn
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				isVerified = rs.getBoolean("emailVerified"); // Lấy giá trị xác minh email từ kết quả truy vấn
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isVerified; // Trả về trạng thái xác minh email
	}

	public boolean verifyEmail(int userId) throws Exception {
		String sql = "UPDATE users SET emailVerified = 1 WHERE userId = ?";

		try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			int rowsUpdated = stmt.executeUpdate();

			return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void updateEmailVerified(int userId) throws Exception {
		String sql = "UPDATE users SET emailVerified = 0 WHERE UserID = ?";

		try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId); // Gắn giá trị cho id người dùng

			int rowsUpdated = stmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Email verification set to false successfully.");
			} else {
				System.out.println("User not found or update failed.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

//a
	public static void main(String[] args) throws Exception {
		DAO d = new DAO();

		// System.out.println(d.getUserByUsername("chia"));
		// System.out.println(""+d.insertProduct(null, null, null, null, null, null));
		// d.signUp("dung1","1");
		// d.deleteProduct(session,"3002");

		// System.out.println(d.getOrderDateById(13));
		List<CartItem> cartItems = new ArrayList<CartItem>();
		String signAf = "cKKu6Rut++GF+w++OyEDCjxdNmRTnR/4uQtHT4JjOJxuoQGI4cf9V/xbR1vtuOcenj12a5KS7c680qs7hwnIN4mMiH6gj56oZr0tqgOx9zwdP3afdlld2T1PNBhfq9g3RsrDsm+Svcv+WFHmM1K+MXmz///IweGoZXZi1I8V8XIiVUFCnCX39e4wFFVrd7r1LZF8+OSsN0wLy/2hwjJC/y54ikiOInxKO3zq7mwN3rexPopC5dWyd7oNAr83ANibWR5mnGkBmcI5XnK9LsTSWUuPB8v2+jMUOCjtl20WrsdGDt/Hft6mjLeMbyOCJ9TSrVrGw5ZzI12/LWtW33XfOA==";
		String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArkBDXCH3SloQoetjqxKO9Ey7N6WcSjlXP+Ugyx0byH2/1JlOHli/xgRfnrxeQ+asFG3WCRK2jImeN3Qg4n+MHvH1PQpapWaahf6B0FvNYnyQRKZF/5lZRVip8DKQ+GKZVQZIpZcbY9xWLlaBxJ6UD+Ldehecy4JA4llOoWxfk9jTpQXHA0HY4ty3nWeBfIx+/PBtIBdhlH3TIeaKqZ4dQ3T++LhMcWXteW49Gy0wCMk8XuXcFHH1LvFD5tpoIRKikjXb39HV0k89YfCgUg3Hh4fInXutKXXsEw9Gnitfw0suiRbEuXwvn4ndqaAbGT9v4IBostaWV3XuqYk/bSWEawIDAQAB";
		String signO = "nN/4SpYuDElUATudHNGtEf/+yCZLwupl+TZJosKx6cPxsigeBC5Ss9btwiEHvnkTDpVbcGFHPWbStUOPBJZVBoChp3bEmOAGs4zahkvL3p5CSEakdqmC6SsVqHPxv1/yHFkaGiKBoQ4bf5Q99lWku+h4cc9lsUxwJhrNuhohdd+LXIHVUIEOW1AKPmMskcBRI/7oDnc6I7nlRWgcYncjb67qbBuLdjMrXwRX9l6KdUpsuE+8pIN7sJiVhJtgq1gG3xhSo0PjhOiutyU3J2qDoeYnyFatm5Lv+oG1fv5C1rmIOlfmEIrV3eshKUlTJyhxoUiptlSodD6z1XFfw7UxYQ==";
<<<<<<< HEAD
		Order o = new Order(9064, cartItems, "dunggghhh", "a@gmail.com", "123", "123", "credit-card",
				new Timestamp(System.currentTimeMillis()), signO, true);
=======
		Order o = new Order(9064, cartItems, "dunggghhh", "a@gmail.com", "123", "123", "credit-card", new Timestamp(System.currentTimeMillis()), signO,
				true,"valid");
>>>>>>> main

		// System.out.println(d.getOrderStatusByOrderID(7054));

		// System.out.println(d.getOrdersByPage(1,5));

		// Order o = new Order(5048, cartItems, "a", "a", "a", "a", "a", new
		// Timestamp(System.currentTimeMillis()), "a");
		// System.out.println(d.updateOrder(o, 9064, 2008));
		System.out.println(d.isEdited(o, pubKey, 9064));

		// System.out.println("de: " + d.decodeSignature(signAf, pubKey));

		// System.out.println(d.decodeSignature(signAf, pubKey));

	}

}
