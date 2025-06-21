package com.backwishlist.app.usecases;

import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.impl.GetAllProductsFromWishlistUseCase;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllProductsFromWishlistUseCaseImpl implements GetAllProductsFromWishlistUseCase {

    private final IWishlistRepository wishlistRepository;

    @Override
    public Wishlist execute(final String customerId) {
        log.info("Verificando se o customer informado existe para retornar todos os produtos desse customer. ");
        return wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));
    }


}
