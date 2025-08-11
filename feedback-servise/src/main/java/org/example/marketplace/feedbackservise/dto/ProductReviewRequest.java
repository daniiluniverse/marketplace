package org.example.marketplace.feedbackservise.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProductReviewRequest(
        @NotNull
        Long productId,
        @Length(max = 1000, message = "Максимальная длина 1000 символов")
        String review,
        @NotNull(message = "Заполните оценку")
        @Min(value = 1, message = "Минимальная оценка 1")
        @Max(value = 5, message = "Максимальная оценка 5")
        Integer rating) {
}
