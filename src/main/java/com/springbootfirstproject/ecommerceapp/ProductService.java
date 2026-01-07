package com.springbootfirstproject.ecommerceapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initializeSampleData() throws IOException, InterruptedException {
        if (productRepository.count() == 0) {
            // Wait longer for categories to be fully loaded and saved
            Thread.sleep(1000);

            System.out.println("ProductService: Starting to load products...");
            System.out.println("ProductService: Available categories count: " + categoryRepository.count());

            ClassPathResource resource = new ClassPathResource("data/products.json");
            List<ProductData> productDataList = objectMapper.readValue(resource.getInputStream(),
                new TypeReference<List<ProductData>>() {});

            List<Product> products = productDataList.stream()
                .map(this::createProductFromData)
                .collect(Collectors.toList());

            System.out.println("ProductService: Created " + products.size() + " products");
            productRepository.saveAll(products);
            System.out.println("ProductService: Saved products to database");
        } else {
            // Fix existing products with null categoryIds
            fixExistingProducts();
        }
    }

    private void fixExistingProducts() {
        System.out.println("ProductService: Checking for products with null categoryIds...");
        List<Product> allProducts = productRepository.findAll();
        boolean needsUpdate = false;

        for (Product product : allProducts) {
            if (product.getCategoryId() == null) {
                // Try to find the category by matching product name to our known data
                String categoryName = getCategoryNameForProduct(product.getName());
                if (categoryName != null) {
                    Category category = categoryRepository.findByName(categoryName).orElse(null);
                    if (category != null) {
                        product.setCategoryId(category.getId());
                        needsUpdate = true;
                        System.out.println("ProductService: Fixed categoryId for product: " + product.getName());
                    }
                }
            }
        }

        if (needsUpdate) {
            productRepository.saveAll(allProducts);
            System.out.println("ProductService: Updated products with correct categoryIds");
        }
    }

    private String getCategoryNameForProduct(String productName) {
        // Map product names to category names based on our JSON data
        if (productName.contains("iPhone") || productName.contains("Samsung") || productName.contains("Huawei")) {
            return "Smartphones";
        } else if (productName.contains("MacBook") || productName.contains("Dell")) {
            return "Laptops";
        } else if (productName.contains("Raspberry") || productName.contains("QPC")) {
            return "Computers";
        } else if (productName.contains("Nokia")) {
            return "Feature Phones";
        }
        return null;
    }

    private Product createProductFromData(ProductData data) {
        Category category = categoryRepository.findByName(data.getCategoryName()).orElse(null);
        String categoryId = category != null ? category.getId() : null;
        return new Product(data.getName(), data.getDescription(), data.getPrice(),
                          data.getStock(), categoryId, data.getImageUrl());
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}
