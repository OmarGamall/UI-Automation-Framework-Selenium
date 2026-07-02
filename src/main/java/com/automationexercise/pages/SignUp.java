package com.automationexercise.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignUp extends BasePage {
    
    // Expected URL
    public static final String EXPECTED_URL = BASE_URL + "signup";

    // ==========================================
    // Constructor
    // ==========================================
    public SignUp(WebDriver driver) {
        super(driver);
    }

    @Override
    public SignUp verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }

    // ==========================================
    // Locators
    // ==========================================
    private final By usernameInput = By.id("sign-username");
    private final By passwordInput = By.id("sign-password");
    private final By signUpButton = By.xpath("//button[normalize-space()='Sign up']");
    private final By closeButton = By.xpath("//div[@id='signInModal'] //button[normalize-space()='Close']");

    // ==========================================
    // Page Actions
    // ==========================================
    public SignUp enterUsername(String username) {
        elementActions.sendKeys(usernameInput, username);
        return this;
    }

    public SignUp enterPassword(String password) {
        elementActions.sendKeys(passwordInput, password);
        return this;
    }

    public HomePage clickSignUpButton() {
        elementActions.click(signUpButton);
        return new HomePage(driver);
    }

    public HomePage clickCloseButton() {
        elementActions.click(closeButton);
        return new HomePage(driver);
    }
}
