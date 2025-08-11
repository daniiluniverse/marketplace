package org.example.marketplace.feedbackservise.entity;


import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.UUID;

@Data
@AllArgsConstructor

public class FavouriteProduct {

    private UUID id;

    private Product product;


    public FavouriteProduct(Product product) {
        this.product = product;
    }

    public FavouriteProduct(){}
}
