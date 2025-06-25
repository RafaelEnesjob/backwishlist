package com.backwishlist.app.repositories;

import com.backwishlist.domain.Wishlist;

import java.util.Optional;

public interface IWishlistRepository {


    Optional<Wishlist> findByCustomerId(final String customerId);


    void save(final Wishlist wishlist);


    void deleteByCustomerId(final String customerId);

}
