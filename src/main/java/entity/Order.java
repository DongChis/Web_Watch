package entity;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import context.DBContext;
import dao.DAO;

public class Order {
	


	private int orderID;
    List<CartItem> cartItems;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    private String paymentMethod;
    private Timestamp  orderDate;    
    private String  sign;
    
    private String orderStatus;
    
    private boolean edited ;
    
    private String cancel = "Đơn hàng đã bị hủy";
   

	public String getCancel() {
		return cancel;
	}
	public void setCancel(String cancel) {
		this.cancel = cancel;
	}
	public boolean isEdited() {
		return edited;
	}
	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	public Order(int orderID, List<CartItem> cartItems, String customerName, String customerEmail, String customerPhone,
			String customerAddress, String paymentMethod, Timestamp  orderDate) {
		this.orderID = orderID;
		this.cartItems = cartItems;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.customerPhone = customerPhone;
		this.customerAddress = customerAddress;
		this.paymentMethod = paymentMethod;
		this.orderDate = orderDate;
		
		
	}
	public Order() {
		
	}

	public Order(int orderID, List<CartItem> cartItems, String customerName, String customerEmail, String customerPhone,
			String customerAddress, String paymentMethod, Timestamp  orderDate,String sign, boolean edited) {
		this.orderID = orderID;
		this.cartItems = cartItems;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.customerPhone = customerPhone;
		this.customerAddress = customerAddress;
		this.paymentMethod = paymentMethod;
		this.orderDate = orderDate;
		this.sign = sign;
		this.edited = edited;
		
	}
	
	
	
	public boolean getIsEdited(int orderID) {
        String query = "SELECT Edited FROM Orders1 WHERE OrderID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Thiết lập giá trị cho tham số trong câu lệnh SQL
            stmt.setInt(1, orderID);

            // Thực thi câu lệnh truy vấn
            ResultSet rs = stmt.executeQuery();

            // Kiểm tra kết quả truy vấn
            if (rs.next()) {
                // Lấy giá trị cột "Edited" từ cơ sở dữ liệu
                this.edited = rs.getBoolean("Edited");
            }

        } catch (SQLException e) {
            System.err.println("SQL error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        return this.edited;
    }
	
	 public String getOrderStatus() {
		    // Tính toán trạng thái đơn hàng dựa trên thời gian đặt hàng
		    LocalDateTime orderTime = orderDate.toLocalDateTime();
		    LocalDateTime now = LocalDateTime.now();
		    Duration duration = Duration.between(orderTime, now);
		    
		    if (duration.toMinutes() > 5) {
		        return "Hoàn tất"; // Hoàn tất
		    } else {   
		       return "process" ; // Đang xử lý
		    }
		    
		    
		}
	 
	 
	 
	 public boolean canCancelOrder(int orderID) throws Exception {
		    Order order = DAO.getInstance().getOrderDetailByOrderID(orderID);
		    return "Đang xử lý".equals(order.getOrderStatus());
		}
	 
	 public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}
	
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public List<CartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Timestamp getOrderDate() {
		return DAO.getInstance().getOrderDateById(orderID);
	}
	public void setDate(Timestamp  orderDate) {
		this.orderDate = orderDate;
	}
	
	
	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", cartItems=" + cartItems + ", customerName=" + customerName
				+ ", customerEmail=" + customerEmail + ", customerPhone=" + customerPhone + ", customerAddress="
				+ customerAddress + ", paymentMethod=" + paymentMethod + ", date=" + getOrderDate() +", sign = " + sign  +", edit = " + edited +"]" + "\n";
	}
    
	
	public static void main(String[] args) {
	System.out.println(new Order().edited);
	}
	
    
}
