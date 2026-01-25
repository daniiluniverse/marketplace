package org.example.marketplace.orderservs.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.orderservs.client.ProductRestClient;
import org.example.marketplace.orderservs.dto.CartData;
import org.example.marketplace.orderservs.dto.CartItemDTO;
import org.example.marketplace.orderservs.dto.OrderDTO;
import org.example.marketplace.orderservs.dto.cart.AddToCartRequest;
import org.example.marketplace.orderservs.entity.CartItem;
import org.example.marketplace.orderservs.entity.Order;
import org.example.marketplace.orderservs.entity.Product;
import org.example.marketplace.orderservs.repository.CartRedisRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@AllArgsConstructor
public class CartService {

    private final CartRedisRepository cartRepository;
    private final ProductRestClient productClient;
    private final OrderService orderService;

    public CartData getCart(Long userId) {
        return cartRepository.getCart(userId);
    }

    public CartData addToCart(Long userId, AddToCartRequest request) {
        // Получить информацию о продукте
        Product product = productClient.findProduct(request.getProductId());

        if (product == null) {
            throw new RuntimeException("Product not found: " + request.getProductId());
        }

        CartItemDTO cartItem = CartItemDTO.builder()
                .productId(product.id())
                .productName(product.name())
                .price(BigDecimal.valueOf(product.price()))
                .quantity(request.getQuantity())
                .build();


        // Добавить в Redis
        cartRepository.addItem(userId, cartItem);

        return cartRepository.getCart(userId);
    }

    public CartData updateItem(Long userId, Long productId, Integer quantity) {
        cartRepository.updateQuantity(userId, productId, quantity);
        return cartRepository.getCart(userId);
    }

    public void removeItem(Long userId, Long productId) {
        cartRepository.removeItem(userId, productId);
    }

    public void clearCart(Long userId) {
        cartRepository.deleteCart(userId);
    }

    // Оформить заказ из корзины
    public OrderDTO createOrderFromCart(Long userId, String address) {
        CartData cart = cartRepository.getCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Создать заказ
        OrderDTO order = orderService.createOrderFromCart(userId, address);

        // Очистить корзинуw
        cartRepository.deleteCart(userId);

        return order;
    }


}
