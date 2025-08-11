package org.example.marketplace.feedbackservise.repository;

import lombok.RequiredArgsConstructor;
import org.example.marketplace.feedbackservise.entity.ProductReview;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ProductReviewRepositoryImpl implements ProductReviewRepository {

    private final List<ProductReview> productReviewList = Collections.synchronizedList(new LinkedList<>());

    @Override
    public Mono<ProductReview> save(ProductReview productReview) {
         this.productReviewList.add(productReview);

         return Mono.just(productReview);
    }

    @Override
    public Flux<ProductReview> findAllByProductId(Long productId) {
        return Flux.fromIterable(this.productReviewList)
                .filter(productReview -> Objects.equals(productReview.getProductId(), productId));
    }
}
