package com.backwishlist.usecases;

import com.backwishlist.Util.WishlistTestDataFactory;
import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.AddProductToWishlistUseCaseImpl;
import com.backwishlist.domain.Product;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.ProductAlreadyExistsException;
import com.backwishlist.domain.exceptions.WishlistLimitExceededException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProductToWishlistUseCaseTest {

    @Mock
    private IWishlistRepository wishlistRepository;

    @InjectMocks
    private AddProductToWishlistUseCaseImpl useCase;

    private static final String CUSTOMER_ID = WishlistTestDataFactory.DEFAULT_CUSTOMER_ID;
    private static final String WISHLIST_ID = WishlistTestDataFactory.DEFAULT_WISHLIST_ID;
    private static final String PRODUCT_ID = WishlistTestDataFactory.DEFAULT_PRODUCT_ID;
    private static final String PRODUCT_NAME = WishlistTestDataFactory.DEFAULT_PRODUCT_NAME;
    private static final Product PRODUCT = WishlistTestDataFactory.createProduct(PRODUCT_ID, PRODUCT_NAME);

    @Test
    void shouldAddProductToExistingWishlist() {
        final Wishlist wishlist = WishlistTestDataFactory.emptyWishlist(WISHLIST_ID, CUSTOMER_ID);
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        useCase.execute(CUSTOMER_ID, PRODUCT);

        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void shouldCreateExistingWishListAndAddProduct() {
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        useCase.execute(CUSTOMER_ID, PRODUCT);

        ArgumentCaptor<Wishlist> wishlistCaptor = ArgumentCaptor.forClass(Wishlist.class);
        verify(wishlistRepository).save(wishlistCaptor.capture());

        final Wishlist saved = wishlistCaptor.getValue();
        assertEquals(CUSTOMER_ID, saved.getCustomerId());
        assertTrue(saved.containsProduct(PRODUCT));
    }

    @Test
    void ShouldLaunchExceptionWhenProductJaExistsOnWishlist() {
        final Wishlist wishlist = WishlistTestDataFactory.wishlistWithProduct(WISHLIST_ID, CUSTOMER_ID, PRODUCT);
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        assertThrows(ProductAlreadyExistsException.class, () -> useCase.execute(CUSTOMER_ID, PRODUCT));

        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void shouldLaunchExceptionWhenLimitDeProductsExceeded() {
        final Wishlist wishlist = WishlistTestDataFactory.fullWishlist(WISHLIST_ID, CUSTOMER_ID);
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        final Product extraProduct = WishlistTestDataFactory.createProduct("p21", "Extra Product");
        assertThrows(WishlistLimitExceededException.class, () -> useCase.execute(CUSTOMER_ID, extraProduct));

        verify(wishlistRepository, never()).save(any());
    }
}