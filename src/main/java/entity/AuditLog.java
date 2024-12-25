package entity;

import java.sql.Timestamp;

public class AuditLog {
    private int auditID; // ID của log
    private int orderID; // Mã đơn hàng bị thay đổi
    private String changedColumn; // Tên cột bị thay đổi
    private String oldValue; // Giá trị cũ trước thay đổi
    private String newValue; // Giá trị mới sau thay đổi
    private String changedBy; // Người thực hiện thay đổi
    private Timestamp changeTime; // Thời gian thay đổi

    // Constructor mặc định
    public AuditLog() {
    }

    // Constructor đầy đủ
    public AuditLog(int auditID, int orderID, String changedColumn, String oldValue, String newValue, String changedBy, Timestamp changeTime) {
        this.auditID = auditID;
        this.orderID = orderID;
        this.changedColumn = changedColumn;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedBy = changedBy;
        this.changeTime = changeTime;
    }

    // Getter và Setter
    public int getAuditID() {
        return auditID;
    }

    public void setAuditID(int auditID) {
        this.auditID = auditID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getChangedColumn() {
        return changedColumn;
    }

    public void setChangedColumn(String changedColumn) {
        this.changedColumn = changedColumn;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    // Phương thức toString để dễ dàng in log
    @Override
    public String toString() {
        return "AuditLog{" +
                "auditID=" + auditID +
                ", orderID=" + orderID +
                ", changedColumn='" + changedColumn + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", changedBy='" + changedBy + '\'' +
                ", changeTime=" + changeTime +
                '}';
    }
}
