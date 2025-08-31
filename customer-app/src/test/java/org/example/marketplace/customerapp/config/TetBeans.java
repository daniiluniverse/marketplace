package org.example.marketplace.customerapp.config;

import org.example.marketplace.customerapp.client.impl.FavouriteProductsClientImpl;
import org.example.marketplace.customerapp.client.impl.ProductReviewsClientImpl;
import org.example.marketplace.customerapp.client.impl.WebClientProductsClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;

@Configuration
public class TetBeans {


    @Bean
    @Primary
    public ReactiveClientRegistrationRepository clientRegistrationRepository(){
        return mock();
    }

    @Bean
    @Primary
   public ServerOAuth2AuthorizedClientRepository authorizedClientRepository(){
        return mock();
   }

    @Bean
    @Primary
    public WebClientProductsClientImpl mockWebClientProductsClient(){
        return new WebClientProductsClientImpl(WebClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }

    @Bean
    @Primary
    public FavouriteProductsClientImpl mockFavouriteProductsClient(){
        return new FavouriteProductsClientImpl(WebClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }

    @Bean
    @Primary
    public ProductReviewsClientImpl mockProductReviewsClient(){
        return new ProductReviewsClientImpl(WebClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }
}
