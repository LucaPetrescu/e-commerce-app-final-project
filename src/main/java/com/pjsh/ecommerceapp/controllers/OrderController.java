package com.pjsh.ecommerceapp.controllers;

import com.pjsh.ecommerceapp.dtos.OrderDTO;
import com.pjsh.ecommerceapp.datamodels.Order;
import com.pjsh.ecommerceapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(this.orderService.placeOrder(orderDTO.getUserId(), orderDTO.getProductIds(), orderDTO.getQuantities()));
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Integer userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/getOrderById/{orderId}")
    public Order getOrderById(@PathVariable Integer orderId) {
        return orderService.getOrderById(orderId);
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public void cancelOrder(@PathVariable Integer orderId) {
        orderService.cancelOrder(orderId);
    }

}
