package org.example.marketplace.feedbackservise.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.marketplace.feedbackservise.dto.ProductReviewRequest;
import org.example.marketplace.feedbackservise.dto.ProductReviewResponse;
import org.example.marketplace.feedbackservise.service.ProductReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("feedback-api/product-reviews")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @GetMapping("by-product-id/{productId}")
    public Mono<List<ProductReviewResponse>> findProductReviewsByProductId(@PathVariable Long productId){
        return this.productReviewService.findProductReviewByProduct(productId)
                .map(productReview -> new ProductReviewResponse(productReview.getProductId(), productReview.getReview(), productReview.getRating()))
                .collectList();
    }

    @PostMapping()
    public Mono<ResponseEntity<ProductReviewResponse>> createProductReview(
            @Valid @RequestBody Mono<ProductReviewRequest> productReviewRequestMono, UriComponentsBuilder uriComponentsBuilder){

        return productReviewRequestMono
                .flatMap(review -> this.productReviewService.createProductReview(review.productId(), review.review(), review.rating()))
                .map(productReviewResponse -> new ProductReviewResponse(productReviewResponse.getProductId(), productReviewResponse.getReview(), productReviewResponse.getRating()))
                .map(response -> ResponseEntity.created(uriComponentsBuilder.replacePath("feedback-api/product-reviews/{productId}").build(response.productId())).body(response));
    }
}
