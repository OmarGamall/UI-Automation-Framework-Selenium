package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;

public abstract class AbstractDriver {
    protected abstract AbstractDriverOptions<?> getOptions();
    public abstract WebDriver createDriver();
}
