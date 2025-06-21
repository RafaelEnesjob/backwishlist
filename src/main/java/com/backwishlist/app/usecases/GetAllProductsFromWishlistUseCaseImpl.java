package com.backwishlist.app.usecases;

import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.impl.GetAllProductsFromWishlistUseCase;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllProductsFromWishlistUseCaseImpl implements GetAllProductsFromWishlistUseCase {

    private final IWishlistRepository wishlistRepository;

    @Override
    public Wishlist execute(final String customerId) {
        return wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));
    }


}
