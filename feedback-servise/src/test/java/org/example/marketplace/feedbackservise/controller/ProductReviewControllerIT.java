package org.example.marketplace.feedbackservise.controller;

import org.example.marketplace.feedbackservise.dto.ProductReviewRequest;
import org.example.marketplace.feedbackservise.dto.ProductReviewResponse;
import org.example.marketplace.feedbackservise.entity.ProductReview;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;


import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class ProductReviewControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    void findProductReviewsByProductId_ReturnsReviews() {
        //when

        reactiveMongoTemplate.dropCollection(ProductReview.class).block();

        // Сохранение тестовых данных (используем Entity)
        List<ProductReview> testReviews = List.of(
                new ProductReview(UUID.randomUUID(), 1L, "Отзыв 1", 5, "user1"),
                new ProductReview(UUID.randomUUID(), 1L, "Отзыв 2", 4, "user2"),
                new ProductReview(UUID.randomUUID(), 2L, "Отзыв 3", 3, "user1") // Этот не должен вернуться
        );

        reactiveMongoTemplate.insertAll(testReviews).blockLast();

        // Выполнение запроса и проверка
        webTestClient.mutateWith(mockJwt())
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductReviewResponse.class)
                .hasSize(2) // Ожидаем только 2 отзыва для productId=1
                .consumeWith(response -> {
                    List<ProductReviewResponse> reviews = response.getResponseBody();
                    assertNotNull(reviews);
                    assertEquals(1L, reviews.get(0).productId());
                    assertEquals(1L, reviews.get(1).productId());
                });
    }

    @Test
    void createProductReview_RequestIsValid_ReturnsCreatedProductReview() {

        //given
        ProductReviewRequest request = new ProductReviewRequest(1L, "Хороший товар 1", 5);
        //when
        this.webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.productId").isEqualTo(request.productId())
                .jsonPath("$.review").isEqualTo(request.review())
                .jsonPath("$.rating").isEqualTo(request.rating())
                .consumeWith(document("feedback/product_reviews/create_product_review",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productId").type("long").description("Идентификатор товара"),
                                fieldWithPath("review").type("string").description("Отзыв"),
                                fieldWithPath("rating").type("int").description("Оценка")
                        ),
                        responseFields(
                                fieldWithPath("productId").type("long").description("Идентификатор товара"),
                                fieldWithPath("review").type("string").description("Отзыв"),
                                fieldWithPath("rating").type("int").description("Оценка")
                        )));



        //then

    }

    @Test
    void createProductReview_RequestIsInvalid_ReturnsWebExchangeBindException(){
        ProductReviewRequest request = new ProductReviewRequest(1L, "Хороший товар 1", 20);

        this.webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.errors").isMap() ;

    }

    @Test
    void createProductReview_UserIsNotAuthenticated_ReturnsNotAuthorized(){
        this.webTestClient
                .post()
                .uri("/feedback-api/product-reviews")
                .exchange()
                .expectStatus().isUnauthorized();
    }


}