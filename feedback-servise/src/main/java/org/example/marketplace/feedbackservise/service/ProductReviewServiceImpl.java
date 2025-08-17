package org.example.marketplace.feedbackservise.service;

import lombok.RequiredArgsConstructor;
import org.example.marketplace.feedbackservise.entity.ProductReview;
import org.example.marketplace.feedbackservise.repository.ProductReviewRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository productReviewRepository;


    @Override
    public Mono<ProductReview> createProductReview(Long productId, String review, int rating, String userId) {
        return this.productReviewRepository.save(new ProductReview(UUID.randomUUID(), productId, review, rating, userId));
    }

    @Override
    public Flux<ProductReview> findProductReviewByProduct(Long productId) {
        return this.productReviewRepository.findAllByProductId(productId);
    }
}
