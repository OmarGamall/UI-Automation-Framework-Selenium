package testcases;

import drivers.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SignUpTest {

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser)
    {
        System.out.println("Setting up the test environment for browser: " + browser);
        WebDriverFactory.create(browser);
        WebDriverFactory.getDriver().get("https://www.demoblaze.com/");
    }


    @Test
    public void testSignUp()
    {

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


