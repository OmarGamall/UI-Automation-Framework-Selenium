package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.ArrayList;

public class WaitManager {
    private  WebDriver driver;

    public WaitManager(WebDriver driver) {
        this.driver = driver;
    }

    public FluentWait<WebDriver> fluentWait() {
        int timeoutSeconds = Integer.parseInt(PropertyReader.getProperty("timeout.seconds", "10"));
        long pollingMs = Long.parseLong(PropertyReader.getProperty("polling.ms", "500"));

        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollingMs))
                .ignoreAll(getExceptions());
    }

    // Define the exceptions to be ignored
    private static ArrayList<Class<? extends Exception>> getExceptions() {
        ArrayList<Class<? extends Exception>> exceptions = new ArrayList<>();
        exceptions.add(NoSuchElementException.class);
        exceptions.add(StaleElementReferenceException.class);
        exceptions.add(ElementNotInteractableException.class);
        exceptions.add(ElementClickInterceptedException.class);
        return exceptions;
    }
}