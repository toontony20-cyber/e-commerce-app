package com.springbootfirstproject.ecommerceapp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shopping_cart")
public class ShoppingCart {

    @Id
    private String id;
    private String userId;
    private String productId;
    private int quantity;

    public ShoppingCart() {}

    public ShoppingCart(String userId, String productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
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

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}