package com.backwishlist.app.usecases.impl;

import com.backwishlist.domain.Wishlist;

public interface GetAllProductsFromWishlistUseCase {
    Wishlist execute(final String customerId);

}
