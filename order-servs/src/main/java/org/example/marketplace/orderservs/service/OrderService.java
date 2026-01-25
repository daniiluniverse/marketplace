package org.example.marketplace.orderservs.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.TimeoutException;
import org.example.marketplace.orderservs.dto.CartData;
import org.example.marketplace.orderservs.dto.CartItemDTO;
import org.example.marketplace.orderservs.dto.CreateOrderRequest;
import org.example.marketplace.orderservs.dto.OrderDTO;
import org.example.marketplace.orderservs.entity.*;
import org.example.marketplace.orderservs.kafka.OrderCreatedEvent;
import org.example.marketplace.orderservs.mapper.OrderMapper;
import org.example.marketplace.orderservs.repository.CartRedisRepository;
import org.example.marketplace.orderservs.repository.OrderItemsRepository;
import org.example.marketplace.orderservs.repository.OrderRepository;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final OrderMapper orderMapper;
    private final CartRedisRepository cartRedisRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Transactional
    public OrderDTO createOrderFromCart(Long userId, String address) {

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
        log.info("заказ  {}", order.getOrderNumber());



        Order save = this.orderRepository.save(order);

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .amount(order.getTotalAmount())
                .build();

        // kafkaTemplate.send("order.created", save.getOrderNumber(), event);

        log.info("Гарантированная отправка в Kafka...");

        try {
            // get() ждёт завершения (блокирующий вызов)
            SendResult<String, Object> result = kafkaTemplate.send(
                    "order.created",
                    save.getOrderNumber(),
                    event
            ).get(10, TimeUnit.SECONDS);  // ждём максимум 10 секунд

            log.info("✅ KAFKA УСПЕХ: Partition={}, Offset={}",
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());

        } catch (TimeoutException e) {
            log.error("⏰ KAFKA ТАЙМАУТ: Превышено время ожидания");
        } catch (ExecutionException e) {
            log.error("❌ KAFKA ОШИБКА ИСПОЛНЕНИЯ: {}", e.getCause().getMessage());
        } catch (Exception e) {
            log.error("⚠️ KAFKA ОБЩАЯ ОШИБКА: {}", e.getMessage(), e);
        }

        log.info("Новое событие  {}", event);

        return orderMapper.toDTO(save);
    }

    @Transactional
    public OrderDTO createOrder(Long userId, CreateOrderRequest createOrderRequest) {

        log.info("Создание заказа");

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .orderStatus(OrderStatus.CREATED)
                .totalAmount(BigDecimal.valueOf(createOrderRequest.price()))
                .paymentStatus(PaymentStatus.PENDING)
                .address(createOrderRequest.address())
                .items(new ArrayList<>())
                .build();


            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productName(createOrderRequest.productName())
                    .productId(createOrderRequest.productId())
                    .price(BigDecimal.valueOf(createOrderRequest.price()))
                    .quantity(1)
                    .build();
            order.getItems().add(orderItem);


        log.info("заказ {} {}", order.getId(), order.getOrderNumber());

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .amount(order.getTotalAmount())
                .build();

        Order save = this.orderRepository.save(order);

        kafkaTemplate.send("order.created", save.getOrderNumber(), event);
        return orderMapper.toDTO(save);
    }



    public String generateOrderNumber(){
        Random random = new Random();
        int n = random.nextInt(1000);
        return "ORD-" + n + "-" + System.currentTimeMillis();
    }

    @Transactional
    public void cancelOrder(Long userId, Long orderId) {

       Order order = this.orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
       order.setOrderStatus(OrderStatus.CANCELLED);
       order.setPaymentStatus(PaymentStatus.CANCELLED);

        this.orderRepository.save(order);
    }
}

