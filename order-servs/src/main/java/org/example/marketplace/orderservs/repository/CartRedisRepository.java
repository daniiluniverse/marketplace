package org.example.marketplace.orderservs.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.orderservs.dto.CartData;
import org.example.marketplace.orderservs.dto.CartItemDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
@Slf4j
public class CartRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CART_PREFIX = "cart:";
    private static final Duration CART_TTL = Duration.ofDays(7);

    public CartRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Сохранить корзину пользователя
    public void saveCart(Long userId, CartData cart) {
        String key = CART_PREFIX + userId;
        redisTemplate.opsForValue().set(key, cart, CART_TTL);
        log.debug("Cart saved for user {}: {} items", userId, cart.getItems().size());
    }

// Получить корзину пользователя (с защитой от null)
    public CartData getCart(Long userId) {
        String key = CART_PREFIX + userId;
        CartData cart = (CartData) redisTemplate.opsForValue().get(key);

        if (cart == null) {
            // Создаем новую корзину с инициализированным списком
            cart = new CartData();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());  // ← ИНИЦИАЛИЗИРУЕМ СПИСОК!
            cart.setUpdatedAt(LocalDateTime.now());
            return cart;
        }

        // Защита от null в items
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());  // ← ЕЩЁ РАЗ ПРОВЕРЯЕМ!
        }

        return cart;
    }
    // Удалить корзину
    public void deleteCart(Long userId) {
        String key = CART_PREFIX + userId;
        redisTemplate.delete(key);
        log.debug("Cart deleted for user {}", userId);
    }

    // Добавить товар в корзину
// Добавить товар в корзину (с защитой от null)
    public void addItem(Long userId, CartItemDTO item) {
        CartData cart = getCart(userId);

        // Гарантируем, что список не null
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        // Проверить, есть ли уже такой товар
        Optional<CartItemDTO> existingItem = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(item.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Обновить количество
            CartItemDTO foundItem = existingItem.get();
            foundItem.setQuantity(foundItem.getQuantity() + item.getQuantity());
        } else {
            // Добавить новый товар
            cart.getItems().add(item);
        }

        saveCart(userId, cart);
    }

    // Удалить товар из корзины
    public void removeItem(Long userId, Long productId) {
        CartData cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        saveCart(userId, cart);
    }

    // Обновить количество товара
    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        CartData cart = getCart(userId);

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    if (quantity <= 0) {
                        cart.getItems().remove(item);
                    } else {
                        item.setQuantity(quantity);
                    }
                });

        saveCart(userId, cart);
    }
}

