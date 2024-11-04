package entity;

public class OrderInfoCustomer {
	private String infoID;
	private String orderDetailID;
	private String name;
	private String phoneNumber;
	private String email;
	private String address;
	public OrderInfoCustomer(String infoID,String orderDetailID,String name, String phoneNumber, String email, String address) {
		this.infoID = infoID;
		this.orderDetailID = orderDetailID;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
	}
	
	public String getInfoID() {
		return infoID;
	}
	public void setInfoID(String infoID) {
		this.infoID = infoID;
	}
	public String getOrderDetailID() {
		return orderDetailID;
	}
	public void setOrderDetailID(String orderDetailID) {
		this.orderDetailID = orderDetailID;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "OrderInfoCustomer [infoID=" + infoID + ", orderDetailID=" + orderDetailID + ", name=" + name
				+ ", phoneNumber=" + phoneNumber + ", email=" + email + ", address=" + address + "]";
	}
	
	
	
	

}
