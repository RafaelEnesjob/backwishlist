package com.backwishlist.app.usecases.impl;

public interface RemoveProductFromWishlistUseCase {
    void execute(String customerId, String productId);
}