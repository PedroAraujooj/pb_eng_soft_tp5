package org.example.banco.selenium.pages;

import org.example.banco.selenium.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class AccountInformationPage extends BasePage {

    private final By accountInfoTitle = By.xpath("//b[normalize-space()='Enter Account Information']");

    private final By mrTitleRadio = By.id("id_gender1");
    private final By passwordField = By.id("password");
    private final By daysSelect = By.id("days");
    private final By monthsSelect = By.id("months");
    private final By yearsSelect = By.id("years");
    private final By firstName = By.id("first_name");
    private final By lastName = By.id("last_name");
    private final By address1 = By.id("address1");
    private final By countrySelect = By.id("country");
    private final By state = By.id("state");
    private final By city = By.id("city");
    private final By zipcode = By.id("zipcode");
    private final By mobileNumber = By.id("mobile_number");
    private final By createAccountButton = By.xpath("//button[normalize-space()='Create Account']");

    public AccountInformationPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountInfoTitle));
    }

    public AccountCreatedPage fillFormAndSubmit(String password) {
        click(mrTitleRadio);
        type(passwordField, password);

        new Select($(daysSelect)).selectByValue("6");
        new Select($(monthsSelect)).selectByVisibleText("December");
        new Select($(yearsSelect)).selectByValue("1997");

        type(firstName, "QA");
        type(lastName, "Automation");
        type(address1, "Rua Selenium 123");
        new Select($(countrySelect)).selectByVisibleText("Australia");
        type(state, "SP");
        type(city, "Sao Paulo");
        type(zipcode, "012345");
        type(mobileNumber, "11999999999");

        click(createAccountButton);
        return new AccountCreatedPage(driver);
    }
}
