package com.backwishlist.domain.exceptions;

public class WishlistLimitExceededException extends RuntimeException {
    public WishlistLimitExceededException(final String max) {
        super("Wishlist has reached the maximum limit of 20 products: " + max);
    }
}

