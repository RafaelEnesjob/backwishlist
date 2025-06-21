package com.backwishlist.app.usecases.impl;

public interface RemoveProductFromWishlistUseCase {
    void execute(final String customerId, final String productId);
}