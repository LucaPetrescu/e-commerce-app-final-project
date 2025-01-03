package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.datamodels.Order;
import com.pjsh.ecommerceapp.datamodels.OrderItem;
import com.pjsh.ecommerceapp.datamodels.Product;
import com.pjsh.ecommerceapp.datamodels.User;
import com.pjsh.ecommerceapp.exceptions.*;
import com.pjsh.ecommerceapp.repositories.OrderRepository;
import com.pjsh.ecommerceapp.repositories.ProductRepository;
import com.pjsh.ecommerceapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    //Expressions are being taken from the

    @Value("${order.discount.expression}")
    private String discountExpression;

    @Value("${order.validation.expression}")
    private String validationExpression;

    public Order placeOrder(Integer userId, List<Integer> productIds, List<Integer> quantities){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserDoesNotExistException("No user with such id exists"));

        // Validate products and create order items
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (int i = 0; i < productIds.size(); i++) {
            Integer productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            Product product = productRepository.findById(productId).orElseThrow(() -> new ProductDoesNotExistException("No product with such id exists"));

            if (product.getAvailableQuantity() < quantity) {
                throw new OutOfStockException("Insufficient stock for product: " + product.getName());
            }

            product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
            productRepository.save(product);

            BigDecimal itemPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(itemPrice);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(itemPrice);

            orderItems.add(orderItem);
        }

        if(!validateOrderTotal(totalPrice)) {
            throw new MinimalOrderPriceException("Order total must be at least $50.00 to be processed");
        }

        BigDecimal discountedPrice = applyDiscount(totalPrice);

        Order order = new Order();
        order.setUser(user);
        order.setItems(orderItems);
        order.setTotalPrice(discountedPrice);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        processOrderAsync(orderItems);

        return orderRepository.save(order);

    }

    // Added SpEL

    private BigDecimal applyDiscount(BigDecimal totalPrice){
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("totalPrice", totalPrice);

        BigDecimal discount = parser.parseExpression(discountExpression).getValue(context, BigDecimal.class);

        return totalPrice.subtract(discount);
    }

    //Added SpEL

    private boolean validateOrderTotal(BigDecimal totalPrice){

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("totalPrice", totalPrice);

        return parser.parseExpression(validationExpression).getValue(context, Boolean.class);
    }

    public List<Order> getOrdersByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserDoesNotExistException("No user with such id exists"));
        return user.getOrders();
    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderDoesNotExistException("No order with such id exists"));
    }

    public void cancelOrder(Integer orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("No order with such id exists"));

        for (OrderItem orderItem : order.getItems()) {
            Product product = orderItem.getProduct();
            product.setAvailableQuantity(product.getAvailableQuantity() + orderItem.getQuantity());
            productRepository.save(product);
        }

        orderRepository.delete(order);
    }

    public CompletableFuture<String> processOrderAsync(List<OrderItem> orderItems){
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Processing order with items" + orderItems.toArray() + "...");
                Thread.sleep(2000);
                System.out.println("Order " + orderItems.toArray() + " payed successfully.");
                return "Order " + orderItems.toArray() + " payed successfully.";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Order processing interrupted.";
            }
        });
    }

}
