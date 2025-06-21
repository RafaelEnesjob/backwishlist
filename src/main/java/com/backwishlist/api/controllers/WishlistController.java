package com.backwishlist.api.controllers;

import com.backwishlist.api.dtos.request.ProductRequest;
import com.backwishlist.app.usecases.impl.AddProductToWishlistUseCase;
import com.backwishlist.app.usecases.impl.RemoveProductFromWishlistUseCase;
import com.backwishlist.domain.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/wishlists")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist management endpoints")
public class WishlistController {

    private final AddProductToWishlistUseCase addProductToWishlistUseCase;
    private final RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;

    @Operation(summary = "Add a product to the customer's wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added successfully"),
            @ApiResponse(responseCode = "404", description = "Customer Wishlist not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Product already exists in wishlist or limit exceeded", content = @Content)
    })
    @PostMapping("/{customerId}/products")
    public ResponseEntity<Void> addProductToWishlist(@PathVariable final String customerId, @Valid @RequestBody final ProductRequest productRequest) {
        final Product product = Product.builder()
                .id(productRequest.id())
                .name(productRequest.name())
                .build();

        addProductToWishlistUseCase.execute(customerId, product);

        log.info("Product [{} - {}] added to customer's wishlist [{}]", product.getId(), product.getName(), customerId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Remove a product from the customer's wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed successfully"),
            @ApiResponse(responseCode = "404", description = "Wishlist or product not found", content = @Content)
    })
    @DeleteMapping("/{customerId}/products/{productId}")
    public ResponseEntity<Void> removeProductFromWishlist(
            @PathVariable final String customerId,
            @PathVariable final String productId
    ) {
        removeProductFromWishlistUseCase.execute(customerId, productId);
        log.info("Produto [{}] removido da wishlist do cliente [{}]", productId, customerId);
        return ResponseEntity.ok().build();
    }


}