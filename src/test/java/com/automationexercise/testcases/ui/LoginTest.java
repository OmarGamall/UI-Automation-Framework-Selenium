package com.automationexercise.testcases.ui;

import com.automationexercise.drivers.WebDriverFactory;
import com.automationexercise.models.User;
import com.automationexercise.pages.HomePage;
import com.automationexercise.facades.LoginFacade;
import com.automationexercise.pages.SignUpLogin;
import com.automationexercise.utils.JsonReader;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private final User validUser = new JsonReader("valid-user").as(User.class);
    private final User invalidPasswordUser = new JsonReader("invalid-password-user").as(User.class);
    private final User nonExistentUser = new JsonReader("non-existent-user").as(User.class);

    @Test(groups = {TestGroups.SMOKE, TestGroups.REGRESSION, TestGroups.POSITIVE}, description = "Verify successful login with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    public void verifySuccessfulLogin()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterLoginEmail(validUser.getEmail())
                .enterLoginPassword(validUser.getPassword())
                .clickLogInSuccess()
                .getNavigationBar()
                .verifyUserLabel(validUser.getUsername());
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that logging in with an incorrect password displays an error message")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyLoginWithIncorrectPassword()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterLoginEmail(validUser.getEmail())
                .enterLoginPassword(invalidPasswordUser.getPassword())
                .clickLogInFailure()
                .verifyLoginErrorMessage(SignUpLogin.ERROR_INVALID_CREDENTIALS);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that logging in with a non-existent email displays an error message")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyLoginWithNonExistentUser() {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterLoginEmail(nonExistentUser.getEmail())
                .enterLoginPassword(validUser.getPassword())
                .clickLogInFailure()
                .verifyLoginErrorMessage(SignUpLogin.ERROR_INVALID_CREDENTIALS);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.POSITIVE}, description = "Verify that a logged in user can log out successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void verifySuccessfulLogout() {
        WebDriver driver = WebDriverFactory.getDriver();
        new LoginFacade(driver)
                .login(validUser)
                .getNavigationBar()
                .verifyUserLabel(validUser.getUsername())
                .navigateToLogout()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .getNavigationBar()
                .verifyUserLabelNotDisplayed()
                .verifyLogoutButtonNotDisplayed();
    }
}
