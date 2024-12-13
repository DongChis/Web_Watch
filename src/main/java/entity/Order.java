package entity;


import java.sql.Timestamp;
import java.util.List;

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
	
	public Order(int orderID, List<CartItem> cartItems, String customerName, String customerEmail, String customerPhone,
			String customerAddress, String paymentMethod, Timestamp  orderDate,String sign) {
		this.orderID = orderID;
		this.cartItems = cartItems;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.customerPhone = customerPhone;
		this.customerAddress = customerAddress;
		this.paymentMethod = paymentMethod;
		this.orderDate = orderDate;
		this.sign = sign;
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
				+ customerAddress + ", paymentMethod=" + paymentMethod + ", date=" + getOrderDate() +", sign = " + sign +"]" + "\n";
	}
    
	
    
}
