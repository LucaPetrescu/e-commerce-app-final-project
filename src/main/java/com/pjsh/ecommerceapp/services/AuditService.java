package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.events.OrderPlacedEvent;
import com.pjsh.ecommerceapp.events.ProductUpdatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    //Here, the events are being listened to whenever they are being published

    @EventListener
    public void handleOrderPlacedEvent(OrderPlacedEvent event){
        System.out.println("OrderPlacedEvent received: Order ID: " + event.getOrderId()
                + ", User ID: " + event.getUserId()
                + ", Total Amount: $" + event.getTotalAmount());
    }

    @EventListener
    public void handleProductUpdatedEvent(ProductUpdatedEvent event) {
        System.out.println("Audit Log - ProductUpdatedEvent: Product ID: "
                + event.getProductId() + " | " + event.getUpdatedFields());
    }

}
