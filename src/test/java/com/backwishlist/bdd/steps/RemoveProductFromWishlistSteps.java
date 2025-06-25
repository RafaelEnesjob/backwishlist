package com.backwishlist.bdd.steps;

import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.impl.RemoveProductFromWishlistUseCase;
import com.backwishlist.domain.Product;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.ProductNotFoundException;
import com.backwishlist.domain.exceptions.WishlistNotFoundException;
import io.cucumber.java.pt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RemoveProductFromWishlistSteps {

    @Autowired
    private IWishlistRepository wishlistRepository;

    @Autowired
    private RemoveProductFromWishlistUseCase useCase;

    private String customerId;
    private String productId;
    private Exception capturedException;

    @Dado("que o cliente {string} já salvou o produto {string} na wishlist")
    public void clienteComProdutoSalvo(final String customerId, final String productId) {
        this.customerId = customerId;
        this.productId = productId;

        final Product produto = Product.builder()
                .id(productId)
                .name("Tênis Nike")
                .build();

        final Wishlist wishlist = Wishlist.create(customerId, produto);
        wishlistRepository.save(wishlist);
    }

    @Dado("que o cliente {string} possui uma wishlist vazia para remoção")
    public void clienteComWishlistVaziaParaRemocao(final String customerId) {
        this.customerId = customerId;

        final Wishlist wishlist = Wishlist.builder()
                .id("wishlist-remocao")
                .customerId(customerId)
                .products(new ArrayList<>())
                .build();

        wishlistRepository.save(wishlist);
    }

    @Dado("que o cliente {string} não possui uma wishlist cadastrada")
    public void clienteSemWishlistCadastrada(final String customerId) {
        this.customerId = customerId;
    }

    @Quando("ele remover o produto {string} da sua wishlist")
    public void removerProduto(final String productId) {
        this.productId = productId;
        try {
            useCase.execute(customerId, productId);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Quando("ele tentar remover o produto {string}")
    public void removerProdutoInexistente(final String productId) {
        this.productId = productId;
        try {
            useCase.execute(customerId, productId);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Entao("o produto não deve estar mais presente na wishlist")
    public void validarProdutoRemovido() {
        final Wishlist wishlist = wishlistRepository.findByCustomerId(customerId).orElseThrow();
        assertFalse(wishlist.containsProduct(productId));
    }

    @Entao("deve ocorrer um erro informando que o produto não foi encontrado")
    public void validarErroProdutoNaoEncontrado() {
        assertNotNull(capturedException);
        assertInstanceOf(ProductNotFoundException.class, capturedException);
    }

    @Entao("deve ocorrer um erro informando que a wishlist não existe")
    public void validarErroWishlistNaoEncontrada() {
        assertNotNull(capturedException);
        assertInstanceOf(WishlistNotFoundException.class, capturedException);
    }
}