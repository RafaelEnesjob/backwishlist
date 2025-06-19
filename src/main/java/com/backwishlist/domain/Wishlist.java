package com.backwishlist.domain;

import com.backwishlist.domain.exceptions.ProductAlreadyExistsException;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistLimitExceededException;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Wishlist {

    private static final int MAX_PRODUCTS = 20;

    private String customerId;
    private List<Product> products;

    public static Wishlist create(final String clientId) {
        return new Wishlist(clientId, new ArrayList<>());
    }

    public void addProduct(final Product product) {
        validateProductLimit();
        validateProductNotExists(product);
        products.add(product);
    }

    public void removeProduct(final String productId) {
        Product productToRemove = findProductById(productId);
        products.remove(productToRemove);
    }

    public boolean containsProduct(final String productId) {
        return products.stream().anyMatch(p -> p.getId().equals(productId));
    }

    public List<Product> listProducts() {
        return Collections.unmodifiableList(products);
    }

    private void validateProductLimit() {
        if (products.size() >= MAX_PRODUCTS) {
            throw new WishlistLimitExceededException(MAX_PRODUCTS);
        }
    }

    private void validateProductNotExists(Product product) {
        if (products.stream().anyMatch(p -> p.getId().equals(product.getId()))) {
            throw new ProductAlreadyExistsException(product.getId());
        }
    }

    private Product findProductById(String productId) {
        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

}
