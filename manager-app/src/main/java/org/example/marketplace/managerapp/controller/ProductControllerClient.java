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
    public ResponseEntity<?> createProduct(@Valid @RequestBody RequestProduct product) {
        log.info("Получен запрос на создание продукта: {}", product.name());

            productRestClient.newProduct(product);
            log.info("Продукт успешно создан: {}", product.name());
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Product created successfully",
                    "data", product
            ));

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody RequestProduct product) {

            productRestClient.updateProduct(id, product);
            log.info("Продукт успешно обновлен: {}", product.name());
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Product updated successfully",
                    "data", product
            ));
        }


    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        log.info("Получен запрос на получение продукта с ID: {}", id);
            Product product = productRestClient.getProduct(id);
            return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        log.info("Получен запрос на получение всех продуктов");
            List<Product> products = productRestClient.getAllProducts();
            return ResponseEntity.ok(products);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        log.info("Получен запрос на удаление продукта с ID: {}", id);

            productRestClient.deleteProduct(id);
            log.info("Продукт с ID {} успешно удален", id);
            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Product deleted successfully"
            ));
    }
}