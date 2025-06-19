package com.backwishlist.domain.exceptions;

public class WishlistLimitExceededException extends RuntimeException {
    public WishlistLimitExceededException(int max) {
        super("wishlist.limit.reached#" + max);
    }
}

