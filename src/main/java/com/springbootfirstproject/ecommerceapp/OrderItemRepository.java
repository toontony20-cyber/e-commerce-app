package com.springbootfirstproject.ecommerceapp;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends MongoRepository<OrderItem, String> {

    List<OrderItem> findByOrderId(String orderId);
    List<OrderItem> findByProductId(String productId);
}