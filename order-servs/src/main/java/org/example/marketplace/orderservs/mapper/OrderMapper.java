package org.example.marketplace.orderservs.mapper;

import org.example.marketplace.orderservs.dto.OrderDTO;
import org.example.marketplace.orderservs.dto.OrderItemDTO;
import org.example.marketplace.orderservs.entity.Order;
import org.example.marketplace.orderservs.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        if (order == null) return null;

        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .items(toItemDTOs(order.getItems()))
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .orderStatus(order.getOrderStatus())
                .address(order.getAddress())
                .paymentStatus(order.getPaymentStatus())
                .build();
    }

    private List<OrderItemDTO> toItemDTOs(List<OrderItem> items) {
        if (items == null) return new ArrayList<>();

        return items.stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
    }

    private OrderItemDTO toItemDTO(OrderItem item) {
        return OrderItemDTO.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
    }
}