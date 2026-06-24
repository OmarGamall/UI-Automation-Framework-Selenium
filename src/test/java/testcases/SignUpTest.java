package testcases;

import drivers.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.actions.AlertActions;

public class SignUpTest extends BaseTest {

    @Test(description = "Verify successful signup with unique valid credentials")
    public void testSignUp()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        String uniqueUser = "User" + System.currentTimeMillis();
        
        new HomePage(driver)
                .clickSignUpTab()
                .enterUsername(uniqueUser)
                .enterPassword("password123")
                .clickSignUpButton();

        AlertActions alertActions = new AlertActions(driver);
        String alertText = alertActions.getAlertText();
        Assert.assertEquals(alertText, "Sign up successful.");
        alertActions.acceptAlert();
    }
}


