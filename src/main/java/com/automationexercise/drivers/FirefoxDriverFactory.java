package com.automationexercise.drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import com.automationexercise.utils.PropertyReader;

public class FirefoxDriverFactory extends AbstractDriver {
    @Override
    protected FirefoxOptions getOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.webnotifications.enabled", false);
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        boolean headless = Boolean.parseBoolean(PropertyReader.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }

    @Override
    public WebDriver createDriver() {
        WebDriver driver = new FirefoxDriver(getOptions());
        // Firefox has no --start-maximized argument, so maximize after launch.
        driver.manage().window().maximize();
        return driver;
    }
}
