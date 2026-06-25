package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;

public class WebDriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private static AbstractDriver getDriver(String browserName) {
        return Browser.fromString(browserName).getDriver();
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
