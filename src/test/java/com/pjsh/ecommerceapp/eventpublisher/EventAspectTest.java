package com.pjsh.ecommerceapp.eventpublisher;

import com.pjsh.ecommerceapp.datamodels.Order;
import com.pjsh.ecommerceapp.evenpublisher.EventAspect;
import com.pjsh.ecommerceapp.events.OrderPlacedEvent;
import com.pjsh.ecommerceapp.events.ProductUpdatedEvent;
import com.pjsh.ecommerceapp.services.AuditService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventAspectTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private JoinPoint joinPoint;

    private EventAspect eventAspect;
    private AuditService auditService;
    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    void setUp() {
        eventAspect = new EventAspect(eventPublisher);
        auditService = new AuditService();
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testPublishOrderPlacedEvent() {

        Integer userId = 1;
        Order order = new Order();
        order.setId(100);
        order.setTotalPrice(new BigDecimal("99.99"));

        Object[] args = {userId};
        when(joinPoint.getArgs()).thenReturn(args);

        OrderPlacedEvent event = eventAspect.publishOrderPlacedEvent(joinPoint, order);

        assertNotNull(event);
        assertEquals(100, event.getOrderId());
        assertEquals(1, event.getUserId());
        assertEquals(new BigDecimal("99.99"), event.getTotalAmount());
        verify(eventPublisher, times(1)).publishEvent(event);
    }

    @Test
    void testPublishProductUpdatedEvent_AllFieldsUpdated() {

        Integer productId = 1;
        Object[] args = {productId, "New Name", "New Description", 10, new BigDecimal("29.99")};
        when(joinPoint.getArgs()).thenReturn(args);

        ProductUpdatedEvent event = eventAspect.publishProductUpdatedEvent(joinPoint);

        assertNotNull(event);
        assertEquals(productId, event.getProductId());
        assertEquals("Updated Fields: Name Description Stock Price", event.getUpdatedFields());
        verify(eventPublisher, times(1)).publishEvent(event);
        assertTrue(outputStreamCaptor.toString().trim()
                .contains("ProductUpdatedEvent published for Product ID: 1 | Updated Fields: Name Description Stock Price"));
    }

    @Test
    void testPublishProductUpdatedEvent_PartialUpdate() {

        Integer productId = 1;
        Object[] args = {productId, null, "New Description", null, new BigDecimal("29.99")};
        when(joinPoint.getArgs()).thenReturn(args);

        ProductUpdatedEvent event = eventAspect.publishProductUpdatedEvent(joinPoint);

        assertNotNull(event);
        assertEquals(productId, event.getProductId());
        assertEquals("Updated Fields: Description Price", event.getUpdatedFields());
        verify(eventPublisher, times(1)).publishEvent(event);
    }

    @Test
    void testHandleOrderPlacedEvent() {

        OrderPlacedEvent event = new OrderPlacedEvent(1, 100, new BigDecimal("199.99"));

        auditService.handleOrderPlacedEvent(event);

        String expectedOutput = "OrderPlacedEvent received: Order ID: 1, User ID: 100, Total Amount: $199.99";
        assertTrue(outputStreamCaptor.toString().trim().contains(expectedOutput));
    }

    @Test
    void testHandleProductUpdatedEvent() {

        ProductUpdatedEvent event = new ProductUpdatedEvent(1, "Updated Fields: Name Price");

        auditService.handleProductUpdatedEvent(event);

        String expectedOutput = "Audit Log - ProductUpdatedEvent: Product ID: 1 | Updated Fields: Name Price";
        assertTrue(outputStreamCaptor.toString().trim().contains(expectedOutput));
    }

    @Test
    void testPublishProductUpdatedEvent_NoFieldsUpdated() {

        Integer productId = 1;
        Object[] args = {productId, null, null, null, null};
        when(joinPoint.getArgs()).thenReturn(args);

        ProductUpdatedEvent event = eventAspect.publishProductUpdatedEvent(joinPoint);

        assertNotNull(event);
        assertEquals(productId, event.getProductId());
        assertEquals("Updated Fields:", event.getUpdatedFields());
        verify(eventPublisher, times(1)).publishEvent(event);
    }
}