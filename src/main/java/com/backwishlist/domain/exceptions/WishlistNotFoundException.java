package com.backwishlist.domain.exceptions;

public class WishlistNotFoundException extends RuntimeException {

    public WishlistNotFoundException(final String customerId) {
        super("Wishlist não encontrada para o cliente. Isso indica problema de consistência de dados" + customerId);
    }
}
