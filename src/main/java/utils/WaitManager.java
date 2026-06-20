package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.ArrayList;

public class WaitManager {
    private static WebDriver driver;

    public WaitManager(WebDriver driver) {
        WaitManager.driver = driver;
    }

    public static FluentWait<WebDriver> fluentWait() {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10)) //wait for 10 seconds
                .pollingEvery(Duration.ofMillis(500))
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