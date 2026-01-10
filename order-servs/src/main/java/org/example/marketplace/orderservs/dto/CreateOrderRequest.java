package org.example.marketplace.orderservs.dto;

import org.example.marketplace.orderservs.entity.OrderItem;

public record CreateOrderRequest(
        long productId,
        String productName,
        double price
) {

}
