package org.example.marketplace.feedbackservise.service;

import org.example.marketplace.feedbackservise.entity.FavouriteProduct;
import org.example.marketplace.feedbackservise.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductService {

    Mono<FavouriteProduct> addProductToFavorite(Product product, String userId);

    Mono<Void> removeProductFromFavorites(Long product, String userId);

    Mono<FavouriteProduct> findFavouriteProductByProductsId(Long productId, String userId);

    Flux<FavouriteProduct> findFavouriteProduct(String userId);




}
