package org.example.marketplace.feedbackservise.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;


import java.util.UUID;

@Data
@AllArgsConstructor

public class FavouriteProduct {

    @Id
    private UUID id;

    private Product product;

    private String userId;


    public FavouriteProduct(Product product) {
        this.product = product;
    }

    public FavouriteProduct(){}
}
