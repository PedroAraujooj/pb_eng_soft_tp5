package org.example.banco.selenium.pages;

import org.example.banco.selenium.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductDetailsPage extends BasePage {

    private final By quantityInput = By.cssSelector("#quantity");
    private final By addToCartBtn = By.cssSelector("button.cart");
    private final By productName = By.cssSelector(".product-information h2");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);

        wait.until(ExpectedConditions.urlContains("/product_details/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productName));
        wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInput));
    }

    public ProductDetailsPage setQuantity(int qty) {
        var el = $(quantityInput);
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.DELETE);
        el.sendKeys(String.valueOf(qty));
        return this;
    }

    public AddToCartModal addToCart() {
        click(addToCartBtn);
        return new AddToCartModal(driver);
    }
}