package org.example.marketplace.feedbackservise.dto;

import org.example.marketplace.feedbackservise.entity.ProductReview;

public record ProductReviewResponse(
        Long productId,
        String review,
        int rating
) {

    public static ProductReviewResponse fromEntity(ProductReview entity) {
        return new ProductReviewResponse(
                entity.getProductId(),
                entity.getReview(),
                entity.getRating()
        );

    }

}
