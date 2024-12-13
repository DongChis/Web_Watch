package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public boolean savePublicKeyFile(int userId, String publicKeyFilePath, Date createTime, Date endTime) {
        // Đọc nội dung từ tệp thành chuỗi String
        String publicKeyFileContent = null;

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
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Nếu gặp lỗi khi đọc tệp, trả về false
        }

        // Kiểm tra nếu không thể đọc được nội dung từ tệp
        if (publicKeyFileContent == null || publicKeyFileContent.isEmpty()) {
            return false; // Nếu nội dung tệp rỗng hoặc null, trả về false
        }

        // Lưu vào cơ sở dữ liệu
        String sql = "INSERT INTO keys (user_id, public_key, create_time, end_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, publicKeyFileContent); // Lưu nội dung tệp dưới dạng chuỗi
            
            stmt.setTimestamp(3, new java.sql.Timestamp(createTime.getTime()));
            stmt.setTimestamp(4, new java.sql.Timestamp(endTime.getTime()));
            return stmt.executeUpdate() > 0; // Trả về true nếu lưu thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Nếu gặp lỗi trong quá trình lưu vào cơ sở dữ liệu
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
   
}
