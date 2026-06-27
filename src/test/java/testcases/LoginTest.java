package testcases;

import drivers.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.Login;
import utils.PropertyReader;
import utils.actions.AlertActions;

public class LoginTest extends BaseTest {

    private final String validUsername = PropertyReader.getProperty("login.valid.username");
    private final String validPassword = PropertyReader.getProperty("login.valid.password");
    private final String invalidPassword = PropertyReader.getProperty("login.invalid.password");
    private final String nonexistentUsername = PropertyReader.getProperty("login.nonexistent.username");

    @Test(description = "Verify successful login with valid credentials")
    public void verifySuccessfulLogin()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        String welcomeMessage = new HomePage(driver)
                .clickLoginTab()
                .enterUsername(validUsername)
                .enterPassword(validPassword)
                .clickLogInButton()
                .getWelcomeMessage();
        Assert.assertEquals(welcomeMessage, "Welcome " + validUsername);
    }

    @Test(description = "Verify that logging in with an incorrect password displays a 'Wrong password.' alert")
    public void verifyLoginWithIncorrectPassword()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .clickLoginTab()
                .enterUsername(validUsername)
                .enterPassword(invalidPassword)
                .clickLogInButton();

        AlertActions alertActions = new AlertActions(driver);
        String alertText = alertActions.getAlertText();
        Assert.assertEquals(alertText, "Wrong password.");
        alertActions.acceptAlert();
    }

    @Test(description = "Verify that logging in with a non-existent username displays a 'User does not exist.' alert")
    public void verifyLoginWithNonExistentUser() {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .clickLoginTab()
                .enterUsername(nonexistentUsername)
                .enterPassword(invalidPassword)
                .clickLogInButton();

        AlertActions alertActions = new AlertActions(driver);
        String alertText = alertActions.getAlertText();
        Assert.assertEquals(alertText, "User does not exist.");
        alertActions.acceptAlert();
    }

    @Test(description = "Verify that attempting to log in with empty credentials displays a warning alert")
    public void verifyLoginWithEmptyCredentials() {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .clickLoginTab()
                .enterUsername("")
                .enterPassword("")
                .clickLogInButton();

        AlertActions alertActions = new AlertActions(driver);
        String alertText = alertActions.getAlertText();
        Assert.assertEquals(alertText, "Please fill out Username and Password.");
        alertActions.acceptAlert();
    }

    @Test(description = "Verify that clicking the Close button cancels the login process and keeps the user as a guest")
    public void verifyCancelLoginFromModal() {
        WebDriver driver = WebDriverFactory.getDriver();
        HomePage homePage = new HomePage(driver)
                .clickLoginTab()
                .enterUsername(validUsername)
                .enterPassword(validPassword)
                .clickCloseButton();
        
        Assert.assertTrue(homePage.isLoginButtonDisplayed(), "Login button should be displayed as user is guest");
    }

    @Test(description = "Verify that a logged-in user can successfully log out and terminate their session")
    public void verifySuccessfulLogout() {
        WebDriver driver = WebDriverFactory.getDriver();
        HomePage homePage = new HomePage(driver)
                .clickLoginTab()
                .enterUsername(validUsername)
                .enterPassword(validPassword)
                .clickLogInButton();

        // Check login was successful first
        Assert.assertEquals(homePage.getWelcomeMessage(), "Welcome " + validUsername);
        
        // Log out
        homePage.clickLogout();
        
        // Check log out was successful (Login button is displayed again)
        Assert.assertTrue(homePage.isLoginButtonDisplayed(), "Login button should be displayed after logout");
    }
}
