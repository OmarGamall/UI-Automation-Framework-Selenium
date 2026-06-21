package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.actions.ElementActions;

public class Login {
    
    // ==========================================
    // Fields & Drivers
    // ==========================================
    private final WebDriver driver;
    private final ElementActions elementActions;

    // ==========================================
    // Constructor
    // ==========================================
    public Login(WebDriver driver) {
        this.driver = driver;
        this.elementActions = new ElementActions(driver);
    }

    // ==========================================
    // Locators
    // ==========================================
    private final By usernameInput = By.id("loginusername");
    private final By passwordInput = By.id("loginpassword");
    private final By loginTab = By.id("login2");
    private final By loginButton = By.xpath("//button[normalize-space()='Log in']");
    private final By closeButton = By.xpath("//div[@id='logInModal']//button[normalize-space()='Close']");

    // ==========================================
    // Page Actions
    // ==========================================
    public Login selectLoginTab() {
        elementActions.click(loginTab);
        return this;
    }

    public Login enterUsername(String username) {
        elementActions.sendKeys(usernameInput, username);
        return this;
    }

    public Login enterPassword(String password) {
        elementActions.sendKeys(passwordInput, password);
        return this;
    }

    public HomePage clickLogInButton() {
        elementActions.click(loginButton);
        return new HomePage(driver);
    }

    public HomePage clickCloseButton() {
        elementActions.click(closeButton);
        return new HomePage(driver);
    }
}

