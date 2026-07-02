package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;

public class ContactUs extends BasePage {
    
    // Expected URL
    public static final String EXPECTED_URL = BASE_URL + "contact_us";

    // ==========================================
    // Constructor
    // ==========================================
    public ContactUs(WebDriver driver) {
        super(driver);
    }

    @Override
    public ContactUs verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }
}
