package org.example.marketplace.customerapp.client.payload;

public record NewProductReviewPayload(
        Long productId,
        String review,
        int rating
) {
}
