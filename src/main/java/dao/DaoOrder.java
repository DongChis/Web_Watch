package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import context.DBContext;
import entity.Order;

public class DaoOrder {

    private Connection connection;

    public DaoOrder() throws Exception {
        // Khởi tạo kết nối cơ sở dữ liệu
        this.connection = new DBContext().getConnection();
    }

    /**
     * Lấy danh sách đơn hàng xảy ra sau một thời điểm nhất định.
     * 
     * @param userId   ID của người dùng
     * @param lossTime Thời gian bắt đầu truy vấn
     * @return Danh sách các đơn hàng
     * @throws SQLException
     */
    public List<Order> getOrdersAfterTime(Integer userId, LocalDateTime lossTime) throws SQLException {
        List<Order> orders = new ArrayList<>();

        String query = "SELECT OrderID, CustomerName, CustomerEmail, CustomerPhone, CustomerAddress, PaymentMethod, OrderDate, OrderStatus, Edited " +
                       "FROM Orders1 WHERE UserID = ? AND OrderDate > ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setTimestamp(2, Timestamp.valueOf(lossTime));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order();
                    order.setOrderID(resultSet.getInt("OrderID"));
                    order.setCustomerName(resultSet.getString("CustomerName"));
                    order.setCustomerEmail(resultSet.getString("CustomerEmail"));
                    order.setCustomerPhone(resultSet.getString("CustomerPhone"));
                    order.setCustomerAddress(resultSet.getString("CustomerAddress"));
                    order.setPaymentMethod(resultSet.getString("PaymentMethod"));
                    order.setDate(resultSet.getTimestamp("OrderDate"));
                    order.setOrderStatus(resultSet.getString("OrderStatus"));
                    order.setEdited(resultSet.getBoolean("Edited"));
                    orders.add(order);
                }
            }
        }

        return orders;
    }
    
    public boolean updateOrdersToInvalid(int userId, LocalDateTime lossTime) {
        String sql = "UPDATE Orders1 " +
                     "SET StatusReport = 'Invalid' " +
                     "WHERE UserId = ? AND OrderDate > ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(lossTime));
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * Lấy thông tin chi tiết của một đơn hàng dựa vào OrderID.
     * 
     * @param orderId ID của đơn hàng
     * @return Đối tượng Order chứa thông tin chi tiết
     * @throws SQLException
     */
    public Order getOrderDetailByOrderID(int orderId) throws SQLException {
        String query = "SELECT OrderID, CustomerName, CustomerEmail, CustomerPhone, CustomerAddress, PaymentMethod, OrderDate, OrderStatus, Edited " +
                       "FROM Orders1 WHERE OrderID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Order order = new Order();
                    order.setOrderID(resultSet.getInt("OrderID"));
                    order.setCustomerName(resultSet.getString("CustomerName"));
                    order.setCustomerEmail(resultSet.getString("CustomerEmail"));
                    order.setCustomerPhone(resultSet.getString("CustomerPhone"));
                    order.setCustomerAddress(resultSet.getString("CustomerAddress"));
                    order.setPaymentMethod(resultSet.getString("PaymentMethod"));
                    order.setDate(resultSet.getTimestamp("OrderDate"));
                    order.setOrderStatus(resultSet.getString("OrderStatus"));
                    order.setEdited(resultSet.getBoolean("Edited"));
                    return order;
                }
            }
        }

        return null; // Trả về null nếu không tìm thấy đơn hàng
    }

    /**
     * Cập nhật trạng thái của một đơn hàng.
     * 
     * @param orderId     ID của đơn hàng
     * @param newStatus   Trạng thái mới của đơn hàng
     * @return true nếu cập nhật thành công, false nếu thất bại
     * @throws SQLException
     */
    public boolean updateOrderStatus(int orderId, String newStatus) throws SQLException {
        String query = "UPDATE Orders1 SET OrderStatus = ? WHERE OrderID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus);
            statement.setInt(2, orderId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Đóng kết nối cơ sở dữ liệu.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



