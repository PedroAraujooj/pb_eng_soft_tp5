package org.example.banco.selenium;



import org.example.banco.selenium.core.BaseTest;
import org.example.banco.selenium.pages.CartPage;
import org.example.banco.selenium.pages.HomePage;
import org.example.banco.selenium.pages.ProductDetailsPage;
import org.example.banco.selenium.pages.ProductsPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CartTests extends BaseTest {

    @Test
    void deveAdicionarProdutoNoCarrinho() {
        try {
            HomePage home = new HomePage(driver);
            home.open();
            assertTrue(home.isHomeVisible(), "Home deveria estar visível.");

            CartPage cart = home.goToProducts()
                    .addProductToCartById(1)
                    .goToCartFromModal()
                    .awaitAtLeastItems(1);

            assertFalse(cart.isEmpty(), "Carrinho deveria ter itens após adicionar.");
            assertTrue(cart.itemCount() >= 1, "Carrinho deveria ter pelo menos 1 item.");
        } catch (Exception e) {
            takeScreenshot("deveAdicionarProdutoNoCarrinho");
            throw e;
        }
    }

    @Test
    void deveAdicionarDoisProdutosNoCarrinho() {
        try {
            HomePage home = new HomePage(driver);
            home.open();

            ProductsPage products = home.goToProducts();

            products.addProductToCartById(1)
                    .continueShopping()
                    .addProductToCartById(2)
                    .goToCartFromModal();

            CartPage cart = new CartPage(driver);

            assertTrue(cart.itemCount() >= 2, "Carrinho deveria ter 2 itens após adicionar dois produtos.");
            assertTrue(cart.containsProductName("Blue Top"), "Carrinho deveria conter 'Blue Top'.");
            assertTrue(cart.containsProductName("Men Tshirt"), "Carrinho deveria conter 'Men Tshirt'.");
        } catch (Exception e) {
            takeScreenshot("deveAdicionarDoisProdutosNoCarrinho");
            throw e;
        }
    }

    @Test
    void deveAdicionarProdutoComQuantidadeEValidarNoCarrinho_updateQuantity() {
        try {
            HomePage home = new HomePage(driver);
            home.open();

            ProductDetailsPage details = home.goToProducts()
                    .openFirstProductDetails();

            CartPage cart = details
                    .setQuantity(4)
                    .addToCart()
                    .goToCartFromModal();

            assertEquals(4, cart.readFirstItemQuantity(), "Quantidade no carrinho deveria ser 4.");
        } catch (Exception e) {
            System.out.println("URL no momento da falha: " + driver.getCurrentUrl());
            takeScreenshot("deveAdicionarProdutoComQuantidadeEValidarNoCarrinho_updateQuantity");
            throw e;
        }
    }

    @Test
    void deveRemoverProdutoDoCarrinho_delete() {
        try {
            HomePage home = new HomePage(driver);
            home.open();

            CartPage cart = home.goToProducts()
                    .addProductToCartById(1)
                    .goToCartFromModal()
                    .awaitAtLeastItems(1);

            assertFalse(cart.isEmpty(), "Pré-condição: carrinho deveria ter item antes de remover.");

            cart.removeFirstItem();
            assertTrue(cart.isEmpty(), "Carrinho deveria ficar vazio após remover item.");
        } catch (Exception e) {
            takeScreenshot("deveRemoverProdutoDoCarrinho_delete");
            throw e;
        }
    }
}