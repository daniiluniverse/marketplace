package org.example.marketplace.managerapp.controller;

import jakarta.validation.Valid;
import org.example.marketplace.managerapp.client.ProductRestClient;
import org.example.marketplace.managerapp.dto.RequestProduct;
import org.example.marketplace.managerapp.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductControllerClient {

    private final ProductRestClient productRestClient;
    private static final Logger log = LoggerFactory.getLogger(ProductControllerClient.class);

    public ProductControllerClient(ProductRestClient productRestClient) {
        this.productRestClient = productRestClient;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody RequestProduct product, BindingResult bindingResult) {
        log.info("Получен запрос на создание продукта: {}", product.name());

        if (bindingResult.hasErrors()){
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField,
                                    error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Неизвестная ошибка")
                            );
            return ResponseEntity.badRequest().body(errors);
        }

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
                    "error", e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody RequestProduct product, BindingResult bindingResult) {
        log.info("Получен запрос на обновление продукта с ID {}: {}", id, product.name());

        if (bindingResult.hasErrors()){
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField,
                            error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Неизвестная ошибка")
                    );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            productRestClient.updateProduct(id, product);
            log.info("Продукт успешно обновлен: {}", product.name());
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Product updated successfully",
                    "data", product
            ));
        } catch (HttpClientErrorException e) {
            log.error("Ошибка клиента при обновлении продукта: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", "Failed to update product",
                    "error", e.getStatusText()
            ));
        } catch (Exception e) {
            log.error("Неожиданная ошибка при обновлении продукта: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Internal server error",
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        log.info("Получен запрос на получение продукта с ID: {}", id);

        try {
            Product product = productRestClient.getProduct(id);
            return ResponseEntity.ok(product);
        } catch (HttpClientErrorException e) {
            log.error("Ошибка клиента при получении продукта: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", "Failed to get product",
                    "error", e.getStatusText()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        log.info("Получен запрос на получение всех продуктов");

        try {
            List<Product> products = productRestClient.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (HttpClientErrorException e) {
            log.error("Ошибка клиента при получении продуктов: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", "Failed to get products",
                    "error", e.getStatusText()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        log.info("Получен запрос на удаление продукта с ID: {}", id);

        try {
            productRestClient.deleteProduct(id);
            log.info("Продукт с ID {} успешно удален", id);
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Product deleted successfully"
            ));
        } catch (HttpClientErrorException e) {
            log.error("Ошибка клиента при удалении продукта: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", "Failed to delete product",
                    "error", e.getStatusText()
            ));
        }
    }


}