package org.example.marketplace.orderservs.controller;

import org.example.marketplace.orderservs.dto.CartData;
import org.example.marketplace.orderservs.dto.CartItemDTO;
import org.example.marketplace.orderservs.dto.cart.AddToCartRequest;
import org.example.marketplace.orderservs.dto.cart.CartItemResponse;
import org.example.marketplace.orderservs.dto.cart.CartResponse;
import org.example.marketplace.orderservs.entity.Product;
import org.example.marketplace.orderservs.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartControllerUnitTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Test
    void getCart_Success(){

        //given

        Long userId = 1L;

        Product product = new Product(1L, "iPhone 15 Pro", "Test details",1299.99);

        CartItemResponse cartItemResponse = CartItemResponse.builder()
                .productId(product.id())
                .productName(product.name())
                .price(BigDecimal.valueOf(product.price()))
                .quantity(1)
                .subtotal(BigDecimal.valueOf(1299.99))
                .build();


        CartItemDTO cartItemDTO = CartItemDTO.builder()
                .productId(product.id())
                .productName(product.name())
                .price(BigDecimal.valueOf(product.price()))
                .quantity(1)
                .build();

        CartData cartData = CartData.builder()
                .userId(userId)
                .items(List.of(cartItemDTO))
                .updatedAt(LocalDateTime.now())
                .build();

        when(cartService.getCart(eq(userId))).thenReturn(cartData);

        CartResponse cartResponse = CartResponse.builder()
                .userId(userId)
                .items(List.of(cartItemResponse))
                .total(BigDecimal.valueOf(1299.99))
                .itemCount(1)
                .updatedAt(LocalDateTime.now())
                .build();

        //when

        ResponseEntity<CartResponse> response = cartController.getCart(userId);


        //then

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        CartResponse responseBody = response.getBody();
        assertEquals(userId, responseBody.getUserId());
        assertEquals(1, responseBody.getItemCount());
        assertEquals(BigDecimal.valueOf(1299.99), responseBody.getTotal());
        assertEquals(1, response.getBody().getItems().size());


        verify(cartService).getCart(eq(userId));

    }
}
