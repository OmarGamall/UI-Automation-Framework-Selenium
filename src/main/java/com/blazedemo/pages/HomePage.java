package com.blazedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.blazedemo.utils.actions.ElementActions;

public class HomePage {
    
    // ==========================================
    // Fields & Drivers
    // ==========================================
    private final WebDriver driver;
    private final ElementActions elementActions;

    // ==========================================
    // Constructor
    // ==========================================
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.elementActions = new ElementActions(driver);
    }

    // ==========================================
    // Locators
    // ==========================================
    private final By welcomeMessage = By.id("nameofuser");
    private final By loginButton = By.id("login2");
    private final By signUpButton = By.id("signin2");
    private final By logoutButton = By.id("logout2");

    // ==========================================
    // Page Actions
    // ==========================================
    public String getWelcomeMessage() {
        return elementActions.getText(welcomeMessage);
    }

    public boolean isLoginButtonDisplayed() {
        return elementActions.isDisplayed(loginButton);
    }

    public boolean isSignUpButtonDisplayed() {
        return elementActions.isDisplayed(signUpButton);
    }

    public Login clickLoginTab() {
        elementActions.click(loginButton);
        return new Login(driver);
    }

    public SignUp clickSignUpTab() {
        elementActions.click(signUpButton);
        return new SignUp(driver);
    }

    public HomePage clickLogout() {
        elementActions.click(logoutButton);
        return this;
    }
}

