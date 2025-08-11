package org.example.marketplace.feedbackservise.repository;

import org.example.marketplace.feedbackservise.entity.FavouriteProduct;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Repository
public class InMemoryFavouriteProductRepositoryImpl implements FavouriteProductRepository {


    private final List<FavouriteProduct> favouriteProducts = Collections.synchronizedList(new LinkedList<>());


    @Override
    public Mono<Void> delete(Long productId) {
         this.favouriteProducts.removeIf(favouriteProduct -> favouriteProduct.getProduct().id() == productId);
         return Mono.empty();
    }

    @Override
    public Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct) {
        this.favouriteProducts.add(favouriteProduct);
        return Mono.just(favouriteProduct);
    }

    @Override
    public Mono<FavouriteProduct> findByProductId(Long productId) {
        return Flux.fromIterable(this.favouriteProducts)
                .filter(favouriteProduct -> favouriteProduct.getProduct().id() == productId)
                .singleOrEmpty();
    }

    @Override
    public Flux<FavouriteProduct> findAll() {
        return Flux.fromIterable(this.favouriteProducts);
    }


}
