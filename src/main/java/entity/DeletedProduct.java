package entity;

import java.sql.Timestamp;

public class DeletedProduct {
    private int productId;
    private String productName;
    private String deletedBy;
    private Timestamp deletedAt;

    public DeletedProduct(int productId, String productName, String deletedBy, Timestamp deletedAt) {
        this.productId = productId;
        this.productName = productName;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
    }

    // Getters và Setters (có thể thêm nếu cần thiết)
    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

	@Override
	public String toString() {
		return "DeletedProduct [productId=" + productId + ", productName=" + productName + ", deletedBy=" + deletedBy
				+ ", deletedAt=" + deletedAt + "]";
	}
    
    
}
