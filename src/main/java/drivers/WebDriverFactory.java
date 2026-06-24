package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;

public class WebDriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private static AbstractDriver getDriver(String browserName) {
        if (browserName == null) {
            browserName = "chrome";
        }
        switch (browserName.toLowerCase().trim()) {
            case "chrome":
                return new ChromeDriverFactory();
            case "edge":
                return new EdgeDriverFactory();
            default:
                System.out.println("Unsupported browser: " + browserName + ". Defaulting to Chrome.");
                return new ChromeDriverFactory();
        }
    }

    public static WebDriver create(String browserName) {
        WebDriver driver = ThreadGuard.protect(getDriver(browserName).createDriver());
        driverThreadLocal.set(driver);
        return driverThreadLocal.get();
    }

    public static WebDriver getDriver() {

        return driverThreadLocal.get();
    }

    public static void unload() {
        driverThreadLocal.remove();
    }
}
