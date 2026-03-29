package org.example.banco.selenium.pages;

import org.example.banco.selenium.core.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage {
    private final By deleteButtons = By.cssSelector("a.cart_quantity_delete");

    private final By cartRows = By.cssSelector("#cart_info_table tbody tr");
    private final By emptyText = By.xpath("//*[contains(normalize-space(),'Cart is empty!')]");
    private final By deleteFirstBtn = By.cssSelector("a.cart_quantity_delete");

    public CartPage(WebDriver driver) {
        super(driver);
    }




    public int readFirstItemQuantity() {
        if (itemCount() == 0) throw new IllegalStateException("Carrinho vazio; não há quantidade para ler.");
        By qtyBtn = By.cssSelector("#cart_info_table tbody tr:first-child td.cart_quantity button");
        String txt = $(qtyBtn).getText().trim();
        return Integer.parseInt(txt);
    }

    public boolean containsProductName(String expectedName) {
        By names = By.cssSelector("#cart_info_table td.cart_description h4 a");
        return $$(names).stream().anyMatch(e -> e.getText().trim().equalsIgnoreCase(expectedName.trim()));
    }
    public CartPage awaitAtLeastItems(int min) {
        wait.until(d -> d.findElements(By.cssSelector("#cart_info_table tbody tr")).size() >= min);
        return this;
    }

    public int itemCount() {
        return driver.findElements(deleteButtons).size();
    }

    public boolean isEmpty() {
        if (itemCount() > 0) return false;
        return isEmptyMessageVisible();
    }

    private boolean isEmptyMessageVisible() {
        List<WebElement> els = driver.findElements(emptyText);
        return els.stream().anyMatch(WebElement::isDisplayed);
    }

    public void removeFirstItem() {
        int before = itemCount();
        if (before == 0) throw new IllegalStateException("Carrinho já está vazio; não há item para remover.");

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(deleteButtons));
        WebElement row = btn.findElement(By.xpath("./ancestor::tr"));

        safeClick(btn);

        wait.until(d -> {
            int now = d.findElements(deleteButtons).size();
            if (now < before) return true;
            try {
                row.isDisplayed();
                return false;
            } catch (StaleElementReferenceException ignored) {
                return true;
            }
        });

        if (before == 1) {
            wait.until(d -> d.findElements(deleteButtons).isEmpty());
            wait.until(d -> isEmptyMessageVisible());
        }
    }

    private void safeClick(WebElement el) {
        try {
            el.click();
        } catch (WebDriverException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }
}
