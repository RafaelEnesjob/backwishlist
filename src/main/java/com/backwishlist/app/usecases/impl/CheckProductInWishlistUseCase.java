package com.backwishlist.app.usecases.impl;

import com.backwishlist.domain.Wishlist;

public interface CheckProductInWishlistUseCase {

    Wishlist execute(final String customerId, final String productId);

}
