package org.example.marketplace.customerapp.client;

import org.example.marketplace.customerapp.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductsClient {

    Flux<Product> findAllProducts();

    Mono<Product> findProduct(Long id);
}
