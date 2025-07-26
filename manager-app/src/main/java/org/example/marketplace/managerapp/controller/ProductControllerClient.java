package org.example.marketplace.managerapp.controller;

import jakarta.validation.Valid;
import org.example.marketplace.managerapp.client.ProductRestClient;
import org.example.marketplace.managerapp.dto.RequestProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductControllerClient {

    private final ProductRestClient productRestClient;
    private static final Logger log = LoggerFactory.getLogger(ProductControllerClient.class);

    public ProductControllerClient(ProductRestClient productRestClient) {
        this.productRestClient = productRestClient;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody RequestProduct product) {

        log.info("Получен запрос на создание продукта: {}", product.name());

        try {
            productRestClient.newProduct(product);
            log.info("Продукт успешно создан: {}", product.name());
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Product created successfully",
                    "data", product
            ));
        } catch (HttpClientErrorException e) {
            log.error("Ошибка клиента при создании продукта: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", "Failed to create product",
                    "error", e.getStatusText()
            ));
        } catch (Exception e) {
            log.error("Неожиданная ошибка при создании продукта: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Internal server error",
                    "error", "Please try again later"
            ));
        }
    }
}