package org.example.marketplace.feedbackservise.service;

import lombok.RequiredArgsConstructor;
import org.example.marketplace.feedbackservise.entity.FavouriteProduct;
import org.example.marketplace.feedbackservise.entity.Product;
import org.example.marketplace.feedbackservise.repository.FavouriteProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavouriteProductServiceImpl implements FavouriteProductService {

    private final FavouriteProductRepository favouriteProductRepository;



    @Override
    public Mono<FavouriteProduct> addProductToFavorite(Product product, String userId) {

        return this.favouriteProductRepository.save(new FavouriteProduct(UUID.randomUUID(), product, userId));
    }

    @Override
    public Mono<Void> removeProductFromFavorites(Long productId, String userId) {

        return this.favouriteProductRepository.deleteByProductIdAndUserId(productId, userId);

    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductsId(Long productId, String userId) {
        return this.favouriteProductRepository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProduct(String userId) {
        return this.favouriteProductRepository.findAllByUserId(userId);
    }


}
