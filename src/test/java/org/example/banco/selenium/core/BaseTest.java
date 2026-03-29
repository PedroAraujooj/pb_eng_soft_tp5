package org.example.banco.selenium.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = configurarChrome();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private ChromeOptions configurarChrome() {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_settings.popups", 0);

        options.setExperimentalOption("prefs", prefs);

        // options.addArguments("--headless=new");
        options.addArguments("--incognito");
        options.addArguments("--window-size=1280,800");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--lang=pt-BR");


        return options;
    }

    protected void takeScreenshot(String testName) {
        if (driver == null) return;

        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            Path targetDir = Path.of("src", "test", "resources", "screenshots");
            Files.createDirectories(targetDir);

            Path target = targetDir.resolve(testName + "_" + timestamp + ".png");
            Files.copy(src.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot salvo em: " + target.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Falha ao salvar screenshot: " + e.getMessage());
        }
    }
}