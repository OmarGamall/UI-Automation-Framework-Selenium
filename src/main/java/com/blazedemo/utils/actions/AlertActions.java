package com.blazedemo.utils.actions;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.blazedemo.utils.WaitManager;
import com.blazedemo.utils.LogsManager;

public class AlertActions {
    private final WebDriver driver;
    private final WaitManager waitManager;

    public AlertActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
    }

    private Alert waitForAlert() {
        return waitManager.fluentWait().until(ExpectedConditions.alertIsPresent());
    }

    /**
     * get the text of the alert
     * @return text in the alert
     */
    public String getAlertText() {
        String text = waitForAlert().getText();
        LogsManager.info("Retrieved text '{}' from alert", text);
        return text;
    }

    /**
     * accept the alert
     */
    public void acceptAlert() {
        LogsManager.info("Accepting alert");
        waitForAlert().accept();
    }

    /**
     * dismiss the alert
     */
    public void dismissAlert() {
        LogsManager.info("Dismissing alert");
        waitForAlert().dismiss();
    }

    /**
     * send keys to the alert
     * @param text the text to send
     */
    public void sendKeysToAlert(String text) {
        LogsManager.info("Typing '{}' into alert", text);
        waitForAlert().sendKeys(text);
    }
}
