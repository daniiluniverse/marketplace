package org.example.marketplace.cataloguesevice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record ProductUpdateRequest(
        @Size(min = 1, message = "Имя должно быть больше 1 символа")
        String name,
        @Size(min = 3, max = 50, message = "Описание должно быть от 3 до 50 символов")
        String details,
        @Positive(message = "Цена должна быть положительным числом")
        Double price) {

}
