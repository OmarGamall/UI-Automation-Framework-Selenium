package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;
import com.automationexercise.pages.components.NavigationBar;

public class HomePage extends BasePage {
    
    // Expected URL
    public static final String EXPECTED_URL = BASE_URL;

    // ==========================================
    // Fields & Drivers
    // ==========================================
    private final NavigationBar navigationBar;

    // ==========================================
    // Constructor
    // ==========================================
    public HomePage(WebDriver driver) {
        super(driver);
        this.navigationBar = new NavigationBar(driver);
    }

    // ==========================================
    // Page Components
    // ==========================================
    public NavigationBar getNavigationBar() {
        return this.navigationBar;
    }

    @Override
    public HomePage verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }
}

