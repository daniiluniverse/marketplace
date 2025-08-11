package org.example.marketplace.customerapp.client.impl;

import lombok.RequiredArgsConstructor;
import org.example.marketplace.customerapp.client.payload.NewProductReviewPayload;
import org.example.marketplace.customerapp.client.ProductReviewsClient;
import org.example.marketplace.customerapp.client.exception.ClientBadRequestException;
import org.example.marketplace.customerapp.entity.ProductReview;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
public class ProductReviewsClientImpl implements ProductReviewsClient {

    private final WebClient webClient;


    @Override
    public Flux<ProductReview> findProductReviewsByProductId(Long productId) {
        return this.webClient
                .get()
                .uri("feedback-api/product-reviews/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToFlux(ProductReview.class);
    }

    @Override
    public Mono<ProductReview> createProductReview(Long productId, String review, int rating) {
        return webClient.post()
                .uri("feedback-api/product-reviews")
                .bodyValue(new NewProductReviewPayload(productId, review, rating))
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(Map.class)
                                .flatMap(body -> Mono.error(new ClientBadRequestException(
                                        body.getOrDefault("message", "Validation failed").toString(),
                                        (Map<String, String>) body.getOrDefault("errors", Map.of())
                                )))
                )
                .bodyToMono(ProductReview.class);
    }
}
