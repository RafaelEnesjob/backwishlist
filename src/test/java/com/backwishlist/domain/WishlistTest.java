package com.backwishlist.domain;

import com.backwishlist.domain.exceptions.ProductAlreadyExistsException;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistLimitExceededException;
import com.backwishlist.Util.WishlistTestDataFactory;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WishlistTest {

    @Test
    void GetProductsShouldReturnEmptyListWhenProductsForNull() {
        final Wishlist wishlist = Wishlist.builder()
                .id(WishlistTestDataFactory.DEFAULT_WISHLIST_ID)
                .customerId(WishlistTestDataFactory.DEFAULT_CUSTOMER_ID)
                .products(null)
                .build();

        final List<Product> products = wishlist.getProducts();

        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    void IsEmptyMustReturnTrueWhenProductsForNull() {
        final Wishlist wishlist = Wishlist.builder()
                .id(WishlistTestDataFactory.DEFAULT_WISHLIST_ID)
                .customerId(WishlistTestDataFactory.DEFAULT_CUSTOMER_ID)
                .products(null)
                .build();

        assertTrue(wishlist.isEmpty());
    }

    @Test
    void AddProductMustAddProductWithSuccessfully() {
        final Wishlist wishlist = WishlistTestDataFactory.emptyWishlist(
                WishlistTestDataFactory.DEFAULT_WISHLIST_ID,
                WishlistTestDataFactory.DEFAULT_CUSTOMER_ID
        );

        final Product product = WishlistTestDataFactory.defaultProduct();

        final Wishlist updatedWishlist = wishlist.addProduct(product);

        assertTrue(updatedWishlist.containsProduct(product.getId()));
    }

    @Test
    void AddProductShouldThrowExceptionWhenProductAlreadyExists() {
        final Product product = WishlistTestDataFactory.defaultProduct();

        final Wishlist wishlist = WishlistTestDataFactory.wishlistWithProduct(
                WishlistTestDataFactory.DEFAULT_WISHLIST_ID,
                WishlistTestDataFactory.DEFAULT_CUSTOMER_ID,
                product
        );

        assertThrows(ProductAlreadyExistsException.class, () -> wishlist.addProduct(product));
    }

    @Test
    void AddProductShouldThrowExceptionWhenLimitExceeded() {
        final Wishlist fullWishlist = WishlistTestDataFactory.fullWishlist(
                WishlistTestDataFactory.DEFAULT_WISHLIST_ID,
                WishlistTestDataFactory.DEFAULT_CUSTOMER_ID
        );

        final Product extraProduct = WishlistTestDataFactory.createProduct("p21", "Extra Product");

        assertThrows(WishlistLimitExceededException.class, () -> fullWishlist.addProduct(extraProduct));
    }

    @Test
    void RemoveProductMustRemoveProductSuccessfully() {
        final Product product = WishlistTestDataFactory.defaultProduct();

        final Wishlist wishlist = WishlistTestDataFactory.wishlistWithProduct(
                WishlistTestDataFactory.DEFAULT_WISHLIST_ID,
                WishlistTestDataFactory.DEFAULT_CUSTOMER_ID,
                product
        );

        final Wishlist updatedWishlist = wishlist.removeProduct(product.getId());

        assertFalse(updatedWishlist.containsProduct(product.getId()));
    }

    @Test
    void RemoveProductShouldThrowExceptionWhenProductDoesNotExist() {
        final Wishlist wishlist = WishlistTestDataFactory.emptyWishlist(
                WishlistTestDataFactory.DEFAULT_WISHLIST_ID,
                WishlistTestDataFactory.DEFAULT_CUSTOMER_ID
        );

        assertThrows(ProductNotFoundException.class, () -> wishlist.removeProduct("non-existing-product-id"));
    }
}
