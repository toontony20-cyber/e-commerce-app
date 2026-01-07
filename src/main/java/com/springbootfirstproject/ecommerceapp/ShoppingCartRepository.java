package com.springbootfirstproject.ecommerceapp;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {

    List<ShoppingCart> findByUserId(String userId);
    List<ShoppingCart> findByProductId(String productId);
}