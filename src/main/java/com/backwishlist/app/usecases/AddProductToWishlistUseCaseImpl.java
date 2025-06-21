package com.backwishlist.app.usecases;

import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.impl.AddProductToWishlistUseCase;
import com.backwishlist.domain.Product;
import com.backwishlist.domain.Wishlist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddProductToWishlistUseCaseImpl implements AddProductToWishlistUseCase {

    private final IWishlistRepository wishlistRepository;

    @Override
    public void execute(final String customerId, final Product product) {
        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .map(existing -> {
                    Wishlist updated = existing.addProduct(product);
                    log.info("Product added to customer's wishlist: {}", customerId);
                    return updated;
                })
                .orElseGet(() -> {
                    Wishlist created = Wishlist.create(customerId, product);
                    log.info("Wishlist created and product added for customer: {}", customerId);
                    return created;
                });

        wishlistRepository.save(wishlist);
    }
}
