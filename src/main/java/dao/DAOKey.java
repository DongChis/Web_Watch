package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import context.DBContext;

public class DAOKey {

    private static DAOKey instance;

    private Connection conn;
    private PreparedStatement ps;

    public DAOKey() {
    }

    public static synchronized DAOKey getInstance() {
        if (instance == null) {
            instance = new DAOKey();
        }
        return instance;
    }
    
    
    public boolean deactivateKey(Integer userId, String publicKey, LocalDateTime lossTime) {
        boolean isDeactivated = false;
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE KeyManagement SET EndTime = ?, IsActive = 0 " +
                     "WHERE UserID = ? AND PublicKey = ? AND IsActive = 1")) {

            // Thiết lập các tham số cho câu lệnh SQL
            ps.setTimestamp(1, Timestamp.valueOf(lossTime));
            ps.setInt(2, userId);
            ps.setString(3, publicKey);

            // Thực thi câu lệnh SQL
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                isDeactivated = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeactivated;
    }

    
    public boolean savePublicKeyFile(int userId, String publicKeyFilePath, Date createTime, Date endTime) {
        // Đọc nội dung từ tệp thành chuỗi String
        String publicKeyFileContent = null;
        Connection conn = null;
        PreparedStatement deletePs = null;
        PreparedStatement insertPs = null;

        try {
            // Kiểm tra nếu đường dẫn tệp là hợp lệ
            File file = new File(publicKeyFilePath);
            if (!file.exists() || !file.isFile()) {
                return false; // Nếu tệp không tồn tại hoặc không phải là tệp hợp lệ
            }

            // Đọc nội dung tệp thành chuỗi String
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                publicKeyFileContent = sb.toString();
            }

            // Kiểm tra nếu không thể đọc được nội dung từ tệp
            if (publicKeyFileContent == null || publicKeyFileContent.isEmpty()) {
                return false; // Nếu nội dung tệp rỗng hoặc null, trả về false
            }

            // Mở kết nối và bắt đầu transaction
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false); // Enable transaction management

            // Step 1: Xóa các khóa cũ của người dùng
            String deleteSql = "DELETE FROM KeyManagement WHERE UserID = ?";
            deletePs = conn.prepareStatement(deleteSql);
            deletePs.setInt(1, userId);
            deletePs.executeUpdate(); // Thực thi câu lệnh xóa

            // Step 2: Thêm khóa mới
            String insertSql = "INSERT INTO KeyManagement (UserID, PublicKey, CreateTime, EndTime, IsActive) VALUES (?, ?, ?, ?, 1)";
            insertPs = conn.prepareStatement(insertSql);
            insertPs.setInt(1, userId);

            if (publicKeyFileContent != null && !publicKeyFileContent.isEmpty()) {
                insertPs.setString(2, publicKeyFileContent);
            } else {
                throw new SQLException("Public Key is null or empty");
            }

            // Sử dụng Timestamp cho các trường Date
            if (createTime != null) {
                insertPs.setTimestamp(3, new java.sql.Timestamp(createTime.getTime()));
            } else {
                throw new SQLException("Create Time is null");
            }

            if (endTime != null) {
                insertPs.setTimestamp(4, new java.sql.Timestamp(endTime.getTime()));
            } else {
                throw new SQLException("End Time is null");
            }

            int rowsAffected = insertPs.executeUpdate();
            if (rowsAffected > 0) {
                // Commit transaction nếu insert thành công
                conn.commit();
                return true;
            } else {
                // Nếu không có dòng nào bị ảnh hưởng, rollback transaction
                conn.rollback();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback trong trường hợp có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (deletePs != null) deletePs.close();
                if (insertPs != null) insertPs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public boolean savePublicKey(int userId, String publicKey, Date createTime, Date endTime) {
        boolean isSaved = false;
        Connection conn = null;
        PreparedStatement deletePs = null;
        PreparedStatement insertPs = null;

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false); // Enable transaction management

            // Step 1: Delete existing keys for the user
            String deleteSql = "DELETE FROM KeyManagement WHERE UserID = ?";
            deletePs = conn.prepareStatement(deleteSql);
            deletePs.setInt(1, userId);
            deletePs.executeUpdate(); // Execute delete query

            // Step 2: Insert the new key
            String insertSql = "INSERT INTO KeyManagement (UserID, PublicKey, CreateTime, EndTime, IsActive) VALUES (?, ?, ?, ?, 1)";
            insertPs = conn.prepareStatement(insertSql);
            insertPs.setInt(1, userId);

            if (publicKey != null && !publicKey.isEmpty()) {
                insertPs.setString(2, publicKey);
            } else {
                throw new SQLException("Public Key is null or empty");
            }

            // Use Timestamp for Date fields
            if (createTime != null) {
                insertPs.setTimestamp(3, new java.sql.Timestamp(createTime.getTime()));
            } else {
                throw new SQLException("Create Time is null");
            }

            if (endTime != null) {
                insertPs.setTimestamp(4, new java.sql.Timestamp(endTime.getTime()));
            } else {
                throw new SQLException("End Time is null");
            }

            int rowsAffected = insertPs.executeUpdate();
            if (rowsAffected > 0) {
                isSaved = true;
            }

            conn.commit(); // Commit the transaction
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback in case of an error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            try {
                if (deletePs != null) deletePs.close();
                if (insertPs != null) insertPs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isSaved;
    }

    // Cập nhật thời gian hết hạn (endTime) khi khóa bị lộ
    public boolean reportLostKey(int userId) {
        boolean isUpdated = false;

        try {
            conn = new DBContext().getConnection();
            // Câu lệnh SQL để cập nhật endTime khi khóa bị lộ
            String sql = "UPDATE KeyManagement SET EndTime = GETDATE(), IsActive = 0 WHERE UserID = ? AND IsActive = 1";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            // Thực thi câu lệnh
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                isUpdated = true;  // Nếu dữ liệu đã được cập nhật thành công
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return isUpdated;
    }

    // Lấy PublicKey từ database
    public Map<String, String> getKeyInfo(int userId) {
        Map<String, String> keyInfo = new HashMap<>();
        String sql = "SELECT PublicKey, CreateTime, EndTime FROM KeyManagement WHERE UserID = ? AND IsActive = 1";  // Cập nhật lại truy vấn để lấy thời gian tạo và kết thúc

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);  // Thiết lập tham số cho câu lệnh SQL

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    keyInfo.put("publicKey", rs.getString("PublicKey"));  // Lấy khóa công khai
                    keyInfo.put("createTime", rs.getString("CreateTime"));  // Lấy thời gian tạo
                    keyInfo.put("endTime", rs.getString("EndTime"));  // Lấy thời gian kết thúc
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Có thể ghi log lỗi tại đây nếu cần thiết
        }

        return keyInfo;  // Trả về thông tin khóa hoặc null nếu không có kết quả
    }


    // Đóng kết nối và tài nguyên (không còn sử dụng ở đây vì đã dùng try-with-resources)
    private void closeResources() {
        try {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    public boolean saveToken(int userId, String token) throws Exception {
        String sql = "INSERT INTO Tokens (user_id, token, expiration_time) VALUES (?, ?, ?)";
        try (Connection conn =  new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Tính thời gian hết hạn (24 giờ từ thời điểm hiện tại)
            long expirationMillis = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
            Timestamp expirationTime = new Timestamp(expirationMillis);

            stmt.setInt(1, userId);
            stmt.setString(2, token);
            stmt.setTimestamp(3, expirationTime);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isTokenValid(String token) throws Exception {
        // Kiểm tra tính hợp lệ của token
        String sql = "SELECT expiration_time FROM Tokens WHERE token = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp expirationTime = rs.getTimestamp("expiration_time");
                    return expirationTime != null && expirationTime.after(new Timestamp(System.currentTimeMillis()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getUserIdByToken(String token) throws Exception {
        // Lấy userId từ token
        String sql = "SELECT user_id FROM Tokens WHERE token = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy userId
    }


public static void main(String[] args) {
	DAOKey d = new DAOKey();
	
	System.out.println(d.getKeyInfo(2008));
}
 
   
}
