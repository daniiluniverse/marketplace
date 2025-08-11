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
    public Mono<FavouriteProduct> addProductToFavorite(Product product) {

        return this.favouriteProductRepository.save(new FavouriteProduct(UUID.randomUUID(), product));
    }

    @Override
    public Mono<Void> removeProductFromFavorites(Long productId) {

        return this.favouriteProductRepository.delete(productId);

    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductsId(Long productId) {
        return this.favouriteProductRepository.findByProductId(productId);
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProduct() {
        return this.favouriteProductRepository.findAll();
    }


}
