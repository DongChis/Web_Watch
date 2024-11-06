package entity;

import dao.DAO;

public class OrderItem {
	
	private String productId;
	private String quantityOrder;
	private Double totalPrice;

	public OrderItem(String productId, String quantityOrder, Double totalPrice) {
		
		this.productId = productId;
		this.quantityOrder = quantityOrder;
		this.totalPrice = totalPrice;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getQuantityOrder() {
		return quantityOrder;
	}

	public void setQuantityOrder(String quantityOrder) {
		this.quantityOrder = quantityOrder;
	}

	public Double getTotalPrice() {
		return totalPrice= Double.parseDouble(DAO.getInstance().getProductByID(productId).getPrice())
				*Double.parseDouble(quantityOrder);
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
