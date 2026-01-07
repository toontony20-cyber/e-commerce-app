package com.springbootfirstproject.ecommerceapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initializeSampleData() throws IOException, InterruptedException {
        if (shoppingCartRepository.count() == 0) {
            // Wait for users and products to be loaded
            Thread.sleep(800);
            ClassPathResource resource = new ClassPathResource("data/shopping-cart.json");
            List<ShoppingCartData> cartDataList = objectMapper.readValue(resource.getInputStream(),
                new TypeReference<List<ShoppingCartData>>() {});

            List<ShoppingCart> cartItems = cartDataList.stream()
                .map(this::createCartItemFromData)
                .collect(Collectors.toList());

            shoppingCartRepository.saveAll(cartItems);
        }
    }

    private ShoppingCart createCartItemFromData(ShoppingCartData data) {
        User user = userRepository.findByEmail(data.getUserEmail()).orElse(null);
        String userId = user != null ? user.getId() : null;

        Product product = productRepository.findByName(data.getProductName()).orElse(null);
        String productId = product != null ? product.getId() : null;

        return new ShoppingCart(userId, productId, data.getQuantity());
    }

    public List<ShoppingCart> getAllShoppingCarts() {
        return shoppingCartRepository.findAll();
    }

    public Optional<ShoppingCart> getShoppingCartById(String id) {
        return shoppingCartRepository.findById(id);
    }

    public List<ShoppingCart> getShoppingCartsByUserId(String userId) {
        return shoppingCartRepository.findByUserId(userId);
    }

    public List<ShoppingCart> getShoppingCartsByProductId(String productId) {
        return shoppingCartRepository.findByProductId(productId);
    }

    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    public void deleteShoppingCart(String id) {
        shoppingCartRepository.deleteById(id);
    }
}