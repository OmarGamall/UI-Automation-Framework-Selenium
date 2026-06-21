package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Login {
    WebDriver driver;

    // Page constructor
    public Login(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    private final By Username = By.id("loginusername");
    private final By Password = By.id("loginpassword");
    private final By SignUpButton = By.xpath("//button[normalize-space()='Log in']");

    public Login selectLoginTab() {
        driver.findElement(By.id("login2")).click();
        return this;
    }

    // page actions
    public Login enterUsername(String username) {
        driver.findElement(Username).sendKeys(username);
        return this;
    }

    public Login enterPassword(String password) {
        driver.findElement(Password).sendKeys(password);
        return this;
    }

    public HomePage clickLogInButton() {
        driver.findElement(SignUpButton).click();
        return new HomePage(driver);
    }
}
