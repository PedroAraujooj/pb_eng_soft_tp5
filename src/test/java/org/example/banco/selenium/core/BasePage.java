package org.example.banco.selenium.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public WebElement $(By locator) {
        waitForPageReady();
        removeKnownAdIframes();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public List<WebElement> $$(By locator) {
        waitForPageReady();
        removeKnownAdIframes();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        waitForPageReady();
        removeKnownAdIframes();

        String currentWindow = driver.getWindowHandle();
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollIntoView(el);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (ElementClickInterceptedException | TimeoutException | StaleElementReferenceException e) {
            removeKnownAdIframes();
            el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            scrollIntoView(el);
            jsClick(el);
        } catch (WebDriverException e) {
            jsClick(el);
        } finally {
            closeExtraTabs(currentWindow);
        }
    }

    protected void type(By locator, String text) {
        waitForPageReady();
        removeKnownAdIframes();

        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        scrollIntoView(el);

        try {
            el.clear();
            el.sendKeys(text);
        } catch (WebDriverException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", el);
            el.sendKeys(text);
        }
    }

    protected void waitForPageReady() {
        wait.until(d ->
                "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    protected void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", el);
    }

    protected void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    protected void removeKnownAdIframes() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll(" +
                            "'iframe[src*=\"doubleclick\"]," +
                            "iframe[src*=\"googlesyndication\"]," +
                            "iframe[src*=\"googleads\"]," +
                            "iframe[src*=\"adservice\"]," +
                            "iframe[id*=\"google_ads_iframe\"]," +
                            "iframe[id*=\"aswift\"]," +
                            "ins.adsbygoogle'" +
                            ").forEach(el => el.remove());"
            );
        } catch (Exception ignored) {
        }
    }

    protected void closeExtraTabs(String currentWindow) {
        Set<String> handles = driver.getWindowHandles();

        for (String handle : handles) {
            if (!handle.equals(currentWindow)) {
                driver.switchTo().window(handle);
                driver.close();
            }
        }

        driver.switchTo().window(currentWindow);
    }
}