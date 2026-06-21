package testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SignUpTest {
    WebDriver driver;

    @BeforeMethod
    public void setUp()
    {
        System.out.println("Setting up the test environment");
        driver = new ChromeDriver();
        driver.get("https://www.demoblaze.com/");
    }


    @Test
    public void testSignUp()
    {

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown()
    {
        System.out.println("Tearing down the test environment");
        driver.quit();
    }
}
