package org.example.marketplace.orderservs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.marketplace.orderservs.entity.OrderItem;
import org.example.marketplace.orderservs.entity.OrderStatus;
import org.example.marketplace.orderservs.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private String orderNumber;

    private Long userId;

    private List<OrderItemDTO> items;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private OrderStatus orderStatus;

    private String address;

    private PaymentStatus paymentStatus;
}
