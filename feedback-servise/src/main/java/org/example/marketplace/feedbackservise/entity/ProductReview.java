package org.example.marketplace.feedbackservise.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {

    @Id
    private UUID id;

    private Long productId;

    private String review;

    private int rating;

    private String userId;

}
