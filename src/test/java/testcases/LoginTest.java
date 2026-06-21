package testcases;

import drivers.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.Login;

public class LoginTest {

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser)
    {
        System.out.println("Setting up the test environment for browser: " + browser);
        WebDriverFactory.create(browser);
        WebDriverFactory.getDriver().get("https://www.demoblaze.com/");
    }


    @Test
    public void testLogin()
    {
        String welcomeMessage = new Login(WebDriverFactory.getDriver())
                .selectLoginTab()
                .enterUsername("Omar Gamal")
                .enterPassword("123")
                .clickLogInButton().getWelcomeMessage();
        Assert.assertEquals(welcomeMessage, "Welcome Omar Gamal");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown()
    {
        System.out.println("Tearing down the test environment");
        WebDriver driver = WebDriverFactory.getDriver();
        if (driver != null) {
            driver.quit();
        }
        WebDriverFactory.unload();
    }
}


