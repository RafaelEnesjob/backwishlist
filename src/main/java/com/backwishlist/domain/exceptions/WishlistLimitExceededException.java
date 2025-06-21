package com.backwishlist.domain.exceptions;

public class WishlistLimitExceededException extends RuntimeException {
    public WishlistLimitExceededException(final String max) {
        super("wishlist.limit.reached#" + max);
    }
}

