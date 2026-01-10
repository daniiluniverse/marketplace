package org.example.marketplace.orderservs.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CartResponse {
    private Long userId;
    private List<CartItemResponse> items;
    private BigDecimal total;
    private Integer itemCount;

    private LocalDateTime updatedAt;
}
