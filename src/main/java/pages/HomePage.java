package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    WebDriver driver;

    // page constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    private final By welcomeMessage = By.id("nameofuser");

    public String getWelcomeMessage() {
        return driver.findElement(welcomeMessage).getText();
    }

}
