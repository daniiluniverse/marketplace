package org.example.marketplace.orderservs.repository;

import org.example.marketplace.orderservs.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {
}
