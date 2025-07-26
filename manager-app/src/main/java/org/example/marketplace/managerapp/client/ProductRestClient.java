package org.example.marketplace.managerapp.client;

import org.example.marketplace.managerapp.dto.RequestProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Base64;

// Убираем @Component, так как бин создается в RestClientBeans
public class ProductRestClient {
    private final RestClient restClient;
    private final Logger log = LoggerFactory.getLogger(ProductRestClient.class);

    // Убираем @Autowired, так как это единственный конструктор
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
                    .onStatus(status -> status.isError(), (request, response) -> {
                        throw new HttpClientErrorException(response.getStatusCode(),
                                "Ошибка при создании продукта: " + response.getStatusText());
                    })
                    .toBodilessEntity();

            log.info("Успешно создан продукт: {}", product.name());
        } catch (HttpClientErrorException e) {
            log.error("Ошибка HTTP при создании продукта: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Неожиданная ошибка: {}", e.getMessage());
            throw new RuntimeException("Service unavailable", e);
        }
    }
}