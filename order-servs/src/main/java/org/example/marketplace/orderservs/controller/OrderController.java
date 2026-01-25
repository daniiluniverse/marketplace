package org.example.marketplace.orderservs.controller;


import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.marketplace.orderservs.client.ProductRestClient;
import org.example.marketplace.orderservs.dto.CreateOrderRequest;
import org.example.marketplace.orderservs.dto.OrderDTO;
import org.example.marketplace.orderservs.entity.Order;
import org.example.marketplace.orderservs.entity.Product;
import org.example.marketplace.orderservs.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("{userId}/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductRestClient productRestClient;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);



    @PostMapping("/new/{productId}")
    public ResponseEntity<?> createOrder(@PathVariable Long userId, @PathVariable Long productId, @RequestBody String address){

        log.info("Creating order with product {}", productId);

        try {
            Product product = productRestClient.findProduct(productId);
            if (product == null) {
                log.error("Product is null!");
                return ResponseEntity.notFound().build();
            }


            CreateOrderRequest createOrderRequest = new CreateOrderRequest(product.id(), product.name(), product.price(), address);
            OrderDTO order = this.orderService.createOrder(userId, createOrderRequest);

            return ResponseEntity.created(URI.create("/orders/"+order.getId()))
                    .body(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/orderId")

    public ResponseEntity<?> cancelOrder(@PathVariable Long userId, @PathVariable Long orderId){
        orderService.cancelOrder(userId, orderId);

        return ResponseEntity.ok().body("Заказ отменен");
    }


}
