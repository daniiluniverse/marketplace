package org.example.marketplace.feedbackservise.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.feedbackservise.dto.FavouriteProductRequest;
import org.example.marketplace.feedbackservise.dto.FavouriteProductResponse;
import org.example.marketplace.feedbackservise.entity.Product;
import org.example.marketplace.feedbackservise.service.FavouriteProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public Flux<FavouriteProductResponse> findFavouriteProducts(
            Mono<JwtAuthenticationToken> authenticationTokenMono
    ){
        log.info("Запрос избранных товаров");
        return
               authenticationTokenMono.flatMapMany(token ->  this.favouriteProductService.findFavouriteProduct(token.getToken().getSubject())
                .map(favouriteProduct ->
                        new FavouriteProductResponse(
                                favouriteProduct.getProduct().id(),
                                favouriteProduct.getProduct().name(),
                                favouriteProduct.getProduct().details(),
                                favouriteProduct.getProduct().price()))
                .doOnNext(fp -> log.info("Received from client: {}", fp)));
    }

    @GetMapping("by-product-id/{productId}")
    public Mono<FavouriteProductResponse> findFavouriteProductByProductId(
            Mono<JwtAuthenticationToken> authenticationTokenMono,
            @PathVariable Long productId){
        log.info("Запрос на избранный товар с id: {}", productId);
        return authenticationTokenMono.flatMap(token -> this.favouriteProductService.findFavouriteProductByProductsId(productId, token.getToken().getSubject())
                .map(favouriteProduct ->
                        new FavouriteProductResponse(
                                favouriteProduct.getProduct().id(),
                                favouriteProduct.getProduct().name(),
                                favouriteProduct.getProduct().details(),
                                favouriteProduct.getProduct().price()
                        )));
    }

    @PostMapping
    public Mono<ResponseEntity<FavouriteProductResponse>> addProductsToFavourite(
            Mono<JwtAuthenticationToken> authenticationTokenMono,
            @Valid @RequestBody Mono<FavouriteProductRequest> favouriteProductRequest,
            UriComponentsBuilder uriComponentsBuilder
            ){
        return authenticationTokenMono.flatMap(token ->
                favouriteProductRequest
                .flatMap(request -> this.favouriteProductService.addProductToFavorite(new Product(
                        request.getProductId(),
                        request.getName(),
                        request.getDetails(),
                        request.getPrice()
                ), token.getToken().getSubject())))

                .map(product -> new FavouriteProductResponse(
                        product.getProduct().id(),
                        product.getProduct().name(),
                        product.getProduct().details(),
                        product.getProduct().price()
                ))
                .map(response -> ResponseEntity.created(uriComponentsBuilder.replacePath("/feedback-api/favourite-products/{productId}").build(response.getProductId())).body(response));
    }

    @DeleteMapping("by-product-id/{productId}")
    public Mono<ResponseEntity<Void>> removeProductFromFavourites(
            Mono<JwtAuthenticationToken> authenticationTokenMono,
            @PathVariable Long productId){

        log.info("Запрос на удаление избранного товара с id: {}", productId);


        return authenticationTokenMono.flatMap(token ->
                this.favouriteProductService.removeProductFromFavorites(productId, token.getToken().getSubject())
                .then(Mono.just(ResponseEntity.noContent().build())));
    }









}
