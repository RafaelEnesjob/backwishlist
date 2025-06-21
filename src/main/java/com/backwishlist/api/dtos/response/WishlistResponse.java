package com.backwishlist.api.dtos.response;



import java.util.List;


public record WishlistResponse(
        String id,
        String customerId,
        List<ProductResponse> products
) {
    public static WishlistResponse of(final String customerId) {
        return new WishlistResponse(null, customerId, List.of());
    }
}
