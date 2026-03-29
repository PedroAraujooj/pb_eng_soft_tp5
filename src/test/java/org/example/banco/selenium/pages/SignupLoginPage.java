package org.example.banco.selenium.pages;

import org.example.banco.selenium.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SignupLoginPage extends BasePage {

    private final By loginTitle = By.xpath("//h2[normalize-space()='Login to your account']");
    private final By signupTitle = By.xpath("//h2[normalize-space()='New User Signup!']");

    private final By loginEmail = By.xpath("//input[@data-qa='login-email']");
    private final By loginPassword = By.xpath("//input[@placeholder='Password']");
    private final By loginButton = By.xpath("//button[normalize-space()='Login']");
    private final By loginError = By.xpath("//p[contains(text(),'Your email or password is incorrect!')]");

    private final By signupName = By.xpath("//input[@placeholder='Name']");
    private final By signupEmail = By.xpath("//input[@data-qa='signup-email']");
    private final By signupButton = By.xpath("//button[normalize-space()='Signup']");

    public SignupLoginPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginTitle));
    }

    public boolean isSignupFormVisible() {
        return $(signupTitle).isDisplayed();
    }

    public AccountInformationPage startSignup(String name, String email) {
        type(signupName, name);
        type(signupEmail, email);
        click(signupButton);
        return new AccountInformationPage(driver);
    }

    public void login(String email, String password) {
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginButton);
    }

    public String getLoginErrorMessage() {
        return $(loginError).getText();
    }
}
