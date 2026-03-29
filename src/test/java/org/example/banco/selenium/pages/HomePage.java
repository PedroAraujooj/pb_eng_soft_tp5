package org.example.banco.selenium.pages;

import org.example.banco.selenium.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By logo = By.cssSelector("a > img");
    private final By signupLoginLink = By.xpath("//a[normalize-space()='Signup / Login']");
    private final By loggedInAs = By.xpath("//*[contains(text(),'Logged in as')]");
    private final By productsLink    = By.xpath("//a[contains(@href,'/products')]");

    private final By logoutLink = By.xpath("//a[normalize-space()='Logout']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://automationexercise.com/");
        waitForPageReady();
        removeKnownAdIframes();
    }
    public ProductsPage goToProducts() {
        click(productsLink);
        return new ProductsPage(driver);
    }

    public boolean isHomeVisible() {
        return $(logo).isDisplayed();
    }

    public SignupLoginPage clickSignupLogin() {
        click(signupLoginLink);
        return new SignupLoginPage(driver);
    }

    public boolean isLoggedIn() {
        return !driver.findElements(loggedInAs).isEmpty();
    }

    public void logout() {
        click(logoutLink);
    }
}
