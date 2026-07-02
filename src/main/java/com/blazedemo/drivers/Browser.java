package com.blazedemo.drivers;

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
    };

    public abstract AbstractDriver getDriver();

    public static Browser fromString(String browserName) {
        if (browserName == null) {
            return CHROME;
        }
        try {
            return Browser.valueOf(browserName.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            System.out.println("Unsupported browser: " + browserName + ". Defaulting to Chrome.");
            return CHROME;
        }
    }
}
