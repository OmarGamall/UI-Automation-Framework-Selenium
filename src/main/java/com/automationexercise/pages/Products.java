package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;

public class Products extends BasePage {
    
    // Expected URL
    public static final String EXPECTED_URL = BASE_URL + "products";

    // ==========================================
    // Constructor
    // ==========================================
    public Products(WebDriver driver) {
        super(driver);
    }

    @Override
    public Products verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }

    // ==========================================
    // Locators
    // ==========================================


    // ==========================================
    // Actions
    // ==========================================
}
