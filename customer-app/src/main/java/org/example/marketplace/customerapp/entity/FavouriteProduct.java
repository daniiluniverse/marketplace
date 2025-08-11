package org.example.marketplace.customerapp.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor

public class FavouriteProduct {

    private UUID id;

    private Product product;


    public FavouriteProduct(){}
}
