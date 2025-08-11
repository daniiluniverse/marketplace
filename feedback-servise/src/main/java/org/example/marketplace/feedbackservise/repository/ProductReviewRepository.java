package org.example.marketplace.feedbackservise.repository;

import org.example.marketplace.feedbackservise.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewRepository {

    Mono<ProductReview> save(ProductReview productReview);

    Flux<ProductReview> findAllByProductId(Long productId);
}
