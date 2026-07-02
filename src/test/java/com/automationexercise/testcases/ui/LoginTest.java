package com.automationexercise.testcases.ui;

import com.automationexercise.drivers.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.SignUpLogin;
import com.automationexercise.utils.PropertyReader;

public class LoginTest extends BaseTest {

    private final String validUserName = PropertyReader.getProperty("login.valid.username");
    private final String validUserEmail = PropertyReader.getProperty("login.valid.email");
    private final String validPassword = PropertyReader.getProperty("login.valid.password");
    private final String invalidPassword = PropertyReader.getProperty("login.invalid.password");
    private final String nonexistentEmail = PropertyReader.getProperty("login.nonexistent.email");

    @Test(description = "Verify successful login with valid credentials")
    public void verifySuccessfulLogin()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .clickSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterLoginEmail(validUserEmail)
                .enterLoginPassword(validPassword)
                .clickLogInSuccess()
                .verifyUserLabel(validUserName);
    }

    @Test(description = "Verify that logging in with an incorrect password displays an error message")
    public void verifyLoginWithIncorrectPassword()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .getNavigationBar()
                .clickSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterLoginEmail(validUserEmail)
                .enterLoginPassword(invalidPassword)
                .clickLogInFailure()
                .verifyLoginErrorMessage(SignUpLogin.ERROR_INVALID_CREDENTIALS);
    }

    @Test(description = "Verify that logging in with a non-existent email displays an error message")
    public void verifyLoginWithNonExistentUser() {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .getNavigationBar()
                .clickSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterLoginEmail(nonexistentEmail)
                .enterLoginPassword(validPassword)
                .clickLogInFailure()
                .verifyLoginErrorMessage(SignUpLogin.ERROR_INVALID_CREDENTIALS);
    }
}
