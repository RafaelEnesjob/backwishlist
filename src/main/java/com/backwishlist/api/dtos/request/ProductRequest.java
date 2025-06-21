package com.backwishlist.api.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        @NotBlank(message = "Product ID is mandatory")
        String id,
        @NotBlank(message = "Name is mandatory")
        String name
) {
}