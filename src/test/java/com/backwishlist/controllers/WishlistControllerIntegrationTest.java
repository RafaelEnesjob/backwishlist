package com.backwishlist.controllers;

import com.backwishlist.Util.WishlistTestDataFactory;
import com.backwishlist.api.dtos.request.ProductRequest;
import com.backwishlist.app.usecases.impl.AddProductToWishlistUseCase;
import com.backwishlist.app.usecases.impl.CheckProductInWishlistUseCase;
import com.backwishlist.app.usecases.impl.GetAllProductsFromWishlistUseCase;
import com.backwishlist.app.usecases.impl.RemoveProductFromWishlistUseCase;
import com.backwishlist.domain.Product;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WishlistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AddProductToWishlistUseCase addProductToWishlistUseCase;

    @MockBean
    private RemoveProductFromWishlistUseCase removeProductFromWishlistUseCase;

    @MockBean
    private GetAllProductsFromWishlistUseCase getAllProductsFromWishlistUseCase;

    @MockBean
    private CheckProductInWishlistUseCase checkProductInWishlistUseCase;

    private static final String BASE_URL = "/api/v1/wishlists";
    private static final String CUSTOMER_ID = WishlistTestDataFactory.DEFAULT_CUSTOMER_ID;
    private static final String PRODUCT_ID = WishlistTestDataFactory.DEFAULT_PRODUCT_ID;

    @Test
    void ShouldAddProductOnWishlistWithSuccessfully() throws Exception {
        final ProductRequest request = new ProductRequest(PRODUCT_ID, "Tênis Nike");

        mockMvc.perform(post(BASE_URL + "/{customerId}/products", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(addProductToWishlistUseCase).execute(eq(CUSTOMER_ID), any(Product.class));
    }

    @Test
    void ShouldReturnAllProductsDaWishlistSuccessfully() throws Exception {
        final Wishlist wishlist = WishlistTestDataFactory.wishlistWithProduct("wishlist-1", CUSTOMER_ID, WishlistTestDataFactory.defaultProduct());

        when(getAllProductsFromWishlistUseCase.execute(CUSTOMER_ID)).thenReturn(wishlist);

        mockMvc.perform(get(BASE_URL + "/{customerId}/products", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.products[0].id").value(PRODUCT_ID));
    }

    @Test
    void ShouldReturn404WhenWishlistDoesNotExist() throws Exception {
        when(getAllProductsFromWishlistUseCase.execute(CUSTOMER_ID)).thenThrow(new WishlistNotFoundException(CUSTOMER_ID));

        mockMvc.perform(get(BASE_URL + "/{customerId}/products", CUSTOMER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void ShouldRemoveProductDaWishlistSuccessfully() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isOk());

        verify(removeProductFromWishlistUseCase).execute(CUSTOMER_ID, PRODUCT_ID);
    }

    @Test
    void ShouldReturn404WhenRemovingNonExistentWishlistProduct() throws Exception {
        doThrow(new WishlistNotFoundException(CUSTOMER_ID))
                .when(removeProductFromWishlistUseCase).execute(CUSTOMER_ID, PRODUCT_ID);

        mockMvc.perform(delete(BASE_URL + "/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void ShouldBuscarProdutoNaWishlistSuccessfully() throws Exception {
        final Wishlist wishlist = WishlistTestDataFactory.wishlistWithProduct("wishlist-1", CUSTOMER_ID, WishlistTestDataFactory.defaultProduct());

        when(checkProductInWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID)).thenReturn(wishlist);

        mockMvc.perform(get(BASE_URL + "/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.products[0].id").value(PRODUCT_ID));
    }

    @Test
    void ShouldReturn404WhenProductDoesNotExistInWishlist() throws Exception {
        when(checkProductInWishlistUseCase.execute(CUSTOMER_ID, PRODUCT_ID))
                .thenThrow(new ProductNotFoundException(PRODUCT_ID));

        mockMvc.perform(get(BASE_URL + "/{customerId}/products/{productId}", CUSTOMER_ID, PRODUCT_ID))
                .andExpect(status().isNotFound());
    }

}
