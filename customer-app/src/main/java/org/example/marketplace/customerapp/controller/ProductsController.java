package org.example.marketplace.customerapp.controller;


import lombok.RequiredArgsConstructor;
import org.example.marketplace.customerapp.client.FavouriteProductsClient;
import org.example.marketplace.customerapp.client.ProductsClient;
import org.example.marketplace.customerapp.dto.FavouriteProductResponse;
import org.example.marketplace.customerapp.dto.ProductResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("customer/products")
public class ProductsController {

    private final ProductsClient productsClient;
    private final FavouriteProductsClient favouriteProductsClient;

    @GetMapping("/list")
    public Flux<ProductResponse> getProductsList() {
        return productsClient.findAllProducts()
                .map(product -> new ProductResponse(
                        product.id(),
                        product.name(),
                        product.details(),
                        product.price()
                ));
    }

    @GetMapping("/favourite")
    public Flux<FavouriteProductResponse> getFavouriteProduct(){

        return this.favouriteProductsClient.findFavouriteProduct();



    }
}
