package org.example.marketplace.customerapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.customerapp.client.FavouriteProductsClient;
import org.example.marketplace.customerapp.client.ProductReviewsClient;
import org.example.marketplace.customerapp.client.ProductsClient;
import org.example.marketplace.customerapp.dto.FavouriteProductResponse;
import org.example.marketplace.customerapp.dto.ProductResponse;
import org.example.marketplace.customerapp.dto.ProductReviewRequest;
import org.example.marketplace.customerapp.dto.ProductReviewResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId:\\d+}")
@Slf4j
public class ProductController {


    private final FavouriteProductsClient favouriteProductsClient;
    private final ProductsClient productsClient;
    private final ProductReviewsClient productReviewsClient;


    @GetMapping
    public Mono<ProductResponse> getProduct(@PathVariable Long productId){


        return this.productsClient.findProduct(productId)
                .map(product -> new ProductResponse(
                        product.id(),
                        product.name(),
                        product.details(),
                        product.price()
                ))
                .onErrorResume(throwable -> {
                    if (throwable instanceof WebClientResponseException.NotFound) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Product not found"
                        ));
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Internal server error"
                    ));
                });

    }

    @PostMapping("/favourite-product/add/")
    public Mono<ResponseEntity<FavouriteProductResponse>> addProductToFavourites(@PathVariable Long productId){

        log.info("Запрос на создание продукта" + productId);

        return productsClient.findProduct(productId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Product not found"
                        )
                )))
                .flatMap(product ->
                        this.favouriteProductsClient.addProductToFavorites(product)
                                .log("Создание продукта" + product)
                                .map(favouriteProduct -> ResponseEntity.ok(
                                        new FavouriteProductResponse(
                                                product.id(),
                                                product.name(),
                                                product.details(),
                                                product.price()
                                        )
                                ))
                );
    }

    @DeleteMapping("/favourite-product/delete/")
    public Mono<ResponseEntity<Void>> deleteProductFromFavorites (@PathVariable Long productId){
        log.info("Запрос на удаление продукта" + productId);
        return this.favouriteProductsClient.removeProductFromFavorites(productId)
                .thenReturn(ResponseEntity.noContent().build());

    }

    @GetMapping("/favourite-product")
    public Mono<ResponseEntity<FavouriteProductResponse>> getFavouriteProductByProductId(@PathVariable Long productId){

        return this.favouriteProductsClient.findFavouriteProductByProductId(productId)
                .map(favouriteProduct -> new FavouriteProductResponse(
                        favouriteProduct.getProduct().id(),
                        favouriteProduct.getProduct().name(),
                        favouriteProduct.getProduct().details(),
                        favouriteProduct.getProduct().price()
                ))
                .map(ResponseEntity::ok);
    }

    @PostMapping("/create-review")
    public Mono<ResponseEntity<ProductReviewResponse>> createReview(@PathVariable Long productId,@RequestBody ProductReviewRequest productReviewRequest){

        return productsClient.findProduct(productId)
                .flatMap(product -> productReviewsClient.createProductReview(
                                productId,
                                productReviewRequest.review(),
                                productReviewRequest.rating())
                        .then(Mono.just(ResponseEntity.ok(new ProductReviewResponse(
                                productId,
                                productReviewRequest.review(),
                                productReviewRequest.rating()
                        ))))
                )
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/get-reviews")
    Mono<ResponseEntity<?>> getAllProductsReview(@PathVariable Long productId){

        return productReviewsClient.findProductReviewsByProductId(productId)
                .map(ProductReviewResponse::fromEntity)
                .collectList()
                .map(reviews -> {
                    if (reviews.isEmpty()) {
                        return ResponseEntity.noContent().build();
                    }
                    return ResponseEntity.ok(reviews);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }





}
