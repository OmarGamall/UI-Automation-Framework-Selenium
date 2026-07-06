package com.automationexercise.pages;

import com.automationexercise.pages.components.NavigationBar;
import com.automationexercise.utils.actions.AlertActions;
import com.automationexercise.utils.actions.ElementActions;
import com.automationexercise.utils.PropertyReader;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import com.automationexercise.utils.WaitManager;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final ElementActions elementActions;
    protected final NavigationBar navigationBar;
    protected final AlertActions alertActions;
    
    // Load Base URL dynamically from properties
    public static final String BASE_URL = PropertyReader.getProperty("baseUrlWeb");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.elementActions = new ElementActions(driver);
        this.navigationBar = new NavigationBar(driver);
        this.alertActions = new AlertActions(driver);
    }

    public NavigationBar getNavigationBar() {
        return this.navigationBar;
    }

    @Step("Verify that the page URL matches the expected URL: {expectedUrl}")
    public BasePage verifyPageUrl(String expectedUrl) {
        elementActions.waitForUrlToBe(expectedUrl);
        String actualUrl = elementActions.getCurrentUrl();
        // Normalize trailing slashes to avoid discrepancies
        String normalizedActual = actualUrl.replaceAll("/$", "");
        String normalizedExpected = expectedUrl.replaceAll("/$", "");
        Assert.assertEquals(normalizedActual, normalizedExpected, "Page URL does not match!");
        return this;
    }
}
