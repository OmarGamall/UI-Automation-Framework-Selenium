package com.blazedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.blazedemo.utils.actions.ElementActions;

public class SignUp {
    
    // ==========================================
    // Fields & Drivers
    // ==========================================
    private final WebDriver driver;
    private final ElementActions elementActions;

    // ==========================================
    // Constructor
    // ==========================================
    public SignUp(WebDriver driver) {
        this.driver = driver;
        this.elementActions = new ElementActions(driver);
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
