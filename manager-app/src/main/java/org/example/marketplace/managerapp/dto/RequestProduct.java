package org.example.marketplace.managerapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

public record RequestProduct(
        @NotNull(message = "Имя не может быть пустым")
        String name,
        @NotNull(message = "Описание не может быть пустым")
        @Length(min = 3, max = 50, message = "Описание должно быть от 3 до 50 символов")
        String details,
        @Positive(message = "Цена должна быть положительным числом")
        @NotNull(message = "Цена не может быть null")
        Double price) {

}
