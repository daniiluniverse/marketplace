package org.example.marketplace.orderservs.controller;

import org.example.marketplace.orderservs.client.ProductRestClient;
import org.example.marketplace.orderservs.dto.CreateOrderRequest;
import org.example.marketplace.orderservs.dto.OrderDTO;
import org.example.marketplace.orderservs.dto.OrderItemDTO;
import org.example.marketplace.orderservs.entity.OrderItem;
import org.example.marketplace.orderservs.entity.OrderStatus;
import org.example.marketplace.orderservs.entity.PaymentStatus;
import org.example.marketplace.orderservs.entity.Product;
import org.example.marketplace.orderservs.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerUnitTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ProductRestClient productRestClient;



    @InjectMocks
    private OrderController orderController;



    @Test
    void createOrder_Success() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        String address = "Пушкина";

        Product product = new Product(productId, "iPhone 15 Pro", "Test details",1299.99);

        OrderItemDTO item = new OrderItemDTO(productId, "iPhone 15 Pro", new BigDecimal("1299.99"), 2);
        OrderDTO expectedOrder = new OrderDTO();
        expectedOrder.setId(31L);
        expectedOrder.setOrderNumber("ORD-330-1768248403067");
        expectedOrder.setUserId(userId);
        expectedOrder.setItems(List.of(item));
        expectedOrder.setTotalAmount(BigDecimal.valueOf(2599.98));
        expectedOrder.setCreatedAt(LocalDateTime.now());
        expectedOrder.setOrderStatus(OrderStatus.CREATED);
        expectedOrder.setAddress(address);
        expectedOrder.setPaymentStatus(PaymentStatus.PENDING);

        when(productRestClient.findProduct(productId)).thenReturn(product);
        when(orderService.createOrder(eq(userId), any(CreateOrderRequest.class)))
                .thenReturn(expectedOrder);

        // When
        ResponseEntity<?> response = orderController.createOrder(userId, productId, address);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/orders/31"), response.getHeaders().getLocation());

        OrderDTO responseBody = (OrderDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals(31L, responseBody.getId());
        assertEquals("ORD-330-1768248403067", responseBody.getOrderNumber());
        assertEquals(OrderStatus.CREATED, responseBody.getOrderStatus());
        assertEquals(new BigDecimal("2599.98"), responseBody.getTotalAmount(), String.valueOf(0.001));

        verify(productRestClient).findProduct(productId);

    }



}