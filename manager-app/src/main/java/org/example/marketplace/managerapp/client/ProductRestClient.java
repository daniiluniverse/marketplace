package org.example.marketplace.managerapp.client;

import org.example.marketplace.managerapp.dto.RequestProduct;
import org.example.marketplace.managerapp.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

public class ProductRestClient {
    private final RestClient restClient;
    private final Logger log = LoggerFactory.getLogger(ProductRestClient.class);

    public ProductRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void newProduct(RequestProduct product) {
        try {
            log.info("Отправка продукта: {}", product.name());

            restClient.post()
                    .uri("/app/products/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(product)
                    .retrieve()
                    .toBodilessEntity();

            log.info("продукт {}: успешно создан ", product.name());

        } catch (HttpClientErrorException e) {
            log.error("Ошибка HTTP при создании продукта: {}", e.getMessage());
            throw e;
        }
    }

    public void updateProduct(Long id, RequestProduct product){

        try {
            restClient.put()
                    .uri("/app/products/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(product)
                    .retrieve()
                    .toBodilessEntity();

            log.info("продукт {}: успешно обновлен ", product.name());

        } catch (HttpClientErrorException e) {
            log.error("Ошибка HTTP при обновлении продукта: {}", e.getMessage());
            throw e;

        }
    }

    public Product getProduct(Long id){

        try {
            return restClient.get()
                    .uri("/app/products/{id}", id)
                    .retrieve()
                    .body(Product.class);

        } catch (HttpClientErrorException e) {
            log.error("Ошибка HTTP при получении продукта: {}", e.getMessage());
            throw e;
        }
    }

    public List<Product> getAllProducts(){

        try {
            return restClient.get()
                    .uri("/app/products")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Product>>() {});

        } catch (HttpClientErrorException e) {
            log.error("Ошибка HTTP при получении продуктов: {}", e.getMessage());
            throw e;
        }

    }

    public void deleteProduct(Long id){
        try {
            restClient.delete()
                    .uri("/app/products/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Продукт успешно удален");

        } catch (HttpClientErrorException e) {
            log.error("Ошибка HTTP при удалении продукта: {}", e.getMessage());
            throw e;
        }

    }
}