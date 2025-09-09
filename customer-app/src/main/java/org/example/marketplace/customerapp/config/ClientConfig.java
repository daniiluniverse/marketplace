package org.example.marketplace.customerapp.config;


import de.codecentric.boot.admin.client.config.ClientProperties;
import de.codecentric.boot.admin.client.registration.ReactiveRegistrationClient;
import de.codecentric.boot.admin.client.registration.RegistrationClient;
import org.example.marketplace.customerapp.client.impl.FavouriteProductsClientImpl;
import org.example.marketplace.customerapp.client.impl.ProductReviewsClientImpl;
import org.example.marketplace.customerapp.client.impl.WebClientProductsClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    @Scope("prototype")
    public WebClient.Builder marketServicesWebClientBuilder(ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                            ServerOAuth2AuthorizedClientRepository authorizedClientRepository){
        ServerOAuth2AuthorizedClientExchangeFilterFunction filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, authorizedClientRepository);
        filter.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
                .filter(filter);
    }

    @Bean
    public WebClientProductsClientImpl webClientProductsClient(
            @Value("${catalogue.service.url}") String baseUrl,
            WebClient.Builder marketServicesWebClientBuilder
    ){
        return new WebClientProductsClientImpl(marketServicesWebClientBuilder
                .baseUrl(baseUrl)
                .build());
    }

    @Bean
    public FavouriteProductsClientImpl favouriteProductsClient(
            @Value("${feedback.service.url}") String baseUrl,
            WebClient.Builder marketServicesWebClientBuilder
    ){
        return new FavouriteProductsClientImpl(marketServicesWebClientBuilder
                .baseUrl(baseUrl)
                .build());
    }

    @Bean
    public ProductReviewsClientImpl productReviewsClient(
            @Value("${feedback.service.url}") String baseUrl,
            WebClient.Builder marketServicesWebClientBuilder
    ){
        return new ProductReviewsClientImpl(marketServicesWebClientBuilder
                .baseUrl(baseUrl)
                .build());
    }

    @Bean
    public RegistrationClient registrationClient(ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                 ReactiveOAuth2AuthorizedClientService authorizedClientService,
                                                 ClientProperties clientProperties) {

        ServerOAuth2AuthorizedClientExchangeFilterFunction filter =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                        new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService)
                );

        filter.setDefaultClientRegistrationId("metrics");

        return new ReactiveRegistrationClient(WebClient.builder()
                .filter(filter)
                .build(), clientProperties.getReadTimeout());
    }


}
