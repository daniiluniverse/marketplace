package org.example.marketplace.customerapp.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.customerapp.client.FavouriteProductsClient;
import org.example.marketplace.customerapp.client.exception.ClientBadRequestException;
import org.example.marketplace.customerapp.client.payload.NewFavouriteProductPayload;
import org.example.marketplace.customerapp.entity.FavouriteProduct;
import org.example.marketplace.customerapp.entity.Product;
import org.example.marketplace.customerapp.entity.ProductReview;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class FavouriteProductsClientImpl implements FavouriteProductsClient {

    private final WebClient webClient;


    @Override
    public Mono<FavouriteProduct> addProductToFavorites(Product product) {
        log.info("Отправка{}", product);
        return this.webClient
                .post()
                .uri("/feedback-api/favourite-products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new NewFavouriteProductPayload(product.id(), product.name(), product.details(),  product.price()))
                .retrieve()
//                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
//                        response.bodyToMono(Map.class)
//                                .flatMap(body -> Mono.error(new ClientBadRequestException(
//                                        body.getOrDefault("message", "Validation failed").toString(),
//                                        (Map<String, String>) body.getOrDefault("errors", Map.of())
//                                )))
//                )
                .bodyToMono(FavouriteProduct.class);
    }

    @Override
    public Mono<Void> removeProductFromFavorites(Long productId) {
        return this.webClient
                .delete()
                .uri("/feedback-api/favourite-products/by-product-id/{productId}", productId)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(Long productId) {
        return this.webClient
                .get()
                .uri("/feedback-api/favourite-products/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToMono(FavouriteProduct.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProduct() {
        return this.webClient
                .get()
                .uri("/feedback-api/favourite-products")
                .retrieve()
                .bodyToFlux(FavouriteProduct.class);
    }
}
