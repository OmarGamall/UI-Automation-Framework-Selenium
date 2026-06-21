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
│   │   └── java
│   │       ├── drivers
│   │       │   ├── AbstractDriver.java       # Factory method base class
│   │       │   ├── ChromeDriverFactory.java  # Chrome-specific options & instantiation
│   │       │   ├── EdgeDriverFactory.java    # Edge-specific options & instantiation
│   │       │   └── WebDriverFactory.java     # ThreadLocal lifecycle & simple factory facade
│   │       ├── pages
│   │       │   ├── HomePage.java             # Home page elements and actions
│   │       │   ├── Login.java                # Login page elements and actions
│   │       │   └── SignUp.java               # Sign up page elements and actions
│   │       └── utils
│   │           ├── WaitManager.java          # Configurable non-static FluentWait setup
│   │           └── actions
│   │               └── ElementActions.java   # Wrapper actions (scrolling, clicking, waits)
│   └── test
│       └── java
│           └── testcases
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
UI test failures are commonly caused by dynamic loading or elements not being ready. We solve this at the action level:

1. **`WaitManager`**: A non-static helper class initializing `FluentWait<WebDriver>` configured to ignore common exceptions (`NoSuchElementException`, `StaleElementReferenceException`, `ElementNotInteractableException`, `ElementClickInterceptedException`).
2. **`ElementActions`**: Enforces a strict action protocol:
   - **Scroll First**: Automatically scrolls to the target element using `Actions.scrollToElement(element).perform()` before interacting.
   - **Retry-on-Fail**: Wraps finding and interacting inside `waitManager.fluentWait().until(...)` with a custom lambda. If an action fails with an ignored exception, it automatically catches it, pauses, and retries the entire sequence.
   - **Empty Text Handling**: When retrieving text, if the text is empty/blank (as during async requests), it returns `null` inside the lambda, forcing the fluent wait to poll until the text is populated.

### Step 3: Page Object Model (POM)
Pages (like `Login` and `HomePage`) encapsulate locators (`By`) and business actions.
- Constructors accept the thread's `WebDriver` and instantiate `ElementActions`.
- Actions delegate all browser calls to `ElementActions`, keeping page classes free of raw driver calls and synchronization logic.

### Step 4: Test Cases & Execution Lifecycle
Tests are designed to be isolated, parameterizable, and clean:
- `setUp()`: Accepts a `@Parameters("browser")` value, creates the isolated driver using `WebDriverFactory.create(browser)`, and navigates to the URL.
- Test methods: Use `WebDriverFactory.getDriver()` to retrieve the current thread's driver and pass it to page objects.
- `tearDown()`: Shuts down the browser and cleans up thread-local storage using `WebDriverFactory.unload()`.

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
mvn clean test
```
