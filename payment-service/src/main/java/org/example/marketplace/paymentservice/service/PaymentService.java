package org.example.marketplace.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.example.marketplace.paymentservice.entity.PaymentEntity;
import org.example.marketplace.paymentservice.entity.PaymentMethod;
import org.example.marketplace.paymentservice.entity.PaymentStatus;
import org.example.marketplace.paymentservice.kafka.OrderCreatedEvent;
import org.example.marketplace.paymentservice.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final static Logger log = LoggerFactory.getLogger(PaymentService.class);


    public PaymentEntity createPayment(OrderCreatedEvent event){

        log.info("Создание платежа для заказа: {}", event.getOrderNumber());

        PaymentEntity payment = PaymentEntity.builder()
                .orderId(event.getOrderId())
                .orderNumber(event.getOrderNumber())
                .amount(event.getAmount())
                .paymentStatus(PaymentStatus.SUCCESS)
                .paymentMethod(PaymentMethod.CARD)
                .build();

        return this.paymentRepository.save(payment);
    }


}
