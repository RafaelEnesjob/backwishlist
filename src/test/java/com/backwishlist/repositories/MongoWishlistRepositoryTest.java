package com.backwishlist.repositories;

import com.backwishlist.Util.WishlistTestDataFactory;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.infrastructure.database.documents.WishlistDocument;
import com.backwishlist.infrastructure.database.repositories.MongoWishlistRepository;
import com.backwishlist.infrastructure.database.repositories.WishListMongoRepository;
import com.backwishlist.infrastructure.mappers.WishlistInfraMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MongoWishlistRepositoryTest {

    @Mock
    private WishListMongoRepository wishListMongoRepository;

    @Mock
    private WishlistInfraMapper wishlistInfraMapper;

    @InjectMocks
    private MongoWishlistRepository mongoWishlistRepository;

    private static final String CUSTOMER_ID = "customer-1";

    @Test
    void ShouldSearchWishlistByCustomerId() {
        // Given
        final WishlistDocument document = new WishlistDocument();
        final Wishlist expectedWishlist = WishlistTestDataFactory.emptyWishlist("wishlist-1", CUSTOMER_ID);

        when(wishListMongoRepository.findByCustomerId(CUSTOMER_ID)).thenReturn(Optional.of(document));
        when(wishlistInfraMapper.toDomain(document)).thenReturn(expectedWishlist);

        // When
        final Optional<Wishlist> result = mongoWishlistRepository.findByCustomerId(CUSTOMER_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedWishlist, result.get());
        verify(wishListMongoRepository).findByCustomerId(CUSTOMER_ID);
        verify(wishlistInfraMapper).toDomain(document);
    }

    @Test
    void ShouldSaveWishlistCallingMapperAndDocumentRepository() {
        // Given
        final Wishlist wishlist = WishlistTestDataFactory.emptyWishlist("wishlist-1", CUSTOMER_ID);
        final WishlistDocument document = new WishlistDocument();

        when(wishlistInfraMapper.toDocument(wishlist)).thenReturn(document);

        // When
        mongoWishlistRepository.save(wishlist);

        // Then
        verify(wishlistInfraMapper).toDocument(wishlist);
        verify(wishListMongoRepository).save(document);
    }

    @Test
    void ShouldDeleteByCustomerId() {
        // When
        mongoWishlistRepository.deleteByCustomerId(CUSTOMER_ID);

        // Then
        verify(wishListMongoRepository).deleteById(CUSTOMER_ID);
    }
}
