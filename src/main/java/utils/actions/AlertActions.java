package utils.actions;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.WaitManager;

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

    public String getAlertText() {
        return waitForAlert().getText();
    }

    public void acceptAlert() {
        waitForAlert().accept();
    }

    public void dismissAlert() {
        waitForAlert().dismiss();
    }

    public void sendKeysToAlert(String text) {
        waitForAlert().sendKeys(text);
    }
}
