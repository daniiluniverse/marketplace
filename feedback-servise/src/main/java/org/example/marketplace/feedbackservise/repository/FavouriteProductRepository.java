package org.example.marketplace.feedbackservise.repository;

import org.example.marketplace.feedbackservise.entity.FavouriteProduct;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FavouriteProductRepository extends ReactiveCrudRepository<FavouriteProduct, UUID> {



    Mono<Void> deleteByProductIdAndUserId(Long product_id, String userId);

    Mono<FavouriteProduct> findByProductIdAndUserId(Long productId, String userId);

    Flux<FavouriteProduct> findAllByUserId(String userId);
}
