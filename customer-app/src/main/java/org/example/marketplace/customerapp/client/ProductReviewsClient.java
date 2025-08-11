package org.example.marketplace.customerapp.client;

import org.example.marketplace.customerapp.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewsClient {

    Flux<ProductReview> findProductReviewsByProductId(Long productId);

    Mono<ProductReview> createProductReview(Long productId, String review, int rating);
}
