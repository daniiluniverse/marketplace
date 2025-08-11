package org.example.marketplace.customerapp.config;


import org.example.marketplace.customerapp.client.impl.FavouriteProductsClientImpl;
import org.example.marketplace.customerapp.client.impl.ProductReviewsClientImpl;
import org.example.marketplace.customerapp.client.impl.WebClientProductsClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClientProductsClientImpl webClientProductsClient(
            @Value("${catalogue.service.url}") String baseUrl
    ){
        return new WebClientProductsClientImpl(WebClient.builder()
                .baseUrl(baseUrl)
                .build());
    }

    @Bean
    public FavouriteProductsClientImpl favouriteProductsClient(
            @Value("${feedback.service.url}") String baseUrl
    ){
        return new FavouriteProductsClientImpl(WebClient.builder()
                .baseUrl(baseUrl)
                .build());
    }

    @Bean
    public ProductReviewsClientImpl productReviewsClient(
            @Value("${feedback.service.url}") String baseUrl
    ){
        return new ProductReviewsClientImpl(WebClient.builder()
                .baseUrl(baseUrl)
                .build());
    }
}
