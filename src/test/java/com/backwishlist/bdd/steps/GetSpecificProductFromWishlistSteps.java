package com.backwishlist.bdd.steps;

import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.impl.CheckProductInWishlistUseCase;
import com.backwishlist.domain.Product;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import io.cucumber.java.pt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GetSpecificProductFromWishlistSteps {

    @Autowired
    private IWishlistRepository wishlistRepository;

    @Autowired
    private CheckProductInWishlistUseCase useCase;

    private String customerId;
    private String productId;
    private Wishlist response;
    private Exception capturedException;

    @Dado("que o cliente {string} possui o produto {string} na wishlist")
    public void clientePossuiProduto(final String customerId, final String productId) {
        this.customerId = customerId;
        this.productId = productId;

        final Product produto = Product.builder().id(productId).name("Tênis Nike").build();
        final Wishlist wishlist = Wishlist.create(customerId, produto);
        wishlistRepository.save(wishlist);
    }

    @Dado("que o cliente {string} possui uma wishlist sem produtos")
    public void clienteComWishlistVazia(final String customerId) {
        this.customerId = customerId;

        final Wishlist wishlist = Wishlist.builder()
                .id("wishlist-vazia")
                .customerId(customerId)
                .products(new ArrayList<>())
                .build();

        wishlistRepository.save(wishlist);
    }

    @Quando("eu buscar o produto {string} na wishlist")
    public void buscarProduto(final String productId) {
        this.productId = productId;
        try {
            response = useCase.execute(customerId, productId);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Entao("o produto retornado deve ter o nome {string}")
    public void validarProdutoRetornado(final String nomeEsperado) {
        assertNotNull(response);
        assertTrue(response.containsProduct(productId));

        final Product produto = response.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow();

        assertEquals(nomeEsperado, produto.getName());
    }

    @Entao("deve ocorrer um erro de produto não encontrado")
    public void erroProdutoInexistente() {
        assertNotNull(capturedException);
        assertInstanceOf(ProductNotFoundException.class, capturedException);
    }
}