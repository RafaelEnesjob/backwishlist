package com.backwishlist.app.usecases.impl;

import com.backwishlist.domain.Product;

public interface AddProductToWishlistUseCase {

    void execute(final String customerId, final Product product);

}
