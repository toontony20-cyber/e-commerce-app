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
public class PaymentTransactionService {

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initializeSampleData() throws IOException, InterruptedException {
        if (paymentTransactionRepository.count() == 0) {
            // Wait for orders to be loaded
            Thread.sleep(900);
            ClassPathResource resource = new ClassPathResource("data/payment-transactions.json");
            List<PaymentTransactionData> transactionDataList = objectMapper.readValue(resource.getInputStream(),
                new TypeReference<List<PaymentTransactionData>>() {});

            List<PaymentTransaction> transactions = transactionDataList.stream()
                .map(this::createTransactionFromData)
                .collect(Collectors.toList());

            paymentTransactionRepository.saveAll(transactions);
        }
    }

    private PaymentTransaction createTransactionFromData(PaymentTransactionData data) {
        List<Order> orders = orderRepository.findByStatus(data.getOrderStatus());
        String orderId = orders.isEmpty() ? null : orders.get(0).getId();
        return new PaymentTransaction(orderId, data.getPaymentDate(), data.getAmount(),
                                    data.getPaymentMethod(), data.getStatus());
    }

    public List<PaymentTransaction> getAllPaymentTransactions() {
        return paymentTransactionRepository.findAll();
    }

    public Optional<PaymentTransaction> getPaymentTransactionById(String id) {
        return paymentTransactionRepository.findById(id);
    }

    public List<PaymentTransaction> getPaymentTransactionsByOrderId(String orderId) {
        return paymentTransactionRepository.findByOrderId(orderId);
    }

    public List<PaymentTransaction> getPaymentTransactionsByStatus(String status) {
        return paymentTransactionRepository.findByStatus(status);
    }

    public PaymentTransaction savePaymentTransaction(PaymentTransaction paymentTransaction) {
        return paymentTransactionRepository.save(paymentTransaction);
    }

    public void deletePaymentTransaction(String id) {
        paymentTransactionRepository.deleteById(id);
    }
}