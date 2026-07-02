package com.automationexercise.testcases.ui;

import com.automationexercise.drivers.WebDriverFactory;
import com.automationexercise.utils.PropertyReader;
import com.automationexercise.utils.LogsManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser)
    {
        LogsManager.info("Setting up the test environment for browser: {}", browser);
        String url = PropertyReader.getProperty("baseUrlWeb");
        WebDriverFactory.create(browser).get(url);
        LogsManager.info("Navigated to URL: {}", url);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown()
    {
        LogsManager.info("Tearing down the test environment");
        WebDriver driver = WebDriverFactory.getDriver();
        if (driver != null) {
            driver.quit();
        }
        WebDriverFactory.unload();
    }
}
