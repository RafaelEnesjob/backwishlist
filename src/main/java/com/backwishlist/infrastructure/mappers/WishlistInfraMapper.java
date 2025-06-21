package com.backwishlist.infrastructure.mappers;

import com.backwishlist.domain.Wishlist;
import com.backwishlist.infrastructure.database.documents.WishlistDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WishlistInfraMapper {

    WishlistDocument toDocument(final Wishlist wishlist);

    Wishlist toDomain(final WishlistDocument document);

}
