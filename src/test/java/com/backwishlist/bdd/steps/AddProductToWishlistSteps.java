package com.backwishlist.bdd.steps;

import com.backwishlist.Util.WishlistTestDataFactory;
import com.backwishlist.app.repositories.IWishlistRepository;
import com.backwishlist.app.usecases.impl.AddProductToWishlistUseCase;
import com.backwishlist.domain.Product;
import com.backwishlist.domain.Wishlist;
import com.backwishlist.domain.exceptions.ProductAlreadyExistsException;
import com.backwishlist.domain.exceptions.WishlistLimitExceededException;
import io.cucumber.java.pt.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AddProductToWishlistSteps {

    @Autowired
    private IWishlistRepository wishlistRepository;

    @Autowired
    private AddProductToWishlistUseCase useCase;

    private String customerId;
    private Product product;
    private Exception capturedException;

    @Dado("que o cliente {string} tem uma wishlist vazia")
    public void clienteTemWishlistVazia(final String customerId) {
        this.customerId = customerId;

        final Wishlist wishlist = Wishlist.builder()
                .id("wishlist-1")
                .customerId(customerId)
                .products(new java.util.ArrayList<>())
                .build();

        wishlistRepository.save(wishlist);
    }

    @Dado("que o cliente {string} já tem o produto {string} na wishlist")
    public void clienteJaTemProduto(final String customerId, String productId) {
        this.customerId = customerId;
        this.product = Product.builder().id(productId).name("Tênis Nike").build();
        final Wishlist wishlist = Wishlist.create(customerId, product);
        wishlistRepository.save(wishlist);
    }

    @Dado("que o cliente {string} já tem 20 produtos na wishlist")
    public void clienteCom20Produtos(final String customerId) {
        this.customerId = customerId;
        final Wishlist wishlist = WishlistTestDataFactory.fullWishlist("wishlist-1", customerId);
        wishlistRepository.save(wishlist);
    }

    @Quando("eu adicionar o produto {string} com nome {string}")
    public void adicionarProduto(final String productId, final String productName) {
        product = Product.builder().id(productId).name(productName).build();
        try {
            useCase.execute(customerId, product);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Quando("eu tentar adicionar o mesmo produto novamente")
    public void adicionarProdutoDuplicado() {
        try {
            useCase.execute(customerId, product);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Quando("eu tentar adicionar o produto {string} com nome {string}")
    public void adicionarProdutoComLimite(final String productId, final String productName) {
        product = Product.builder().id(productId).name(productName).build();
        try {
            useCase.execute(customerId, product);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Entao("o produto {string} deve estar na wishlist do cliente {string}")
    public void produtoEstaNaWishlist(final String productId, final String customerId) {
        final Wishlist wishlist = wishlistRepository.findByCustomerId(customerId).orElseThrow();
        assertTrue(wishlist.containsProduct(productId));
    }

    @Entao("deve ocorrer um erro de produto já existente")
    public void validarProdutoDuplicado() {
        assertNotNull(capturedException);
        assertTrue(capturedException instanceof ProductAlreadyExistsException);
    }

    @Entao("deve ocorrer um erro de limite de produtos excedido")
    public void validarLimiteExcedido() {
        assertNotNull(capturedException);
        assertTrue(capturedException instanceof WishlistLimitExceededException);
    }
}
