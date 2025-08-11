package org.example.marketplace.customerapp.dto;

public record ProductReviewRequest(
        String review,
        Integer rating) {
}
