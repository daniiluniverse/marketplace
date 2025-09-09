package org.example.marketplace.customerapp.controller;

import org.example.marketplace.customerapp.client.FavouriteProductsClient;
import org.example.marketplace.customerapp.client.ProductReviewsClient;
import org.example.marketplace.customerapp.client.ProductsClient;
import org.example.marketplace.customerapp.dto.ProductResponse;
import org.example.marketplace.customerapp.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    FavouriteProductsClient favouriteProductsClient;
    @Mock
    ProductsClient productsClient;
    @Mock
    ProductReviewsClient productReviewsClient;

    @InjectMocks
    ProductController productController;

    @Test
    @DisplayName("Return product")
    void getProduct_ProductExists_ReturnNotEmptyMono(){

        var product = new Product(1 , "Товар 1", "Описание товара 1", 100);
        var productResponse = new ProductResponse(1 , "Товар 1", "Описание товара 1", 100);

        when(this.productsClient.findProduct(1L)).thenReturn(Mono.just(product));

        StepVerifier.create(this.productController.getProduct(1L))
                .expectNext(productResponse)
                .expectComplete()
                .verify();


        verify(this.productsClient).findProduct(1L);
        verifyNoMoreInteractions(this.productsClient);
        verifyNoInteractions(this.favouriteProductsClient, this.productReviewsClient);



    }

}