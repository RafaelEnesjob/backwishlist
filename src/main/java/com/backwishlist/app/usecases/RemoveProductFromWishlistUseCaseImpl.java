package com.backwishlist.app.usecases;

import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.impl.RemoveProductFromWishlistUseCase;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RemoveProductFromWishlistUseCaseImpl implements RemoveProductFromWishlistUseCase {

    private final IWishlistRepository wishlistRepository;

    @Override
    public void execute(final String customerId, final String productId) {
        final Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));

        if (!wishlist.containsProduct(productId)) {
            throw new ProductNotFoundException(productId);
        }

        wishlist.removeProduct(productId);
        wishlistRepository.save(wishlist);

        log.info("Product [{}] removed from customer's wishlist [{}]", productId, customerId);
    }
}