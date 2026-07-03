package com.automationexercise.pages;

import com.automationexercise.pages.components.NavigationBar;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class SignUpLogin extends BasePage {
    
    // Expected Constants
    public static final String EXPECTED_URL = BASE_URL + "login";
    public static final String ERROR_INVALID_CREDENTIALS = "Your email or password is incorrect!";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "Email Address already exist!";
    // ==========================================
    // Constructor
    // ==========================================
    public SignUpLogin(WebDriver driver) {
        super(driver);
    }

    // ==========================================
    // Locators
    // ==========================================
    // Login Section
    private final By loginEmailInput = By.cssSelector("[data-qa='login-email']");
    private final By loginPasswordInput = By.cssSelector("[data-qa='login-password']");
    private final By loginButton = By.cssSelector("[data-qa='login-button']");
    private final By loginErrorMessage = By.cssSelector(".login-form p");
    // Sign Up Section
    private final By signUpName = By.cssSelector("[data-qa='signup-name']");
    private final By signUpEmail = By.cssSelector("[data-qa='signup-email']");
    private final By signUpButton = By.cssSelector("[data-qa='signup-button']");
    private final By signUpErrorMessage = By.cssSelector(".signup-form p");


    // ==========================================
    // Page Actions
    // ==========================================
    @Step("Enter login email: {email}")
    public SignUpLogin enterLoginEmail(String email) {
        elementActions.sendKeys(loginEmailInput, email);
        return this;
    }

    @Step("Enter login password: {password}")
    public SignUpLogin enterLoginPassword(String password) {
        elementActions.sendKeys(loginPasswordInput, password);
        return this;
    }

    @Step("Click Log In button expecting success")
    public HomePage clickLogInSuccess() {
        elementActions.click(loginButton);
        return new HomePage(driver);
    }

    @Step("Click Log In button expecting failure")
    public SignUpLogin clickLogInFailure() {
        elementActions.click(loginButton);
        return this;
    }

    public String getLoginErrorMessage() {
        return elementActions.getText(loginErrorMessage);
    }

    public String getSignUpErrorMessage() {
        return elementActions.getText(signUpErrorMessage);
    }

    @Step("Enter sign up name: {name}")
    public SignUpLogin enterSignUpName(String name) {
        elementActions.sendKeys(signUpName, name);
        return this;
    }

    @Step("Enter sign up email: {email}")
    public SignUpLogin enterSignUpEmail(String email) {
        elementActions.sendKeys(signUpEmail, email);
        return this;
    }

    @Step("Click Sign Up button expecting success")
    public SignUp clickSignUpSuccess() {
        elementActions.click(signUpButton);
        return new SignUp(driver);
    }

    @Step("Click Sign Up button expecting failure")
    public SignUpLogin clickSignUpFailure() {
        elementActions.click(signUpButton);
        return this;
    }


    // ==========================================
    // Verification
    // ==========================================
    @Step("Verify login error message: {expectedMessage}")
    public SignUpLogin verifyLoginErrorMessage(String expectedMessage) {
        String actualMessage = getLoginErrorMessage();
        Assert.assertEquals(actualMessage, expectedMessage, "Login error message does not match expected.");
        return this;
    }

    @Step("Verify sign up error message: {expectedMessage}")
    public SignUpLogin verifySignUpErrorMessage(String expectedMessage) {
        String actualMessage = getSignUpErrorMessage();
        Assert.assertEquals(actualMessage, expectedMessage, "Sign up error message does not match expected.");
        return this;
    }

    @Override
    public SignUpLogin verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }
}
