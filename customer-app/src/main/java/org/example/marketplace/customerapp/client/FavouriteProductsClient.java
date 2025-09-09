package org.example.marketplace.customerapp.client;

import org.apache.logging.log4j.util.Lazy;
import org.example.marketplace.customerapp.dto.FavouriteProductResponse;
import org.example.marketplace.customerapp.entity.FavouriteProduct;
import org.example.marketplace.customerapp.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductsClient {

    Mono<FavouriteProductResponse> addProductToFavorites(Product product);

    Mono<Void>  removeProductFromFavorites(Long productId);

    Mono<FavouriteProduct> findFavouriteProductByProductId(Long productId);

    Flux<FavouriteProductResponse> findFavouriteProduct();



}
