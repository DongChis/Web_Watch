package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // Lưu PublicKey vào database
    public boolean savePublicKey(int userId, String publicKeyBase64) {
        boolean isSaved = false;

        try {
            conn = new DBContext().getConnection();
            // Câu lệnh SQL để lưu PublicKey
            String sql = "INSERT INTO KeyManagement (UserID, PublicKey, CreateTime, IsActive) VALUES (?, ?, GETDATE(), 1)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, publicKeyBase64);

            // Thực thi câu lệnh
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                isSaved = true;  // Nếu dữ liệu đã được lưu thành công
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return isSaved;
    }

    // Lưu PrivateKey vào database
    public boolean savePrivateKey(int userId, String privateKeyBase64) {
        boolean isSaved = false;

        try {
            conn = new DBContext().getConnection();
            // Câu lệnh SQL để lưu PrivateKey
            String sql = "INSERT INTO KeyManagement (UserID, PrivateKey, CreateTime, IsActive) VALUES (?, ?, GETDATE(), 1)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, privateKeyBase64);

            // Thực thi câu lệnh
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                isSaved = true;  // Nếu dữ liệu đã được lưu thành công
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
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
