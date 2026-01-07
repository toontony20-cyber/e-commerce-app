package com.springbootfirstproject.ecommerceapp;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction, String> {

    List<PaymentTransaction> findByOrderId(String orderId);
    List<PaymentTransaction> findByStatus(String status);
}