package org.example.marketplace.feedbackservise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavouriteProductResponse {

    private long productId;
    private String name;
    private String description;
    private double price;
}
