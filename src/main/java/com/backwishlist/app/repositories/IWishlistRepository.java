package com.backwishlist.app.repositories;

import com.backwishlist.domain.Wishlist;

import java.util.Optional;

public interface IWishlistRepository {


    Optional<Wishlist> findByCustomerId(String customerId);


    void save(Wishlist wishlist);


    void deleteByCustomerId(String customerId);

}
