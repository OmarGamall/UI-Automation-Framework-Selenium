package testcases;

import drivers.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser)
    {
        System.out.println("Setting up the test environment for browser: " + browser);
        String url = utils.ConfigReader.getProperty("url", "https://www.demoblaze.com/");
        WebDriverFactory.create(browser).get(url);
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
