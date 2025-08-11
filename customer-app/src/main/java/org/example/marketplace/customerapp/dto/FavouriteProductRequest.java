package org.example.marketplace.customerapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteProductRequest {

    private long productId;
    private String name;
    private String details;
    private double price;
}
