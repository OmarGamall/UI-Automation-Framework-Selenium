package utils.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import utils.WaitManager;

public class ElementActions {
    private final WebDriver driver;
    private final WaitManager waitManager;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
    }

    public void click(By locator) {
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                new Actions(d).scrollToElement(element).perform();
                element.click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void sendKeys(By locator, String text) {
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                new Actions(d).scrollToElement(element).perform();
                element.sendKeys(text);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public String getText(By locator) {
        return waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                new Actions(d).scrollToElement(element).perform();
                String text = element.getText();
                if (text == null || text.trim().isEmpty()) {
                    return null;
                }
                return text;
            } catch (Exception e) {
                return null;
            }
        });
    }
}
