package org.example.marketplace.orderservs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.orderservs.dto.CartData;
import org.example.marketplace.orderservs.dto.CartItemDTO;
import org.example.marketplace.orderservs.dto.OrderDTO;
import org.example.marketplace.orderservs.dto.cart.AddToCartRequest;
import org.example.marketplace.orderservs.dto.cart.CartItemResponse;
import org.example.marketplace.orderservs.dto.cart.CartResponse;
import org.example.marketplace.orderservs.dto.cart.CheckoutRequest;
import org.example.marketplace.orderservs.entity.CartItem;
import org.example.marketplace.orderservs.entity.Order;
import org.example.marketplace.orderservs.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{userId}/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
        CartData cart = cartService.getCart(userId);
        return ResponseEntity.ok(toResponse(cart));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long userId,
            @RequestBody AddToCartRequest request) {

        CartData cart = cartService.addToCart(userId, request);
        return ResponseEntity.ok(toResponse(cart));
    }

    @PutMapping("items/{productId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        CartData cart = cartService.updateItem(userId, productId, quantity);
        return ResponseEntity.ok(toResponse(cart));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        cartService.removeItem(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @PathVariable Long userId,
            @RequestBody CheckoutRequest request) {

        OrderDTO order = cartService.createOrderFromCart(userId, request.getAddress());
        return ResponseEntity.ok(order);
    }

    private CartResponse toResponse(CartData cart) {
        return CartResponse.builder()
                .userId(cart.getUserId())
                .items(cart.getItems().stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList()))
                .total(cart.getTotal())
                .itemCount(cart.getItems().size())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartItemResponse toItemResponse(CartItemDTO item) {
        return CartItemResponse.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}