package com.backwishlist.domain.exceptions;

public class ProductAlreadyExistsException extends RuntimeException{

    public ProductAlreadyExistsException(final String productId) {
        super("wishlist.product.already.exists#" + productId);
    }
}
