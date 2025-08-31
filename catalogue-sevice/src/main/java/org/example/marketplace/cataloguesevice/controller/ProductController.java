package org.example.marketplace.cataloguesevice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.marketplace.cataloguesevice.dto.ProductUpdateRequest;
import org.example.marketplace.cataloguesevice.dto.RequestProduct;
import org.example.marketplace.cataloguesevice.entity.Product;
import org.example.marketplace.cataloguesevice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/products")

public class ProductController {
    private final ProductService productService;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = @SecurityRequirement(name = "keycloak"))
//    @Operation(responses = {
//            @ApiResponse(
//                    responseCode = "201",
//                    headers = @Header(name = "Content-Type", description = "Тип данных"),
//                    content = @Content(
//                            schema = @Schema(
//
//                            )
//                    )
//            )
//    })
    public Product createProduct(@Valid @RequestBody RequestProduct request) {
        log.info("Создание продукта: {}", request.name());
        return productService.saveProduct(request);

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(security = @SecurityRequirement(name = "keycloak"))
    public void updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        log.info("Обновление продукта ID {}", id);
        productService.updateProduct(id, request);
    }

    @GetMapping("/{id}")
    @Operation(security = @SecurityRequirement(name = "keycloak"))
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping
    @Operation(security = @SecurityRequirement(name = "keycloak"))
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(security = @SecurityRequirement(name = "keycloak"))
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}