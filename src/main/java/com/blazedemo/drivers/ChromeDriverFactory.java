package com.blazedemo.drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.blazedemo.utils.PropertyReader;

public class ChromeDriverFactory extends AbstractDriver {
    @Override
    protected ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        boolean headless = Boolean.parseBoolean(PropertyReader.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    @Override
    public WebDriver createDriver() {
        return new ChromeDriver(getOptions());
    }
}
