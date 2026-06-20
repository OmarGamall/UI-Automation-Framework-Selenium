package testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp()
    {
        System.out.println("Setting up the test environment");
        driver = new ChromeDriver();
        driver.get("https://automationexercise.com/");
        throw new RuntimeException("Dummy exception to simulate setup failure");
    }

    @Test
    public void testValidLogin()
    {
        Assert.assertEquals(1, 2 ,"dummy failed test");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown()
    {
        System.out.println("Tearing down the test environment");
        driver.quit();
    }
}
