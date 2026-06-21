package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.actions.ElementActions;

public class Login {
    WebDriver driver;
    private final ElementActions elementActions;

    // Page constructor
    public Login(WebDriver driver) {
        this.driver = driver;
        this.elementActions = new ElementActions(driver);
    }

    // Locators
    private final By Username = By.id("loginusername");
    private final By Password = By.id("loginpassword");
    private final By SignUpButton = By.xpath("//button[normalize-space()='Log in']");

    public Login selectLoginTab() {
        elementActions.click(By.id("login2"));
        return this;
    }

    // page actions
    public Login enterUsername(String username) {
        elementActions.sendKeys(Username, username);
        return this;
    }

    public Login enterPassword(String password) {
        elementActions.sendKeys(Password, password);
        return this;
    }

    public HomePage clickLogInButton() {
        elementActions.click(SignUpButton);
        return new HomePage(driver);
    }
}

