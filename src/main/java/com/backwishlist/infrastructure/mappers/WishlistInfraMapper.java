package com.backwishlist.infrastructure.mappers;

import com.backwishlist.domain.Wishlist;
import com.backwishlist.infrastructure.database.documents.WishlistDocument;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class WishlistInfraMapper {

    private final ModelMapper modelMapper;

    public WishlistInfraMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public WishlistDocument toDocument(Wishlist wishlist) {
        return modelMapper.map(wishlist, WishlistDocument.class);
    }

    public Wishlist toDomain(WishlistDocument document) {
        return modelMapper.map(document, Wishlist.class);
    }


}
