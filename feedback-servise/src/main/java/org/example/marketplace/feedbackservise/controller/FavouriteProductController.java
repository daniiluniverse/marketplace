package org.example.marketplace.feedbackservise.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketplace.feedbackservise.dto.FavouriteProductRequest;
import org.example.marketplace.feedbackservise.dto.FavouriteProductResponse;
import org.example.marketplace.feedbackservise.entity.Product;
import org.example.marketplace.feedbackservise.service.FavouriteProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    @Operation(security = @SecurityRequirement(name = "keycloak"))
    public Flux<FavouriteProductResponse> findFavouriteProducts(
            @AuthenticationPrincipal Jwt jwt
    ){
        log.info("Запрос избранных товаров");
        return
              this.favouriteProductService.findFavouriteProduct(jwt.getSubject())
                .map(favouriteProduct ->
                        new FavouriteProductResponse(
                                favouriteProduct.getProduct().id(),
                                favouriteProduct.getProduct().name(),
                                favouriteProduct.getProduct().details(),
                                favouriteProduct.getProduct().price()))
                .doOnNext(fp -> log.info("Received from client: {}", fp));
    }

    @GetMapping("by-product-id/{productId}")
    @Operation(security = @SecurityRequirement(name = "keycloak"))
    public Flux<FavouriteProductResponse> findFavouriteProductByProductId(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId){
        log.info("Запрос на избранный товар с id: {}", productId);
       return this.favouriteProductService.findFavouriteProduct(jwt.getSubject())
                .map(favouriteProduct ->
                        new FavouriteProductResponse(
                                favouriteProduct.getProduct().id(),
                                favouriteProduct.getProduct().name(),
                                favouriteProduct.getProduct().details(),
                                favouriteProduct.getProduct().price()
                        ));
    }

    @PostMapping
    @Operation(security = @SecurityRequirement(name = "keycloak"))
    public Mono<ResponseEntity<FavouriteProductResponse>> addProductsToFavourite(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody Mono<FavouriteProductRequest> favouriteProductRequest,
            UriComponentsBuilder uriComponentsBuilder
            ){
        return
                favouriteProductRequest
                .flatMap(request -> this.favouriteProductService.addProductToFavorite(new Product(
                        request.getProductId(),
                        request.getName(),
                        request.getDetails(),
                        request.getPrice()
                ), jwt.getSubject()))

                .map(product -> new FavouriteProductResponse(
                        product.getProduct().id(),
                        product.getProduct().name(),
                        product.getProduct().details(),
                        product.getProduct().price()
                ))
                .map(response -> ResponseEntity.created(uriComponentsBuilder.replacePath("/feedback-api/favourite-products/{productId}").build(response.getProductId())).body(response));
    }

    @DeleteMapping("by-product-id/{productId}")
    @Operation(security = @SecurityRequirement(name = "keycloak"))
    public Mono<ResponseEntity<Void>> removeProductFromFavourites(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productId){

        log.info("Запрос на удаление избранного товара с id: {}", productId);


        return
                this.favouriteProductService.removeProductFromFavorites(productId, jwt.getSubject())
                .then(Mono.just(ResponseEntity.noContent().build()));
    }









}
