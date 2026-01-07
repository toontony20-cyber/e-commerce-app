package com.springbootfirstproject.ecommerceapp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "payment_transactions")
public class PaymentTransaction {

    @Id
    private String id;
    private String orderId;
    private Date paymentDate;
    private double amount;
    private String paymentMethod;
    private String status;

    public PaymentTransaction() {}

    public PaymentTransaction(String orderId, Date paymentDate, double amount, String paymentMethod, String status) {
        this.orderId = orderId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PaymentTransaction{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}