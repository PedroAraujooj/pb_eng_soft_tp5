package org.example.banco.selenium.pages;

import org.example.banco.selenium.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AccountCreatedPage extends BasePage {

    private final By accountCreatedMessage = By.xpath("//b[normalize-space()='Account Created!']");
    private final By continueButton = By.cssSelector("[data-qa='continue-button']");

    public AccountCreatedPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountCreatedMessage));
    }

    public boolean isAccountCreatedVisible() {
        return $(accountCreatedMessage).isDisplayed();
    }

    public HomePage clickContinue() {
        click(continueButton);
        return new HomePage(driver);
    }
}
