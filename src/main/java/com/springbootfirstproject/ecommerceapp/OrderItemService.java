package com.springbootfirstproject.ecommerceapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public Optional<OrderItem> getOrderItemById(String id) {
        return orderItemRepository.findById(id);
    }

    public List<OrderItem> getOrderItemsByOrderId(String orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public List<OrderItem> getOrderItemsByProductId(String productId) {
        return orderItemRepository.findByProductId(productId);
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public void deleteOrderItem(String id) {
        orderItemRepository.deleteById(id);
    }
}