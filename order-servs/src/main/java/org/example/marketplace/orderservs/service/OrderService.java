package org.example.marketplace.orderservs.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.orderservs.dto.CartData;
import org.example.marketplace.orderservs.dto.CartItemDTO;
import org.example.marketplace.orderservs.dto.CreateOrderRequest;
import org.example.marketplace.orderservs.dto.OrderDTO;
import org.example.marketplace.orderservs.entity.*;
import org.example.marketplace.orderservs.mapper.OrderMapper;
import org.example.marketplace.orderservs.repository.CartRedisRepository;
import org.example.marketplace.orderservs.repository.OrderItemsRepository;
import org.example.marketplace.orderservs.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {




    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final OrderMapper orderMapper;
    private final CartRedisRepository cartRedisRepository;


    @Transactional
    public OrderDTO createOrder(Long userId, String address) {

        CartData cart = cartRedisRepository.getCart(userId);

        log.info("Создание заказа");

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .orderStatus(OrderStatus.CREATED)
                .totalAmount(cart.getTotal())
                .paymentStatus(PaymentStatus.PENDING)
                .address(address)
                .items(new ArrayList<>())
                .build();


        for (int i = 0; i <= cart.getItems().size() - 1; i++) {
            CartItemDTO cartItem = cart.getItems().get(i);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .productName(cartItem.getProductName())
                .productId(cartItem.getProductId())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
        order.getItems().add(orderItem);
    }
        log.info("заказ {} {}", order.getId(), order.getOrderNumber());

        Order save = this.orderRepository.save(order);
        return orderMapper.toDTO(save);
    }



    public String generateOrderNumber(){
        Random random = new Random();
        int n = random.nextInt(1000);
        return "ORD-" + n + "-" + System.currentTimeMillis();
    }

}

