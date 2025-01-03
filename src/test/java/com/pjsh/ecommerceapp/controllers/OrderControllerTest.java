package com.pjsh.ecommerceapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.ecommerceapp.dtos.OrderDTO;
import com.pjsh.ecommerceapp.datamodels.Order;
import com.pjsh.ecommerceapp.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testPlaceOrder_success() throws Exception {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1);
        orderDTO.setProductIds(List.of(101, 102));
        orderDTO.setQuantities(List.of(2, 1));

        Order order = new Order();
        order.setId(100);
        order.setTotalPrice(BigDecimal.valueOf(300));

        when(orderService.placeOrder(orderDTO.getUserId(), orderDTO.getProductIds(), orderDTO.getQuantities()))
                .thenReturn(order);

        mockMvc.perform(post("/order/placeOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.totalPrice").value(300));

        verify(orderService).placeOrder(orderDTO.getUserId(), orderDTO.getProductIds(), orderDTO.getQuantities());
    }

    @Test
    void testGetOrdersByUserId_success() throws Exception {

        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(101);
        order1.setTotalPrice(BigDecimal.valueOf(150));
        orders.add(order1);

        when(orderService.getOrdersByUserId(1)).thenReturn(orders);

        mockMvc.perform(get("/order/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].totalPrice").value(150));

        verify(orderService).getOrdersByUserId(1);
    }

    @Test
    void testGetOrderById_success() throws Exception {
        Order order = new Order();
        order.setId(100);
        order.setTotalPrice(BigDecimal.valueOf(200));

        when(orderService.getOrderById(100)).thenReturn(order);

        mockMvc.perform(get("/order/getOrderById/{orderId}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.totalPrice").value(200));

        verify(orderService).getOrderById(100);
    }

    @Test
    void testCancelOrder_success() throws Exception {

        mockMvc.perform(delete("/order/deleteOrder/{orderId}", 100)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService).cancelOrder(100);
    }
}
