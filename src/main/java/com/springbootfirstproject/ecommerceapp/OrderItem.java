package com.springbootfirstproject.ecommerceapp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order_items")
public class OrderItem {

    @Id
    private String id;
    private String orderId;
    private String productId;
    private int quantity;
    private double price;

    public OrderItem() {}

    public OrderItem(String orderId, String productId, int quantity, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}