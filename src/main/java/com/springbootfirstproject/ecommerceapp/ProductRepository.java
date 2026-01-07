package com.springbootfirstproject.ecommerceapp;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    // Find products by categoryId
    List<Product> findByCategoryId(String categoryId);

    // Find products by name containing (case insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Find product by exact name
    Optional<Product> findByName(String name);

    // Find products by price range
    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    // Find products with stock greater than zero
    List<Product> findByStockGreaterThan(int quantity);

    // Find products by categoryId and price range
    List<Product> findByCategoryIdAndPriceBetween(String categoryId, double minPrice, double maxPrice);
}
