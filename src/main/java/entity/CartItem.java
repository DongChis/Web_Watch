package entity;

public class CartItem {
	private Product product;
	private int quantity;
	private double price;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public CartItem(Product product, int quantity) {
		//this.orderIDItem = orderIDItem;
		this.product = product;
		this.quantity = quantity;
		
	}
	//

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

	@Override
	public String toString() {
		return "CartItem [product=" + product + ", quantity=" + quantity + ", price =" + price +"]";
	}

	
	public double getTotalPrice() {
		return product.getPrice() * quantity;
	}
	
	

}