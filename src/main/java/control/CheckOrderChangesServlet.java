package control;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import context.DBContext;

/**
 * Servlet implementation class CheckOrderChangesServlet
 */
@WebServlet("/checkChanges")
public class CheckOrderChangesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM OrderChangeLog WHERE Notified = 0")) {
            
            ResultSet rs = stmt.executeQuery();
            StringBuilder changes = new StringBuilder("Các thay đổi mới:\n");

            while (rs.next()) {
                int orderId = rs.getInt("OrderID");
                String changeType = rs.getString("ChangeType");
                Timestamp changeTime = rs.getTimestamp("ChangeTime");

                changes.append("Đơn hàng ID: ").append(orderId)
                       .append(", Loại thay đổi: ").append(changeType)
                       .append(", Thời gian: ").append(changeTime).append("\n");

                // Cập nhật trạng thái đã thông báo
                try (PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE OrderChangeLog SET Notified = 1 WHERE LogID = ?")) {
                    updateStmt.setInt(1, rs.getInt("LogID"));
                    updateStmt.executeUpdate();
                }
            }

            if (changes.length() > 0) {
                response.getWriter().write(changes.toString());
                System.out.println(changes.toString());
            } else {
                response.getWriter().write("Không có thay đổi mới.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Lỗi kiểm tra thay đổi: " + e.getMessage());
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}