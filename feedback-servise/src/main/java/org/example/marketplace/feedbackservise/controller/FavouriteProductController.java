package org.example.marketplace.feedbackservise.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.feedbackservise.dto.FavouriteProductRequest;
import org.example.marketplace.feedbackservise.dto.FavouriteProductResponse;
import org.example.marketplace.feedbackservise.entity.Product;
import org.example.marketplace.feedbackservise.service.FavouriteProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/feedback-api/favourite-products")
@RequiredArgsConstructor
@Slf4j
public class FavouriteProductController {

    private final FavouriteProductService favouriteProductService;

    @GetMapping
    public Flux<FavouriteProductResponse> findFavouriteProduct(){
        log.info("Запрос избранный товаров");
        return this.favouriteProductService.findFavouriteProduct()
                .map(favouriteProduct ->
                        new FavouriteProductResponse(favouriteProduct.getProduct().id(),
                                favouriteProduct.getProduct().name(),
                                favouriteProduct.getProduct().details(),
                                favouriteProduct.getProduct().price()));
    }

    @GetMapping("by-product-id/{productId}")
    public Mono<FavouriteProductResponse> findFavouriteProductByProductId(@PathVariable Long productId){
        log.info("Запрос на избранный товар с id: {}", productId);
        return this.favouriteProductService.findFavouriteProductByProductsId(productId)
                .map(favouriteProduct ->
                        new FavouriteProductResponse(
                                favouriteProduct.getProduct().id(),
                                favouriteProduct.getProduct().name(),
                                favouriteProduct.getProduct().details(),
                                favouriteProduct.getProduct().price()
                        ));
    }

    @PostMapping
    public Mono<ResponseEntity<FavouriteProductResponse>> addProductsToFavourite(
            @Valid @RequestBody Mono<FavouriteProductRequest> favouriteProductRequest,
            UriComponentsBuilder uriComponentsBuilder
            ){
        return favouriteProductRequest
                .flatMap(request -> this.favouriteProductService.addProductToFavorite(new Product(
                        request.getProductId(),
                        request.getName(),
                        request.getDetails(),
                        request.getPrice()
                )))

                .map(product -> new FavouriteProductResponse(
                        product.getProduct().id(),
                        product.getProduct().name(),
                        product.getProduct().details(),
                        product.getProduct().price()
                ))
                .map(response -> ResponseEntity.created(uriComponentsBuilder.replacePath("/feedback-api/favourite-products/{productId}").build(response.getProductId())).body(response));
    }

    @DeleteMapping("by-product-id/{productId}")
    public Mono<ResponseEntity<Void>> removeProductFromFavourites(@PathVariable Long productId){

        log.info("Запрос на удаление избранного товара с id: {}", productId);


        return this.favouriteProductService.removeProductFromFavorites(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }









}
