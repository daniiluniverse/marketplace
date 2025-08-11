package org.example.marketplace.customerapp.dto;

import org.example.marketplace.customerapp.entity.ProductReview;

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
