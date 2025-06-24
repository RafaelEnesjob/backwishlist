package com.backwishlist.usecases;

import com.backwishlist.Util.WishlistTestDataFactory;
import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.GetAllProductsFromWishlistUseCaseImpl;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllProductsFromWishlistUseCaseTest {

    @Mock
    private IWishlistRepository wishlistRepository;

    @InjectMocks
    private GetAllProductsFromWishlistUseCaseImpl useCase;

    private static final String CUSTOMER_ID = WishlistTestDataFactory.DEFAULT_CUSTOMER_ID;
    private static final String WISHLIST_ID = WishlistTestDataFactory.DEFAULT_WISHLIST_ID;

    @Test
    void ShouldReturnWishlistWhenItExistsToCustomer() {
        final Wishlist wishlist = WishlistTestDataFactory.emptyWishlist(WISHLIST_ID, CUSTOMER_ID);
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(wishlist));

        final Wishlist result = useCase.execute(CUSTOMER_ID);

        assertEquals(wishlist, result);
        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
    }

    @Test
    void ShouldThrowExceptionWhenWishlistDoesNotExist() {
        when(wishlistRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(WishlistNotFoundException.class, () -> useCase.execute(CUSTOMER_ID));
        verify(wishlistRepository).findByCustomerId(CUSTOMER_ID);
    }
}
