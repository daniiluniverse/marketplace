package org.example.marketplace.orderservs.client;

import org.example.marketplace.orderservs.controller.OrderController;
import org.example.marketplace.orderservs.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ProductRestClient  {

    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(ProductRestClient.class);


    public ProductRestClient(RestClient restClient) {
        this.restClient = restClient;
    }


    public List<Product> findAllProducts() {
        return this.restClient.get()
                .uri("/api/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {});
    }

    public Product findProduct(Long id) {

        try {
            log.info("Получен запрос с id{}", id);
            return this.restClient
                    .get()
                    .uri("/app/products/{id}", id)
                    .retrieve()
                    .body(Product.class);

        } catch (HttpClientErrorException e) {
            log.error("Ошибка HTTP при создании продукта: {}", e.getMessage());
            throw e;
        }


    }


}
