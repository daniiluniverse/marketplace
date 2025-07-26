package org.example.marketplace.managerapp.client;


import org.example.marketplace.managerapp.security.OauthClientHttpRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientBeans {
    private final Logger log = LoggerFactory.getLogger(RestClientBeans.class);

    @Value("${catalogue.service.url}")
    private String baseUrl;

    // Переименуем бин, чтобы избежать конфликта
    @Bean("catalogueServiceRestClient")
    public RestClient catalogueServiceRestClient(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository,
            @Value("${catalogue.service.registration}") String registration) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(
                        new OauthClientHttpRequestInterceptor(
                                new DefaultOAuth2AuthorizedClientManager(
                                        clientRegistrationRepository,
                                        auth2AuthorizedClientRepository),
                                registration
                        )
                )
                .build();

    }

    @Bean
    public ProductRestClient productRestClient(RestClient catalogueServiceRestClient) {
        return new ProductRestClient(catalogueServiceRestClient);
    }
}