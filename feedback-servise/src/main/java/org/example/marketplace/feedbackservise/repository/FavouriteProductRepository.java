package org.example.marketplace.feedbackservise.repository;

import org.example.marketplace.feedbackservise.entity.FavouriteProduct;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FavouriteProductRepository {


    Mono<Void> delete(Long productId);


    Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct);

    Mono<FavouriteProduct> findByProductId(Long productId);


    Flux<FavouriteProduct> findAll();
}
