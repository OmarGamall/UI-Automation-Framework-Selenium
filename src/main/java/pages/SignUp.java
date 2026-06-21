package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignUp {
    WebDriver driver;

    // Page constructor
    SignUp(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    private final By Username = By.id("sign-username");
    private final By Password = By.id("sign-password");
    private final By SignUpButton = By.xpath("//button[normalize-space()='Sign up']");

    // page actions
    SignUp enterUsername(String username) {
        driver.findElement(Username).sendKeys(username);
        return this;
    }

    SignUp enterPassword(String password) {
        driver.findElement(Password).sendKeys(password);
        return this;
    }

    HomePage clickSignUpButton() {
        driver.findElement(SignUpButton).click();
        return new HomePage(driver);
    }
}
