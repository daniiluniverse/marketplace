package org.example.marketplace.orderservs.repository;

import org.example.marketplace.orderservs.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
