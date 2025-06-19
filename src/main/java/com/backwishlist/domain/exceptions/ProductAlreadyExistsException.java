package com.backwishlist.domain.exceptions;

public class ProductAlreadyExistsException extends RuntimeException{

    public ProductAlreadyExistsException(String productId) {
        super("wishlist.product.already.exists#" + productId);
    }
}
