package org.example.marketplace.feedbackservise.service;

import org.example.marketplace.feedbackservise.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewService {

    Mono<ProductReview> createProductReview(Long productId, String review, int rating, String userId);

    Flux<ProductReview> findProductReviewByProduct(Long productId);
}
