package org.example.marketplace.orderservs.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long userId;
    private List<CartItemResponse> items;
    private BigDecimal total;
    private Integer itemCount;

    private LocalDateTime updatedAt;
}
