package org.example.marketplace.customerapp.client.payload;


public record NewFavouriteProductPayload (
         long productId,
         String name,
         String details,
         double price
){
}
