package com.springbootfirstproject.ecommerceapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @Autowired
    private PayPalConfig payPalConfig;

    // Product endpoints
    @GetMapping("/api/products")
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/api/products")
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @GetMapping("/api/products/category/{categoryId}")
    public List<Product> getProductsByCategoryId(@PathVariable String categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

    // User endpoints
    @GetMapping("/api/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/api/users")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Category endpoints
    @GetMapping("/api/categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/api/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    // Order endpoints
    @GetMapping("/api/orders")
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/api/orders")
    public Order createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    // OrderItem endpoints
    @GetMapping("/api/order-items")
    public List<OrderItem> getOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    @PostMapping("/api/order-items")
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemService.saveOrderItem(orderItem);
    }

    // ShoppingCart endpoints
    @GetMapping("/api/shopping-cart")
    public List<ShoppingCart> getShoppingCarts() {
        return shoppingCartService.getAllShoppingCarts();
    }

    @PostMapping("/api/shopping-cart")
    public ShoppingCart createShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.saveShoppingCart(shoppingCart);
    }

    // PaymentTransaction endpoints
    @GetMapping("/api/payment-transactions")
    public List<PaymentTransaction> getPaymentTransactions() {
        return paymentTransactionService.getAllPaymentTransactions();
    }

    @PostMapping("/api/payment-transactions")
    public PaymentTransaction createPaymentTransaction(@RequestBody PaymentTransaction paymentTransaction) {
        return paymentTransactionService.savePaymentTransaction(paymentTransaction);
    }

    // Get PayPal client ID for frontend
    @GetMapping("/api/paypal/client-id")
    public ResponseEntity<?> getPayPalClientId() {
        return ResponseEntity.ok(Map.of("clientId", payPalConfig.getClientId()));
    }

    // Authentication status endpoint
    @GetMapping("/api/auth/status")
    public ResponseEntity<?> getAuthStatus(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) principal;
                String email = oauth2User.getAttribute("email");
                String name = oauth2User.getAttribute("name");

                return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "provider", "google",
                    "email", email,
                    "name", name
                ));
            }
        }

        return ResponseEntity.ok(Map.of("authenticated", false));
    }

    // Get user orders with order items
    @GetMapping("/api/user/orders")
    public ResponseEntity<?> getUserOrders(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
        }

        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");

            // Find user by email
            User user = userService.getUserByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            }

            // Get all orders for this user
            List<Order> userOrders = orderService.getOrdersByUserId(user.getId());

            // Get order items for each order
            List<Map<String, Object>> ordersWithItems = userOrders.stream()
                .map(order -> {
                    List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(order.getId());
                    Map<String, Object> orderMap = Map.of(
                        "id", order.getId(),
                        "orderDate", order.getOrderDate(),
                        "totalAmount", order.getTotalAmount(),
                        "status", order.getStatus(),
                        "shippingAddress", order.getShippingAddress(),
                        "paymentMethod", order.getPaymentMethod(),
                        "items", orderItems
                    );
                    return orderMap;
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("orders", ordersWithItems));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get orders: " + e.getMessage()));
        }
    }

    // Update order status (for payment completion)
    @PutMapping("/api/orders/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, Object> statusData, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
        }

        try {
            String status = (String) statusData.get("status");
            String paymentMethod = (String) statusData.get("paymentMethod");
            String transactionId = (String) statusData.get("transactionId");

            // Find the order
            Order order = orderService.getOrderById(orderId).orElse(null);
            if (order == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Order not found"));
            }

            // Update order status
            order.setStatus(status);
            order.setPaymentMethod(paymentMethod);

            // Save the updated order
            orderService.saveOrder(order);

            // If there's a transaction ID, create a payment transaction record
            if (transactionId != null && !transactionId.isEmpty()) {
                PaymentTransaction paymentTransaction = new PaymentTransaction(
                    orderId,
                    new Date(),
                    order.getTotalAmount(),
                    paymentMethod,
                    "completed"
                );
                paymentTransactionService.savePaymentTransaction(paymentTransaction);
            }

            return ResponseEntity.ok(Map.of("success", true, "message", "Order status updated successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update order status: " + e.getMessage()));
        }
    }

    // Checkout endpoint - converts cart items to order
    @PostMapping("/api/checkout")
    public ResponseEntity<?> checkout(@RequestBody Map<String, Object> checkoutData) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) checkoutData.get("cartItems");
            String userEmail = (String) checkoutData.get("userEmail");

            if (cartItems == null || cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
            }

            // Find or create user
            User user = userService.getUserByEmail(userEmail).orElse(null);
            if (user == null) {
                // Create guest user if not found
                user = new User("Guest User", userEmail, null, null, null);
                user = userService.saveUser(user);
            }

            // Calculate total amount
            double totalAmount = cartItems.stream()
                .mapToDouble(item -> {
                    double price = ((Number) item.get("price")).doubleValue();
                    int quantity = ((Number) item.get("quantity")).intValue();
                    return price * quantity;
                })
                .sum();

            // Create order
            Order order = new Order(user.getId(), new Date(), totalAmount, "pending", "Default Address", "pending");
            order = orderService.saveOrder(order);

            // Create order items from cart
            for (Map<String, Object> cartItem : cartItems) {
                String productId = (String) cartItem.get("id");
                Product product = productService.getAllProducts().stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst().orElse(null);

                if (product != null) {
                    double price = ((Number) cartItem.get("price")).doubleValue();
                    int quantity = ((Number) cartItem.get("quantity")).intValue();

                    OrderItem orderItem = new OrderItem(order.getId(), productId, quantity, price);
                    orderItemService.saveOrderItem(orderItem);
                }
            }

            return ResponseEntity.ok(Map.of("orderId", order.getId(), "totalAmount", totalAmount));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Checkout failed: " + e.getMessage()));
        }
    }
}
