package com.springbootfirstproject.ecommerceapp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String userId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private String shippingAddress;
    private String paymentMethod;

    public Order() {}

    public Order(String userId, Date orderDate, double totalAmount, String status, String shippingAddress, String paymentMethod) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}