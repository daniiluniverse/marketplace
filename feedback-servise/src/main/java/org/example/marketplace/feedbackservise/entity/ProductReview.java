package org.example.marketplace.feedbackservise.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {

    private UUID id;

    private Long productId;

    private String review;

    private int rating;
}
