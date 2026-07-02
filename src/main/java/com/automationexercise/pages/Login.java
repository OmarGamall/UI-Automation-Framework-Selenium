package com.automationexercise.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.automationexercise.utils.actions.ElementActions;
import io.qameta.allure.Step;

public class Login {
    
    // ==========================================
    // Fields & Drivers
    // ==========================================
    private final WebDriver driver;
    private final ElementActions elementActions;

    // ==========================================
    // Constructor
    // ==========================================
    public Login(WebDriver driver) {
        this.driver = driver;
        this.elementActions = new ElementActions(driver);
    }

    // ==========================================
    // Locators
    // ==========================================
    private final By emailInput = By.cssSelector("input[data-qa='login-email']");
    private final By passwordInput = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");
    private final By loginErrorMessage = By.cssSelector("form[action='/login'] p");

    // ==========================================
    // Page Actions
    // ==========================================
    @Step("Enter email: {email}")
    public Login enterEmail(String email) {
        elementActions.sendKeys(emailInput, email);
        return this;
    }

    @Step("Enter password")
    public Login enterPassword(String password) {
        elementActions.sendKeys(passwordInput, password);
        return this;
    }

    @Step("Click Login button")
    public HomePage clickLogInButton() {
        elementActions.click(loginButton);
        return new HomePage(driver);
    }

    @Step("Get login error message text")
    public String getErrorMessage() {
        return elementActions.getText(loginErrorMessage);
    }

    @Step("Check if login error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return elementActions.isDisplayed(loginErrorMessage);
    }
}

