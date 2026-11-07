package com.automationexercise.drivers;

import java.util.Arrays;

public enum Browser {
    CHROME {
        @Override
        public AbstractDriver getDriver() {
            return new ChromeDriverFactory();
        }
    },
    EDGE {
        @Override
        public AbstractDriver getDriver() {
            return new EdgeDriverFactory();
        }
    },
    FIREFOX {
        @Override
        public AbstractDriver getDriver() {
            return new FirefoxDriverFactory();
        }
    };

    public abstract AbstractDriver getDriver();

    public static Browser fromString(String browserName) {
        if (browserName == null) {
            return CHROME;
        }
        try {
            return Browser.valueOf(browserName.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unsupported browser: '" + browserName + "'. Supported browsers: " + Arrays.toString(values()), e);
        }
    }
}
