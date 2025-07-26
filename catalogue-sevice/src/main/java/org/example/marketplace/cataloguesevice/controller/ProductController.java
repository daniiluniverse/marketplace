package org.example.marketplace.cataloguesevice.controller;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.marketplace.cataloguesevice.dto.ProductUpdateRequest;
import org.example.marketplace.cataloguesevice.dto.RequestProduct;
import org.example.marketplace.cataloguesevice.entity.Product;
import org.example.marketplace.cataloguesevice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/products")
public class ProductController {

   private final ProductService productService;

   @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createProduct(@Valid @RequestBody RequestProduct requestProduct) {

        Product product = Product.builder()
                .name(requestProduct.name())
                .details(requestProduct.details())
                .price(requestProduct.price())
                .build();

        productService.saveProduct(product);

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message", "продукт создан"
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest updateDTO){

           Product product = productService.getProduct(id);

           if (updateDTO.name() != null)
               product.setName(updateDTO.name());
           if (updateDTO.details() != null)
               product.setDetails(updateDTO.details());
           if (updateDTO.price() != null)
               product.setPrice(updateDTO.price());

           productService.updateProduct(product);

           return ResponseEntity.ok().body(Map.of(
                   "status", "ok",
                   "message", "продукт обновлен"

           ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){

       Product product = productService.getProduct(id);

       return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){

       Product product = productService.getProduct(id);
        productService.deleteProduct(product.getId());

        return ResponseEntity.ok().body(Map.of(
                "status", "ok",
                "message", "продукт удален"

        ));
    }
}
