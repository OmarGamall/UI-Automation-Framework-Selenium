# Selenium Java Test Automation Framework

This repository implements a robust, thread-safe, and parallel-ready Selenium UI Test Automation Framework built on top of Java, TestNG, and Maven.

---

## Architecture Overview

The framework is structured into distinct modular layers designed for high reliability, zero timing synchronization issues, and ease of scaling:

```
[ Test Cases ] (TestNG, Parallel)
       │
       ▼
[ Page Objects ] (POM Layer)
       │
       ▼
[ Actions Layer ] (ElementActions + WaitManager)
       │
       ▼
[ Driver Factory ] (AbstractDriver + WebDriverFactory + ThreadLocal)
```

---

## Directory Structure

```text
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── drivers
│   │   │   │   ├── AbstractDriver.java       # Factory method base class
│   │   │   │   ├── ChromeDriverFactory.java  # Chrome-specific options & instantiation
│   │   │   │   ├── EdgeDriverFactory.java    # Edge-specific options & instantiation
│   │   │   │   └── WebDriverFactory.java     # ThreadLocal lifecycle & simple factory facade
│   │   │   ├── pages
│   │   │   │   ├── HomePage.java             # Home page elements and actions
│   │   │   │   ├── Login.java                # Login page elements and actions
│   │   │   │   └── SignUp.java               # Sign up page elements and actions
│   │   │   └── utils
│   │   │       ├── ConfigReader.java         # Thread-safe environment properties configuration reader
│   │   │       ├── WaitManager.java          # Configurable non-static FluentWait setup
│   │   │       └── actions
│   │   │           ├── AlertActions.java     # Wrapper actions for browser alerts
│   │   │           └── ElementActions.java   # Wrapper actions (scrolling, clicking, waits)
│   │   └── resources
│   │       └── config.properties             # Target URL configuration property file
│   └── test
│       └── java
│           └── testcases
│               ├── BaseTest.java             # Centralized setup & teardown for tests
│               ├── LoginTest.java            # Thread-safe login test cases
│               └── SignUpTest.java           # Thread-safe signup test cases
│       └── resources
│           └── testng.xml                    # Suite configuration for parallel execution
├── pom.xml                                   # Maven dependencies and plugin builds
└── README.md
```

---

## Step-by-Step Architecture Components

### Step 1: Driver Factory and Thread Safety
To support parallel test execution without browser state collision, driver creation and lifetime management are decoupled:

1. **`AbstractDriver` (Abstract Factory)**: Declares the blueprint for creating drivers and fetching options.
   - `protected abstract AbstractDriverOptions<?> getOptions()`
   - `public abstract WebDriver createDriver()`
2. **Specific Factories (`ChromeDriverFactory` / `EdgeDriverFactory`)**:
   - Practice the *Driver Options* concept to optimize browser launches.
   - Configure arguments: `--disable-notifications` and `--start-maximized`.
   - Set `PageLoadStrategy.EAGER` for faster load times.
3. **`WebDriverFactory` (Simple Factory Facade + ThreadLocal)**:
   - Houses the `ThreadLocal<WebDriver>` storage.
   - Exposes `create(browserName)` which retrieves the correct factory internally, registers the initialized driver to the thread context, and returns it.
   - Exposes `getDriver()` to fetch the active thread's driver and `unload()` to clear thread references.

### Step 2: Actions Layer & Dynamic Synchronization
UI test failures are commonly caused by dynamic page loading or elements not being ready. We solve this at the action level through an atomic synchronization strategy:

1. **`WaitManager`**: A helper class initializing a ThreadLocal-compatible `FluentWait<WebDriver>` instance. It is pre-configured with a 10-second timeout, 500ms polling intervals, and a preset list of ignored exceptions to support native Selenium actions.
2. **`ElementActions`**: Enforces a strict interaction protocol to maximize execution stability:
   - **Scroll First**: Automatically scrolls to the target element using W3C actions API (`Actions.scrollToElement(element).perform()`) before any interaction.
   - **Atomic Lambda Wait & Retry**: Wraps locating, scrolling, and interacting in a single try-catch lambda expression passed to `FluentWait.until(...)`. If *any* exception occurs during locating or interacting (e.g., stale elements, click interception), the lambda returns `false` or `null` to prompt `FluentWait` to poll and retry the entire sequence again.
   - **Empty Text Handling**: When retrieving text, if the text is null or blank (common during async AJAX content rendering), the lambda returns `null`, prompting the wait engine to keep polling until the element contains actual text.
   - **Visibility Checks**: Exposes `isDisplayed(By locator)` returning `false` instead of throwing exceptions if the element is absent or doesn't display before the timeout.
