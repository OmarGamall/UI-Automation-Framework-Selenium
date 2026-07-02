package com.blazedemo.utils.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.blazedemo.utils.WaitManager;
import com.blazedemo.utils.LogsManager;

public class ElementActions {
    private final WebDriver driver;
    private final WaitManager waitManager;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
    }

    public void click(By locator) {
        LogsManager.info("Clicking on element: {}", locator);
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
        LogsManager.info("Typing text in element [{}]: '{}'", locator, text);
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                new Actions(d).scrollToElement(element).perform();
                element.clear();
                element.sendKeys(text);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public String getText(By locator) {
        String text = waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                new Actions(d).scrollToElement(element).perform();
                String t = element.getText();
                if (t == null || t.trim().isEmpty()) {
                    return null;
                }
                return t;
            } catch (Exception e) {
                return null;
            }
        });
        LogsManager.info("Retrieved text '{}' from element [{}]", text, locator);
        return text;
    }

    public boolean isDisplayed(By locator) {
        boolean isDisplayed = false;
        try {
            isDisplayed = waitManager.fluentWait().until(d -> {
                try {
                    return d.findElement(locator).isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            // Ignore exception, return false
        }
        LogsManager.info("Checking display status of element [{}]: {}", locator, isDisplayed);
        return isDisplayed;
    }

    public void uploadFile(By locator, String filePath) {
        String FileAbsolutePath = System.getProperty("user.dir") + filePath;
        LogsManager.info("Uploading file '{}' to element [{}]", FileAbsolutePath, locator);
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                new Actions(d).scrollToElement(element).perform();
                element.sendKeys(FileAbsolutePath);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    // method to scroll to element using javascript executor
    public void scrollToElementJs(By locator) {
        LogsManager.info("Scrolling to element using JS: {}", locator);
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center', behaviour: 'auto', inline: 'center'});", element);
    }
}
