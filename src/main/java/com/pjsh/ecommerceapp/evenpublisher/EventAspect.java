package com.pjsh.ecommerceapp.evenpublisher;

import com.pjsh.ecommerceapp.datamodels.Order;
import com.pjsh.ecommerceapp.events.OrderPlacedEvent;
import com.pjsh.ecommerceapp.events.ProductUpdatedEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;

//Here, events are created with the help of the event classes in the "events" package

@Aspect
@Component
public class EventAspect {

    private ApplicationEventPublisher eventPublisher;

    public EventAspect(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @AfterReturning(pointcut = "execution(* com.pjsh.ecommerceapp.services.OrderService.placeOrder(..))", returning = "result")
    public OrderPlacedEvent publishOrderPlacedEvent(JoinPoint joinPoint, Object result) {
        Object[] args = joinPoint.getArgs();
        Integer userId = (Integer) args[0];

        Order placedOrder = (Order) result;

        Integer orderId = placedOrder.getId();
        BigDecimal totalAmount = placedOrder.getTotalPrice();

        OrderPlacedEvent event = new OrderPlacedEvent(orderId, userId, totalAmount);
        eventPublisher.publishEvent(event);

        return event;
    }

    @After("execution(* com.pjsh.ecommerceapp.services.ProductService.updateProduct(..))")
    public ProductUpdatedEvent publishProductUpdatedEvent(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Integer productId = (Integer) args[0];
        String updatedFields = "Updated Fields: ";

        if (args[1] != null) updatedFields += "Name ";
        if (args[2] != null) updatedFields += "Description ";
        if (args[3] != null) updatedFields += "Stock ";
        if (args[4] != null) updatedFields += "Price ";

        ProductUpdatedEvent event = new ProductUpdatedEvent(productId, updatedFields.trim());
        eventPublisher.publishEvent(event);

        System.out.println("ProductUpdatedEvent published for Product ID: " + productId + " | " + updatedFields);

        return event;
    }

}
