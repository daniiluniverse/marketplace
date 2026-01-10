package org.example.marketplace.orderservs.config;

import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.orderservs.client.ProductRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class ProductClientBeans {

    @Value("${catalogue.service.url}")
    private String baseUrl;

    @Bean("catalogueServiceRestClient")
    public RestClient catalogueServiceRestClient() {
        log.info("Создание RestClient с baseUrl: {}", baseUrl);
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();

    }

    @Bean
    public ProductRestClient productRestClient(RestClient catalogueServiceRestClient) {
        return new ProductRestClient(catalogueServiceRestClient);
    }
}
