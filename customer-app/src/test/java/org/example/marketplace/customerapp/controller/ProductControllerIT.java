package org.example.marketplace.customerapp.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

@SpringBootTest
@AutoConfigureWebTestClient
@WireMockTest(httpPort = 54321)
class ProductControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void addProductToFavourites_RequestIsValidReturnsFavouriteProductResponse() {
        stubFor(get(urlPathEqualTo("/app/products/1"))
                .willReturn(okJson("""
                {
                    "id": 1,
                    "name": "iphone",
                    "details": "16gb",
                    "price": 300.0
                }
                """)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        stubFor(post(urlPathEqualTo("/feedback-api/favourite-products"))
                .withRequestBody(equalToJson("""
                {
                    "productId": 1,
                    "name": "iphone", 
                    "details": "16gb",
                    "price": 300.0
                }
                """, true, true))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .willReturn(ok()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                {
                    "productId": 1,
                    "name": "iphone",
                    "details": "16gb",
                    "price": 300.0
                }
                """)));

        this.webTestClient
                .mutateWith(mockUser())
                .post()
                .uri("/customer/products/1/favourite-product/add/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.productId").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("iphone")
                .jsonPath("$.details").isEqualTo("16gb")
                .jsonPath("$.price").isEqualTo(300.0);

        // Проверяем вызовы - исправленные URL
        verify(getRequestedFor(urlPathEqualTo("/app/products/1"))); // Конкретный URL
        verify(postRequestedFor(urlPathEqualTo("/feedback-api/favourite-products"))
                .withRequestBody(equalToJson("""
                {
                    "productId": 1,
                    "name": "iphone",
                    "details": "16gb",
                    "price": 300.0
                }
                """, true, true)));
    }

    @Test
    void addProductToFavourites_ProductNotFound_Returns404() {
        // Stub для 404 продукта
        stubFor(get(urlPathEqualTo("/app/products/999"))
                .willReturn(notFound()));

        this.webTestClient
                .mutateWith(mockUser())
                .post()
                .uri("/customer/products/999/favourite-product/add/")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404) // Стандартное поле Spring
                .jsonPath("$.error").isEqualTo("Not Found") // Стандартное поле Spring
                .jsonPath("$.path").isEqualTo("/customer/products/999/favourite-product/add/");  // ← Проверяем сообщение

        verify(getRequestedFor(urlPathEqualTo("/app/products/999")));
        verify(0, postRequestedFor(urlPathEqualTo("/feedback-api/favourite-products")));
    }



}