3. **`AlertActions`**: Enforces dynamic synchronization for native browser alerts:
   - **Wait for Alert**: Automatically polls using `ExpectedConditions.alertIsPresent()` until the alert is ready.
   - **Encapsulated Actions**: Provides clean, fluent wrappers for alert retrieval and interactions (`getAlertText()`, `acceptAlert()`, `dismissAlert()`, and `sendKeysToAlert(String)`).

### Step 3: Fluent Page Object Model (POM)
Pages (like `Login` and `HomePage`) encapsulate locators (`By`) and business actions using the **Fluent Interface (Method Chaining)** pattern:
- **State Navigation / Return Types**:
  - Actions that stay on the same page/modal return `this` to allow chained actions.
  - Actions that transition to a new page or dismiss a modal return a new instance of the destination Page Object (e.g. `clickLoginTab()` returns `Login`, `clickLogInButton()` returns `HomePage`).
- **No Hardcoded Locators**: All element locators are strictly declared as `private final By` fields at the top of the page object classes, ensuring zero inline locator instantiation in methods.
- **Zero Locator Duplication**: Common elements (like navigation header tabs) reside strictly on the `HomePage` instead of being duplicated in child page/modal classes.
- **Action Delegation**: Constructors accept the thread's `WebDriver` and instantiate `ElementActions`. Actions delegate all browser calls to `ElementActions`, keeping page classes free of raw driver calls and timing logic.

### Step 4: Test Cases & Execution Lifecycle
Tests are designed to be isolated, parameterizable, and highly readable:

1. **`BaseTest`**: A base class that houses the lifecycle hooks (`@BeforeMethod` and `@AfterMethod`).
   - `setUp()`: Consumes the `@Parameters("browser")` parameter, instantiates the driver via `WebDriverFactory.create(browser)`, fetches the target URL dynamically via `ConfigReader.getProperty("url")`, and launches the browser to navigate to it.
   - `tearDown()`: Quits the active driver and frees up the `ThreadLocal` registry using `WebDriverFactory.unload()`.
2. **Concrete Test Classes (`LoginTest` / `SignUpTest`)**:
   - Inherit from `BaseTest` to automatically receive clean, thread-safe test environment lifecycles.
   - Test methods fetch the active thread-local driver using `WebDriverFactory.getDriver()` and run assertions.
   - Test flows are constructed as a natural sequence of operations utilizing Fluent POM method chaining:
     ```java
     String welcomeMessage = new HomePage(driver)
             .clickLoginTab()
             .enterUsername("Omar Gamal")
             .enterPassword("123")
             .clickLogInButton()
             .getWelcomeMessage();
     ```

---

### Step 5: Configuration Management & Scalability
To support scaling up execution across multiple test environments (such as Dev, QA, Staging, and Production) without modifying any code, we externalize settings:

1. **`config.properties`**: A centralized properties file in the resources folder containing default key-value pairs (e.g. `url=https://www.demoblaze.com/`).
2. **`ConfigReader`**: A thread-safe utility class that loads configurations once in a static initialization block.
3. **CLI & CI/CD Support**: Allows overriding properties by passing Java System Properties (e.g., `-Durl=https://staging.example.com`). System properties take absolute precedence over the properties file values.

---

## Parallel Execution Configuration

We define the parallel run strategy in TestNG and plug it into Maven:

### 1. `src/test/resources/testng.xml`
Defines suite settings to run tests in parallel at the class level:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="TestSuite" parallel="classes" thread-count="2">
    <test name="ParallelTests">
        <parameter name="browser" value="chrome"/>
        <classes>
            <class name="testcases.LoginTest"/>
            <class name="testcases.SignUpTest"/>
        </classes>
    </test>
</suite>
```

### 2. `pom.xml` configuration
Instructs the `maven-surefire-plugin` to run using the `testng.xml` suite:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.5</version>
    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
        </suiteXmlFiles>
    </configuration>
</plugin>
```

---

## How to Run

Execute the test suite from the root directory using Maven:

```bash
# Run tests with the default URL configured in config.properties
mvn clean test

# Run tests overriding the URL for a different environment (e.g., QA or Staging)
mvn clean test -Durl=https://staging.demoblaze.com/
```
