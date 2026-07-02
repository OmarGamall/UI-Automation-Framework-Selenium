package com.blazedemo.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import com.blazedemo.utils.LogsManager;

public class WebDriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private static AbstractDriver resolveDriverFactory(String browserName) {
        return Browser.fromString(browserName).getDriver();
    }

    public static WebDriver create(String browserName) {
        LogsManager.info("Resolving factory and creating driver instance for: {}", browserName);
        WebDriver driver = ThreadGuard.protect(resolveDriverFactory(browserName).createDriver());
        driverThreadLocal.set(driver);
        LogsManager.info("WebDriver instance created and stored in ThreadLocal.");
        return driver;
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void unload() {
        LogsManager.info("Removing WebDriver instance from ThreadLocal.");
        driverThreadLocal.remove();
    }
}
