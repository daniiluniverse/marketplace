package org.example.marketplace.managerapp.config;

import lombok.RequiredArgsConstructor;
import org.example.marketplace.managerapp.client.ProductRestClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;


@TestConfiguration
@RequiredArgsConstructor
public class TestingBeans {



    @Bean
    @Primary
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration registration = ClientRegistration.withRegistrationId("keycloak")
                .clientId("test-client")
                .clientSecret("test-secret")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri("http://localhost/mock-token")
                .build();
        return new InMemoryClientRegistrationRepository(registration);
    }

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    @Primary
    RestClient testProductRestClient(
            @Value("${catalogue.service.url}") String serviceBaseUrl
    ){


        return  RestClient.builder()
                .baseUrl(serviceBaseUrl)
                .build();
    }
}