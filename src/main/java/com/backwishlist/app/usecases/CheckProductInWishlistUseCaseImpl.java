package com.backwishlist.app.usecases;

import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.impl.CheckProductInWishlistUseCase;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckProductInWishlistUseCaseImpl implements CheckProductInWishlistUseCase {

    private final IWishlistRepository wishlistRepository;

    @Override
    public Wishlist execute(final String customerId, final String productId) {
        log.info("verificando se existe wishlist para o customer informado");
        final Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));

        log.info("verificando se o produto existe na wishlist");
        if (!wishlist.containsProduct(productId)) {
            throw new ProductNotFoundException(productId);
        }

        return wishlist;
    }

}
