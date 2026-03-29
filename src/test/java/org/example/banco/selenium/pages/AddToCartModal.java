package org.example.banco.selenium.pages;

import org.example.banco.selenium.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddToCartModal extends BasePage {

    private final By modalRoot = By.xpath("//div[contains(@class,'modal') and .//*[contains(.,'added to cart')]]");
    private final By continueShoppingBtn = By.xpath(
            "//div[contains(@class,'modal') and .//*[contains(.,'added to cart')]]//button[normalize-space()='Continue Shopping']"
    );
    private final By viewCartLink = By.xpath(
            "//div[contains(@class,'modal') and .//*[contains(.,'added to cart')]]//a[contains(@href,'view_cart') and normalize-space()='View Cart']"
    );

    public AddToCartModal(WebDriver driver) {
        super(driver);
        $(modalRoot);
    }

    public ProductsPage continueShopping() {
        click(continueShoppingBtn);
        return new ProductsPage(driver);
    }

    public CartPage goToCartFromModal() {
        click(viewCartLink);
        wait.until(d -> d.getCurrentUrl().contains("view_cart"));
        return new CartPage(driver);
    }
}
