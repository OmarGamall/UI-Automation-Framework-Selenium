package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;

public class DeleteAccount extends BasePage {
    
    // Expected URL
    public static final String EXPECTED_URL = BASE_URL + "delete_account";

    // ==========================================
    // Constructor
    // ==========================================
    public DeleteAccount(WebDriver driver) {
        super(driver);
    }

    @Override
    public DeleteAccount verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }
}
