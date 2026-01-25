package org.example.marketplace.paymentservice.repository;


import org.example.marketplace.paymentservice.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
