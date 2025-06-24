package com.backwishlist.Util;

import com.backwishlist.domain.Product;
import com.backwishlist.domain.Wishlist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public class WishlistTestDataFactory {

    public static final String DEFAULT_CUSTOMER_ID = "customer-1";
    public static final String DEFAULT_PRODUCT_ID = "product-1";
    public static final String DEFAULT_PRODUCT_NAME = "Tênis Nike";
    public static final String DEFAULT_WISHLIST_ID = "wishlist-1";

    public static Product createProduct(final String id, final String name) {
        return Product.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static Product defaultProduct() {
        return createProduct(DEFAULT_PRODUCT_ID, DEFAULT_PRODUCT_NAME);
    }

    public static Wishlist emptyWishlist(final String wishlistId, final String customerId) {
        return Wishlist.builder()
                .id(wishlistId)
                .customerId(customerId)
                .products(new ArrayList<>())
                .build();
    }

    public static Wishlist wishlistWithProduct(final String id, final String customerId, final Product product) {
        return Wishlist.builder()
                .id(id)
                .customerId(customerId)
                .products(new ArrayList<>(List.of(product)))
                .build();
    }

    public static Wishlist fullWishlist(final String wishlistId, final String customerId) {
        final List<Product> fullList = IntStream.range(0, 20)
                .mapToObj(i -> createProduct("p" + i, "Product " + i))
                .toList();

        return Wishlist.builder()
                .id(wishlistId)
                .customerId(customerId)
                .products(fullList)
                .build();
    }
}