package com.backwishlist.bdd.steps;

import com.backwishlist.Util.WishlistTestDataFactory;
import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.impl.GetAllProductsFromWishlistUseCase;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import io.cucumber.java.pt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GetAllProductsFromWishlistSteps {

    @Autowired
    private IWishlistRepository wishlistRepository;

    @Autowired
    private GetAllProductsFromWishlistUseCase useCase;

    private String customerId;
    private Wishlist response;
    private Exception capturedException;

    @Dado("que o cliente {string} já tem produtos na wishlist")
    public void clienteComProdutos(final String customerId) {
        this.customerId = customerId;
        final Wishlist wishlist = WishlistTestDataFactory.wishlistWithProduct(
                "wishlist-1", customerId, WishlistTestDataFactory.defaultProduct()
        );
        wishlistRepository.save(wishlist);
    }

    @Dado("que o cliente {string} não possui wishlist cadastrada")
    public void clienteSemWishlist(final String customerId) {
        this.customerId = customerId;
    }

    @Dado("que o cliente {string} possui uma wishlist completamente vazia")
    public void clienteComWishlistVazia(final String customerId) {
        this.customerId = customerId;
        final Wishlist wishlist = Wishlist.builder()
                .id("wishlist-vazia")
                .customerId(customerId)
                .products(new ArrayList<>())
                .build();
        wishlistRepository.save(wishlist);
    }

    @Quando("eu buscar todos os produtos da wishlist do cliente {string}")
    public void buscarWishlistPorId(final String customerId) {
        try {
            response = useCase.execute(customerId);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Quando("eu buscar todos os produtos da wishlist")
    public void buscarWishlistAtual() {
        try {
            response = useCase.execute(customerId);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Entao("os produtos da wishlist do cliente {string} devem ser retornados")
    public void validarProdutos(final String customerId) {
        assertNotNull(response);
        assertEquals(customerId, response.getCustomerId());
        assertFalse(response.getProducts().isEmpty());
    }

    @Entao("deve ocorrer um erro de wishlist não encontrada")
    public void deve_ocorrer_um_erro_de_wishlist_nao_encontrada() {
        assertNotNull(capturedException);
        assertInstanceOf(WishlistNotFoundException.class, capturedException);
    }

    @Entao("a lista de produtos deve estar vazia")
    public void validarListaVazia() {
        assertNotNull(response);
        assertEquals(customerId, response.getCustomerId());
        assertNotNull(response.getProducts());
        assertTrue(response.getProducts().isEmpty());
    }
}