package org.example.marketplace.customerapp.client.impl;

import lombok.RequiredArgsConstructor;
import org.example.marketplace.customerapp.client.ProductsClient;
import org.example.marketplace.customerapp.entity.Product;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class WebClientProductsClientImpl implements ProductsClient {

    private final WebClient webClient;

    @Override
    public Flux<Product> findAllProducts() {
        return this.webClient.get()
                .uri("/app/products")
                .retrieve()
                .bodyToFlux(Product.class);
    }

    @Override
    public Mono<Product> findProduct(Long id) {
        return this.webClient.get()
                .uri("/app/products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
