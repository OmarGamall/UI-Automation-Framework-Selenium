package com.automationexercise.utils.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.automationexercise.utils.WaitManager;
import com.automationexercise.utils.LogsManager;
import org.openqa.selenium.support.ui.Select;

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

    /**
     * Sends the ENTER key to the element identified by the locator.
     *
     * @param locator The By locator of the element
     */
    public void pressEnter(By locator) {
        LogsManager.info("Sending ENTER key to element: {}", locator);
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                new Actions(d).scrollToElement(element).perform();
                element.sendKeys(Keys.ENTER);
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

    public void selectByVisibleText(By locator, String text) {
        LogsManager.info("Selecting option '{}' from drop-down [{}]", text, locator);
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                new Actions(d).scrollToElement(element).perform();
                Select select = new Select(element);
                select.selectByVisibleText(text);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void selectByValue(By locator, String value) {
        LogsManager.info("Selecting option with value '{}' from drop-down [{}]", value, locator);
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                new Actions(d).scrollToElement(element).perform();
                Select select = new Select(element);
                select.selectByValue(value);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    /**
     * Safely checks or unchecks a checkbox based on the desired target state.
     *
     * @param locator The By locator of the checkbox
     * @param select  The target state (true to check, false to uncheck)
     */
    public void setCheckboxState(By locator, boolean select) {
        LogsManager.info("Setting checkbox [{}] to: {}", locator, select);
        waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                // Scroll checkbox into viewport to prevent click intercept errors
                new Actions(d).scrollToElement(element).perform();
                // Only click the checkbox if the current selection state does not match the target state
                if (element.isSelected() != select) {
                    element.click();
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    /**
     * Retrieves the browser's native HTML5 validation message (e.g. "Please fill out this field.")
     * for a given input element using the DOM property 'validationMessage'.
     *
     * @param locator The By locator of the input element
     * @return The validation message string, or null if the element is not found
     */
    public String getHtml5ValidationMessage(By locator) {
        LogsManager.info("Retrieving HTML5 native validation message for element [{}]", locator);
        return waitManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                return element.getAttribute("validationMessage");
            } catch (Exception e) {
                return null;
            }
        });
    }

    /**
     * Waits for an element to become either absent from the DOM or invisible on the page.
     * This uses FluentWait to handle page transitions dynamically without static delays.
     *
     * @param locator The By locator of the element
     * @return true if the element becomes invisible/absent within the timeout; false otherwise
     */
    public boolean isNotDisplayed(By locator) {
        LogsManager.info("Waiting for element to be invisible/absent: {}", locator);
        try {
            return waitManager.fluentWait().until(d -> {
                try {
                    return d.findElements(locator).isEmpty() || !d.findElement(locator).isDisplayed();
                } catch (Exception e) {
                    return true;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }
}