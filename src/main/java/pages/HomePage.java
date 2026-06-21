package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.actions.ElementActions;

public class HomePage {
    WebDriver driver;
    private final ElementActions elementActions;

    // page constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.elementActions = new ElementActions(driver);
    }

    // Locators
    private final By welcomeMessage = By.id("nameofuser");

    public String getWelcomeMessage() {
        return elementActions.getText(welcomeMessage);
    }

}

