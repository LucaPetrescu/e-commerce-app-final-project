package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.datamodels.Order;
import com.pjsh.ecommerceapp.datamodels.OrderItem;
import com.pjsh.ecommerceapp.datamodels.Product;
import com.pjsh.ecommerceapp.datamodels.User;
import com.pjsh.ecommerceapp.exceptions.MinimalOrderPriceException;
import com.pjsh.ecommerceapp.exceptions.UserDoesNotExistException;
import com.pjsh.ecommerceapp.repositories.OrderRepository;
import com.pjsh.ecommerceapp.repositories.ProductRepository;
import com.pjsh.ecommerceapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Value("${order.validation.expression}")
    private String validationExpression;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(orderService, "validationExpression", "#totalPrice >= 50");
        ReflectionTestUtils.setField(orderService, "discountExpression",
                "#totalPrice > 500 ? #totalPrice * 0.10 : #totalPrice * 0.05");
    }

    @Test
    void testPlaceOrder_success() {

        User user = new User();
        user.setId(1);
        user.setName("John Doe");

        Product product = new Product();
        product.setId(101);
        product.setName("Laptop");
        product.setAvailableQuantity(10);
        product.setPrice(BigDecimal.valueOf(1000));

        List<Integer> productIds = List.of(101);
        List<Integer> quantities = List.of(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(101)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.placeOrder(1, productIds, quantities);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(BigDecimal.valueOf(900.0), result.getTotalPrice());
        verify(productRepository).save(product);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testPlaceOrder_insufficientStock() {

        User user = new User();
        user.setId(1);
        user.setName("John Doe");

        Product product = new Product();
        product.setId(101);
        product.setName("Laptop");
        product.setAvailableQuantity(0);
        product.setPrice(BigDecimal.valueOf(1000));

        List<Integer> productIds = List.of(101);
        List<Integer> quantities = List.of(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(101)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.placeOrder(1, productIds, quantities));
        assertEquals("Insufficient stock for product: Laptop", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testPlaceOrder_minimalOrderValidation() {

        User user = new User();
        user.setId(1);
        user.setName("John Doe");

        Product product = new Product();
        product.setId(101);
        product.setName("Laptop");
        product.setAvailableQuantity(10);
        product.setPrice(BigDecimal.valueOf(20));

        List<Integer> productIds = List.of(101);
        List<Integer> quantities = List.of(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(101)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(MinimalOrderPriceException.class, () -> orderService.placeOrder(1, productIds, quantities));
        assertEquals("Order total must be at least $50.00 to be processed", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetOrdersByUserId_success() {

        User user = new User();
        user.setId(1);
        user.setOrders(new ArrayList<>());

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        List<Order> result = orderService.getOrdersByUserId(1);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository).findById(1);
    }

    @Test
    void testGetOrdersByUserId_userDoesNotExist() {

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserDoesNotExistException.class, () -> orderService.getOrdersByUserId(1));
        assertEquals("No user with such id exists", exception.getMessage());
    }

    @Test
    void testCancelOrder_success() {

        Order order = new Order();
        order.setId(1);

        Product product = new Product();
        product.setId(101);
        product.setAvailableQuantity(5);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);

        order.setItems(List.of(item));

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1);

        verify(productRepository).save(product);
        verify(orderRepository).delete(order);
    }

    @Test
    void testCancelOrder_orderDoesNotExist() {

        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.cancelOrder(1));
        assertEquals("No order with such id exists", exception.getMessage());
    }

}
