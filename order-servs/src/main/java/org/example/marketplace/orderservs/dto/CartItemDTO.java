package org.example.marketplace.orderservs.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartItemDTO implements Serializable {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity = 1;
}
