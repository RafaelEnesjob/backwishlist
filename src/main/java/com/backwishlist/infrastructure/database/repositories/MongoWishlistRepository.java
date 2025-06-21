package com.backwishlist.infrastructure.database.repositories;

import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.infrastructure.mappers.WishlistInfraMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class MongoWishlistRepository implements IWishlistRepository {

    private final WishlistInfraMapper wishlistInfraMapper;
    private final WishListMongoRepository wishListMongoRepository;

    @Override
    public Optional<Wishlist> findByCustomerId(final String customerId) {
        log.info("Searching for customer wishlist: {}", customerId);
        return wishListMongoRepository.findByCustomerId(customerId)
                .map(wishlistInfraMapper::toDomain);
    }

    @Override
    public void save(final Wishlist wishlist) {
        log.info("Persisting customer wishlist: {}", wishlist.getCustomerId());
        var document = wishlistInfraMapper.toDocument(wishlist);
        wishListMongoRepository.save(document);
    }


    @Override
    public boolean existsByCustomerId(String customerId) {
        return false;
    }

    @Override
    public void deleteByCustomerId(final String id) {
        log.info("Removing wishlist by id: {}", id);
        wishListMongoRepository.deleteById(id);
    }
}
