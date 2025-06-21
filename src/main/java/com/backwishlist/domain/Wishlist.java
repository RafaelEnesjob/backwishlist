package com.backwishlist.domain;

import com.backwishlist.domain.exceptions.ProductAlreadyExistsException;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistLimitExceededException;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Wishlist {

    private static final int MAX_PRODUCTS = 20;

    private String id;
    private String customerId;
    private List<Product> products;
    private Instant createdAt;
    private Instant updatedAt;

    public List<Product> getProducts() {
        if (Objects.isNull(products)) {
            return new ArrayList<>();
        }
        return products;
    }

    public static Wishlist of(final String customerId, final Product product) {
        return new Wishlist(null, customerId, List.of(product), null, null);
    }

    public boolean isEmpty() {
        return this.getProducts().isEmpty();
    }

    public boolean canAddMoreProducts() {
        return products.size() < MAX_PRODUCTS;
    }

    public boolean containsProduct(final Product product) {
        return products.stream()
                .anyMatch(p -> p.getId().equals(product.getId()));
    }

    public boolean containsProduct(final String productId) {
        return products.stream()
                .anyMatch(p -> productId.equals(p.getId()));
    }

    public Wishlist addProduct(final Product product) {

        if (this.containsProduct(product)) {
            throw new ProductAlreadyExistsException(product.getId());
        }

        if (Boolean.FALSE.equals(this.canAddMoreProducts())) {
            throw new WishlistLimitExceededException(this.customerId);
        }

        final List<Product> productUpdate = new ArrayList<>(this.products);
        productUpdate.add(product);

        return new Wishlist(
                this.id,
                this.customerId,
                productUpdate,
                this.createdAt,
                this.updatedAt
        );
    }

    public Wishlist removeProduct(final String productId) {
        final boolean productRemoved = this.getProducts().removeIf(product -> product.getId().equals(productId));

        if (productRemoved) {
            return new Wishlist(
                    this.id,
                    this.customerId,
                    products,
                    this.createdAt,
                    this.updatedAt
            );
        }

        throw new ProductNotFoundException(productId);
    }
}