package org.example.marketplace.customerapp.client;

import org.apache.logging.log4j.util.Lazy;
import org.example.marketplace.customerapp.entity.FavouriteProduct;
import org.example.marketplace.customerapp.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductsClient {

    Mono<FavouriteProduct> addProductToFavorites(Product product);

    Mono<Void>  removeProductFromFavorites(Long productId);

    Mono<FavouriteProduct> findFavouriteProductByProductId(Long productId);

    Flux<FavouriteProduct> findFavouriteProduct();



}
