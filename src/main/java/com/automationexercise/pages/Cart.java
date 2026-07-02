package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;

public class Cart extends BasePage {
    
    // Expected URL
    public static final String EXPECTED_URL = BASE_URL + "view_cart";

    // ==========================================
    // Constructor
    // ==========================================
    public Cart(WebDriver driver) {
        super(driver);
    }

    @Override
    public Cart verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }
}
