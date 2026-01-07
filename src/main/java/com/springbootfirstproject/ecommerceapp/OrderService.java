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
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initializeSampleData() throws IOException, InterruptedException {
        if (orderRepository.count() == 0) {
            // Wait for users to be loaded
            Thread.sleep(600);
            ClassPathResource resource = new ClassPathResource("data/orders.json");
            List<OrderData> orderDataList = objectMapper.readValue(resource.getInputStream(),
                new TypeReference<List<OrderData>>() {});

            List<Order> orders = orderDataList.stream()
                .map(this::createOrderFromData)
                .collect(Collectors.toList());

            orderRepository.saveAll(orders);
        }
    }

    private Order createOrderFromData(OrderData data) {
        User user = userRepository.findByEmail(data.getUserEmail()).orElse(null);
        String userId = user != null ? user.getId() : null;
        return new Order(userId, data.getOrderDate(), data.getTotalAmount(),
                        data.getStatus(), data.getShippingAddress(), data.getPaymentMethod());
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}