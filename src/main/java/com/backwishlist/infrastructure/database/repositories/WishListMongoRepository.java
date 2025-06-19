package com.backwishlist.infrastructure.database.repositories;


import com.backwishlist.infrastructure.database.documents.WishlistDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishListMongoRepository extends MongoRepository<WishlistDocument, String> {

    Optional<WishlistDocument> findByCustomerId(final String customerId);
}
