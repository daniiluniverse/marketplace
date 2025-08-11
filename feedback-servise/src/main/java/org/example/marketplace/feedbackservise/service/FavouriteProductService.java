package org.example.marketplace.feedbackservise.service;

import org.example.marketplace.feedbackservise.entity.FavouriteProduct;
import org.example.marketplace.feedbackservise.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductService {

    Mono<FavouriteProduct> addProductToFavorite(Product product);

    Mono<Void> removeProductFromFavorites(Long product);

    Mono<FavouriteProduct> findFavouriteProductByProductsId(Long productId);

    Flux<FavouriteProduct> findFavouriteProduct();




}
