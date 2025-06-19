package com.backwishlist.domain.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("wishlist.product.not.found#" + productId);
    }
}

