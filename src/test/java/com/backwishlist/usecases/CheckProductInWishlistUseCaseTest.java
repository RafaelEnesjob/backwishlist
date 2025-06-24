package com.backwishlist.usecases;

import com.backwishlist.Util.WishlistTestDataFactory;
import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.CheckProductInWishlistUseCaseImpl;
import com.backwishlist.domain.Product;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckProductInWishlistUseCaseTest {

    @Mock
    private IWishlistRepository wishlistRepository;

    @InjectMocks
    private CheckProductInWishlistUseCaseImpl useCase;

    private static final String CUSTOMER_ID = WishlistTestDataFactory.DEFAULT_CUSTOMER_ID;
    private static final String WISHLIST_ID = WishlistTestDataFactory.DEFAULT_WISHLIST_ID;
    private static final String PRODUCT_ID = WishlistTestDataFactory.DEFAULT_PRODUCT_ID;
    private static final String PRODUCT_NAME = WishlistTestDataFactory.DEFAULT_PRODUCT_NAME;
    private static final Product PRODUCT = WishlistTestDataFactory.createProduct(PRODUCT_ID, PRODUCT_NAME);

    @Test
    void ShouldReturnWishlistWhenProductExistsInWishlist() {
        final Wishlist wishlist = WishlistTestDataFactory.wishlistWithProduct(WISHLIST_ID, CUSTOMER_ID, PRODUCT);
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        final Wishlist result = useCase.execute(CUSTOMER_ID, PRODUCT_ID);

        assertEquals(wishlist, result);
    }

    @Test
    void ShouldThrowExceptionWhenWishlistDoesNotExist() {
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(WishlistNotFoundException.class, () -> useCase.execute(CUSTOMER_ID, PRODUCT_ID));
    }

    @Test
    void ShouldThrowExceptionWhenProductDoesNotExistInWishlist() {
        final Wishlist wishlist = WishlistTestDataFactory.emptyWishlist(WISHLIST_ID, CUSTOMER_ID);
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        assertThrows(ProductNotFoundException.class, () -> useCase.execute(CUSTOMER_ID, PRODUCT_ID));
    }
}