package org.example.marketplace.paymentservice.kafka;

import lombok.RequiredArgsConstructor;
import org.example.marketplace.paymentservice.entity.PaymentEntity;
import org.example.marketplace.paymentservice.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;
    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);

    @KafkaListener(topics = "order.created", groupId = "payments")
    public PaymentEntity handleOrderCreated(OrderCreatedEvent event){
        log.info("Новый платеж {}", event);

        return paymentService.createPayment(event);
    }
}
