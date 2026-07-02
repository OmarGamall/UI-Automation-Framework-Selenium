# Selenium Java Test Automation Framework

This repository implements a robust, thread-safe, and parallel-ready Selenium UI Test Automation Framework built on top of Java, TestNG, and Maven.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Tech Stack](#tech-stack)
- [Directory Structure](#directory-structure)
- [Step-by-Step Architecture Components](#step-by-step-architecture-components)
  - [Step 1: Driver Factory and Thread Safety](#step-1-driver-factory-and-thread-safety)
  - [Step 2: Actions Layer & Dynamic Synchronization](#step-2-actions-layer-dynamic-synchronization)
  - [Step 3: Fluent Page Object Model (POM)](#step-3-fluent-page-object-model-pom)
  - [Step 4: Test Cases & Execution Lifecycle](#step-4-test-cases-execution-lifecycle)
  - [Step 5: Configuration Management & Scalability](#step-5-configuration-management-scalability)
  - [Step 6: Flaky Test Retry Mechanism](#step-6-flaky-test-retry-mechanism)
  - [Step 7: JSON Test Data Management & Querying](#step-7-json-test-data-management-querying)
  - [Step 8: Thread-Safe Logging and Diagnostics](#step-8-thread-safe-logging-and-diagnostics)
- [Parallel Execution Configuration](#parallel-execution-configuration)
  - [1. testng.xml (Project Root)](#1-testngxml-project-root)
  - [2. pom.xml configuration](#2-pomxml-configuration)
- [How to Run](#how-to-run)
- [Reporting (Allure)](#reporting-allure)
  - [Option A: Using the Allure Maven Plugin (Recommended)](#option-a-using-the-allure-maven-plugin-recommended)
  - [Option B: Using the Local Allure CLI Tool](#option-b-using-the-local-allure-cli-tool)

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

## Tech Stack

The framework leverages a modern, stable automation ecosystem:
- **Core Platform**: Java 21 (JDK 21)
- **Testing Engine**: TestNG (v7.12.0)
- **Web Automation**: Selenium Java (v4.44.0)
- **Build Tool**: Apache Maven
- **Logging Interface**: SLF4J (v2.0.13)
- **Logging Backend**: Apache Log4j2 (v2.23.1)
- **Reporting Engine**: Allure Reports (v2.24.0)
- **Libraries**:
  - **Lombok**: Boilerplate reduction (annotations)
  - **Jackson Databind**: JSON serialization/deserialization
  - **Jayway JsonPath**: Querying JSON files dynamically
  - **Apache Commons IO**: Utility file operations

---

## Directory Structure

```text
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── drivers
│   │   │   │   ├── AbstractDriver.java       # Factory method base class
│   │   │   │   ├── Browser.java              # Enum defining supported browsers and factories
│   │   │   │   ├── ChromeDriverFactory.java  # Chrome-specific options & instantiation
│   │   │   │   ├── EdgeDriverFactory.java    # Edge-specific options & instantiation
│   │   │   │   └── WebDriverFactory.java     # ThreadLocal lifecycle & simple factory facade
│   │   │   ├── pages
│   │   │   │   ├── HomePage.java             # Home page elements and actions
│   │   │   │   ├── Login.java                # Login page elements and actions
│   │   │   │   └── SignUp.java               # Sign up page elements and actions
│   │   │   └── utils
│   │   │       ├── AllureUtilities.java      # Cleans old Allure results before execution starts
│   │   │       ├── JsonReader.java           # Thread-safe JSON reader caching parsed DocumentContexts (JsonPath support)
│   │   │       ├── LogsManager.java          # Centralized thread-safe logging facade dynamically resolving caller class
│   │   │       ├── PropertyReader.java       # Thread-safe properties configuration reader (loads all properties)
│   │   │       ├── WaitManager.java          # Configurable FluentWait setup (configured via properties)
│   │   │       └── actions
│   │   │           ├── AlertActions.java     # Wrapper actions for browser alerts
│   │   │           └── ElementActions.java   # Wrapper actions (scrolling, clicking, waits)
│   │   └── resources
│   │       ├── allure.properties             # Allure results directory configuration
│   │       ├── browser.properties            # Browser headless option configuration
│   │       ├── env.properties                # Target application URL configuration
│   │       ├── log4j2.xml                    # Log4j2 configuration defining console and file appenders
│   │       ├── retry.properties              # Flaky test retry mechanism configuration
│   │       └── waits.properties              # Explicit / Implicit Wait timing and polling configuration
│   └── test
│       ├── java
│       │   ├── listeners
│       │   │   ├── AnnotationTransformer.java # SPI listener to programmatically bind Retry analyzer to tests
│       │   │   ├── Retry.java                 # Controls flaky test retries based on configured limit
│       │   │   ├── TestExecutionListener.java # Automatically loads properties on TestNG execution start
│       │   │   └── TestLogListener.java       # Thread-safe listener setting/clearing MDC testName for logs
│       │   └── testcases
│       │       ├── BaseTest.java             # Centralized setup & teardown for tests
│       │       ├── JsonReaderTest.java       # Test suite verifying JsonReader parsing, caching, and exceptions
│       │       ├── LoginTest.java            # Thread-safe login test cases (uses usercredentials.properties)
│       │       └── SignUpTest.java           # Thread-safe signup test cases
│       └── resources
│           ├── META-INF
│           │   └── services
│           │       └── org.testng.ITestNGListener # SPI registration for TestExecutionListener and AnnotationTransformer
│           ├── test-data
│           │   └── user-data.json            # Sample JSON test data file
│           └── usercredentials.properties     # Externalized user login test data
├── pom.xml                                   # Maven dependencies and plugin builds
├── testng.xml                                # Suite configuration for parallel execution
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
3. **`Browser` (Enum-based Driver Registry)**:
   - Defines the supported browsers (`CHROME`, `EDGE`).
   - Each enum constant overrides an abstract `getDriver()` method returning its corresponding `AbstractDriver` factory implementation, adhering to the Open-Closed Principle (OCP) at the factory consumption level.
4. **`WebDriverFactory` (Simple Factory Facade + ThreadLocal)**:
   - Houses the `ThreadLocal<WebDriver>` storage.
   - Exposes `create(browserName)` which delegates to the `Browser` enum to resolve the factory, protects it with `ThreadGuard.protect(...)`, registers the driver to the thread context, and returns it.
   - Exposes `getDriver()` to fetch the active thread's driver and `unload()` to clear thread references.

### Step 2: Actions Layer & Dynamic Synchronization
UI test failures are commonly caused by dynamic page loading or elements not being ready. We solve this at the action level through an atomic synchronization strategy:

1. **`WaitManager`**: A helper class initializing a ThreadLocal-compatible `FluentWait<WebDriver>` instance. It dynamically reads the timeout (`timeout.seconds`) and polling interval (`polling.ms`) from the configuration properties, fallback-defaulting to 10s and 500ms respectively.
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
   - `setUp()`: Consumes the `@Parameters("browser")` parameter, instantiates the driver via `WebDriverFactory.create(browser)`, fetches the target URL dynamically via `PropertyReader.getProperty("url")`, and launches the browser to navigate to it.
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

1. **Modular Configuration Files**: Split configurations by domain under resources (`src/main/resources`):
   - `env.properties`: stores application environments (e.g., target URL).
   - `browser.properties`: configures browser settings (e.g., headless execution).
   - `waits.properties`: holds explicit/fluent wait parameters (e.g., timeout, polling interval).
   - `retry.properties`: configures flaky test retries.
2. **Externalized Test Data**: Stored in `src/test/resources/usercredentials.properties` to isolate credentials and static test inputs from the test code.
3. **`PropertyReader`**: A thread-safe utility class that recursively scans resources directories (`src/main/resources` and `src/test/resources`) for all `.properties` files, merges them, and makes them available globally.
4. **TestNG SPI Execution Listener**: A `TestExecutionListener` is registered through Java SPI (`META-INF/services/org.testng.ITestNGListener`) to automatically invoke property loading at the start of TestNG execution.
5. **CLI & CI/CD Support**: Allows overriding properties by passing Java System Properties (e.g., `-Durl=https://staging.example.com` or `-Dheadless=true`). System properties take absolute precedence over the properties files values.

### Step 6: Flaky Test Retry Mechanism
To handle transient network latency, random timeouts, or minor server glitches, the framework integrates an automatic retry mechanism:

1. **`Retry` Analyzer**: A retry controller that implements `IRetryAnalyzer`. It reads the configured limit (`retry.limit`) from the configuration properties, logs the attempt, and triggers a retry if the limit isn't exceeded.
2. **`AnnotationTransformer`**: Implements `IAnnotationTransformer` to programmatically assign the `Retry` analyzer to every test method at suite startup, eliminating the need to manually annotate individual test cases with `@Test(retryAnalyzer = Retry.class)`.
3. **SPI Registration**: The `AnnotationTransformer` is registered using Java's Service Provider Interface (SPI) at `META-INF/services/org.testng.ITestNGListener`, ensuring it is loaded automatically by TestNG.

### Step 7: JSON Test Data Management & Querying
To enable data-driven testing and keep test code clean of hardcoded values, the framework includes a modern, thread-safe JSON utility:

1. **`JsonReader`**: A thread-safe reader backed by a global cache. It loads and parses JSON files into Jayway `DocumentContext` instances.
2. **Performance Optimization (Parsing Cache)**: Rather than re-parsing files on every query, `JsonReader` caches parsed representations in a `ConcurrentHashMap` so that file reading and parsing occur exactly once per file across all parallel threads.
3. **Classpath & Filesystem Resolution**: Searches the classpath first (for CI/CD and JAR packaging portability) and falls back to filesystem directory lookups using project-root paths.
4. **Resilient Querying**: Utilizes Jayway `JsonPath` configured with the Jackson Provider. Missing leaf nodes return `null` instead of throwing errors (via `SUPPRESS_EXCEPTIONS`), while syntax errors or missing files fail fast with a custom `TestDataException`.
5. **Generic Return Types**: The query method uses generic types (`public <T> T getJsonData(String jsonPath)`) to automatically cast values (e.g. strings, lists, doubles, booleans) without requiring manual casting in test cases.

### Step 8: Thread-Safe Logging and Diagnostics
To facilitate debugging, tracing, and monitoring of test executions (especially during parallel execution), the framework features a thread-safe logging architecture:

1. **SLF4J & Log4j2 Backend**: Standardized on SLF4J as the logging API and Log4j2 as the implementation engine.
2. **`LogsManager`**: A helper facade class that retrieves the active logger class dynamically from the execution call stack (`Thread.currentThread().getStackTrace()`), keeping files clean of static logger declarations.
3. **MDC (Mapped Diagnostic Context) Test Tracking**: Configured to capture the running test name dynamically. The custom `TestLogListener` (registered via TestNG SPI) automatically registers the test name into SLF4J MDC when a test begins and cleans it up when it finishes.
4. **Appenders & Outputs**:
   - **Console Appender**: Logs events to stdout with ANSI color-coding for log levels (`INFO` in green, `WARN` in yellow, `ERROR`/`FATAL` in red, `DEBUG` in blue).
   - **File Appender**: Writes formatted execution records to `target/logs/execution.log` (overwritten on each suite run).

---

## Parallel Execution Configuration

We define the parallel run strategy in TestNG and plug it into Maven:

### 1. `testng.xml` (Project Root)
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
            <suiteXmlFile>testng.xml</suiteXmlFile>
        </suiteXmlFiles>
    </configuration>
</plugin>
```

---

## How to Run

Execute the test suite from the root directory using Maven:

```bash
# Run tests with the default configuration
mvn clean test

# Run tests overriding the URL for a different environment (e.g., QA or Staging)
mvn clean test -Durl=https://staging.demoblaze.com/

# Run tests in headless mode
mvn clean test -Dheadless=true
```

---

## Reporting (Allure)

Allure generates raw test execution results under the `target/allure-results` directory (configured via `src/main/resources/allure.properties` so it applies to both Maven CLI and IDE test runs).

To prevent old test executions from accumulating in subsequent runs (especially when running directly in IDEs without a Maven `clean` command), the framework features `AllureUtilities.cleanAllureResults()` which is executed automatically at suite startup by the global TestNG listener to programmatically clear out the directory.

### Option A: Using the Allure Maven Plugin (Recommended)
You do not need to install the Allure CLI locally. Run the following Maven commands:

* **To build and serve the report dynamically in your default browser:**
  ```bash
  mvn allure:serve
  ```
* **To build a static, self-contained HTML report in the `target/allure-report` folder:**
  ```bash
  mvn allure:report
  ```

### Option B: Using the Local Allure CLI Tool
If you have the Allure Command Line Tool installed locally, you can run:

* **To serve the report:**
  ```bash
  allure serve target/allure-results
  ```
* **To generate a static report inside `target/allure-report`:**
  ```bash
  allure generate target/allure-results --clean -o target/allure-report
  allure open target/allure-report
  ```
