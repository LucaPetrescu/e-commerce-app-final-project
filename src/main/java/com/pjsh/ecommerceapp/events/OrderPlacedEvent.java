package com.pjsh.ecommerceapp.events;

import java.math.BigDecimal;

//Class for defining an event

public class OrderPlacedEvent {

    private Integer orderId;
    private Integer userId;
    private BigDecimal totalAmount;

    public OrderPlacedEvent(Integer orderId, Integer userId, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

}
