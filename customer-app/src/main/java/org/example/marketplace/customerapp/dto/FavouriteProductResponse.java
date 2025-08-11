package org.example.marketplace.customerapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavouriteProductResponse {

    private long productId;
    private String name;
    private String details;
    private double price;
}
