package com.blazedemo.drivers;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import com.blazedemo.utils.PropertyReader;

public class EdgeDriverFactory extends AbstractDriver {
    @Override
    protected EdgeOptions getOptions() {
        EdgeOptions options = new EdgeOptions();
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
        return new EdgeDriver(getOptions());
    }
}
