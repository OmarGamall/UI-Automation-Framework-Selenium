package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    
    // Expected URL
    public static final String EXPECTED_URL = BASE_URL;

    // ==========================================
    // Constructor
    // ==========================================
    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public HomePage verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }
}

