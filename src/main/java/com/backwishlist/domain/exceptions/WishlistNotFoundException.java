package com.backwishlist.domain.exceptions;

public class WishlistNotFoundException extends RuntimeException {

    public WishlistNotFoundException(final String customerId) {
        super("Wishlist not found for customer: " + customerId);
    }
}
