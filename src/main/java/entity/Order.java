package entity;

import java.util.Date;
import java.util.List;

public class Order {
    private int orderID;
    private int userID;
    private double totalPrice;
    private Date createdAt;
    private List<OrderDetail> orderDetails;

    public Order(int orderID, int userID, double totalPrice, Date createdAt, List<OrderDetail> orderDetails) {
        this.orderID = orderID;
        this.userID = userID;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.orderDetails = orderDetails;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
