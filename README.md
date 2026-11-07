# Selenium Java Test Automation Framework

This repository implements a robust, thread-safe, and parallel-ready Selenium UI Test Automation Framework built on top of Java, TestNG, and Maven. It is configured to test [Automation Exercise](https://automationexercise.com/).

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Tech Stack](#tech-stack)
- [Directory Structure](#directory-structure)
- [Step-by-Step Architecture Components](#step-by-step-architecture-components)
  - [Step 1: Driver Factory and Thread Safety](#step-1-driver-factory-and-thread-safety)
  - [Step 2: Actions Layer & Dynamic Synchronization](#step-2-actions-layer-dynamic-synchronization)
  - [Step 3: Fluent Page Object Model (POM) & Component Composition](#step-3-fluent-page-object-model-pom--component-composition)
  - [Step 4: Business/Flow Facades (Facade Design Pattern)](#step-4-businessflow-facades-facade-design-pattern)
  - [Step 5: Test Cases & Execution Lifecycle](#step-5-test-cases-execution-lifecycle)
  - [Step 6: Configuration Management & Scalability](#step-6-configuration-management-scalability)
  - [Step 7: Flaky Test Retry Mechanism](#step-7-flaky-test-retry-mechanism)
  - [Step 8: JSON Test Data Management & Querying](#step-8-json-test-data-management-querying)
  - [Step 9: Thread-Safe Logging and Diagnostics](#step-9-thread-safe-logging-and-diagnostics)
- [Parallel Execution Tuning](#parallel-execution-tuning)
- [Suite Management](#suite-management)
- [How to Run](#how-to-run)
- [Running on Jenkins (Planned)](#running-on-jenkins-planned)
- [Reporting (Allure)](#reporting-allure)
  - [Option A: Using the Allure Maven Plugin (Recommended)](#option-a-using-the-allure-maven-plugin-recommended)
  - [Option B: Using the Local Allure CLI Tool](#option-b-using-the-local-allure-cli-tool)

---

## Architecture Overview

The framework is structured into distinct modular layers designed for high reliability, zero timing synchronization issues, and ease of scaling:

```
                      [ Test Cases ]
                 (TestNG Parallel Execution)
                             │
                             ▼
                     [ Facade Layer ]
                 (Business/Flow Facades)
                             │
                             ▼
              [ Page Objects & Components ]
         (Inheriting BasePage, composing components
               like the NavigationBar)
                             │
                             ▼
                     [ Actions Layer ]
         (ElementActions & AlertActions wrapper layer)
                             │
                             ▼
                    [ Driver Factory ]
        (WebDriverFactory & ThreadLocal context)
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
- **Reporting Engine**: Allure Reports (v2.24.0) with automated `@Step` annotations
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
│   │   │   │   ├── FirefoxDriverFactory.java # Firefox driver options and creation
│   │   │   │   ├── ChromeDriverFactory.java  # Chrome-specific options & instantiation
│   │   │   │   ├── EdgeDriverFactory.java    # Edge-specific options & instantiation
│   │   │   │   └── WebDriverFactory.java     # ThreadLocal lifecycle & simple factory facade
│   │   │   ├── facades
│   │   │   │   ├── LoginFacade.java          # Login flow facade (Business flow wrapper)
│   │   │   │   └── ProductFacade.java        # Product search and validation facade
│   │   │   ├── models                        # Data Transfer Objects (DTOs) for test data mapping
│   │   │   │   ├── ContactUsMessage.java     # DTO for contact us messages
│   │   │   │   ├── Product.java              # DTO for product metadata (price, availability, etc.)
│   │   │   │   └── User.java                 # DTO for user profile & credentials
│   │   │   ├── pages
│   │   │   │   ├── BasePage.java             # Parent Page class defining common URL assertions
│   │   │   │   ├── HomePage.java             # Home page elements and actions
│   │   │   │   ├── SignUpLogin.java          # Joint Login and Signup page elements and actions
│   │   │   │   ├── Products.java             # Products page elements and actions
│   │   │   │   ├── ProductDetails.java       # Product details page elements and actions
│   │   │   │   ├── Cart.java                 # Cart page elements and actions
│   │   │   │   ├── SignUp.java               # Sign up details page elements and actions
│   │   │   │   ├── DeleteAccount.java        # Delete account confirmation page
│   │   │   │   ├── ContactUs.java            # Contact us form page
│   │   │   │   └── components
│   │   │   │       └── NavigationBar.java    # Navigation bar component with covariant chaining
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
│   │       ├── browser.properties            # Default browser and headless configuration
│   │       ├── env.properties                # Target application URL configuration (automationexercise.com)
│   │       ├── log4j2.xml                    # Log4j2 configuration defining console and file appenders
│   │       ├── retry.properties              # Flaky test retry mechanism configuration
│   │       └── waits.properties              # Explicit / Implicit Wait timing and polling configuration
│   └── test
│       ├── java
│       │   ├── listeners
│       │   │   ├── AnnotationTransformer.java # SPI listener to programmatically bind Retry analyzer to tests
│       │   │   ├── Retry.java                 # Controls flaky test retries based on configured limit
│       │   │   ├── TestExecutionListener.java # Automatically loads properties on TestNG execution start
│       │   │   └── TestLogListener.java       # TestNG ITestListener logging test headers & custom assertion formatters
│       │   └── testcases
│       │       ├── JsonReaderTest.java       # Test suite verifying JsonReader parsing, caching, and exceptions
│       │       └── ui                        # UI test cases
│       │           ├── BaseTest.java         # Centralized setup & teardown for UI tests
│       │           ├── LoginTest.java        # Fluent login test cases (uses usercredentials.properties)
│       │           ├── ProductsTest.java     # Fluent products test cases (catalog search, detail view, etc.)
│       │           └── SignUpTest.java       # Fluent signup test cases (temporarily disabled)
│       └── resources
│           ├── META-INF
│           │   └── services
│           │       └── org.testng.ITestNGListener # SPI registration for TestExecutionListener and AnnotationTransformer
│           ├── test-data                     # JSON test data files loaded by JsonReader
│           │   ├── valid-user.json           # User credentials for successful login
│           │   ├── invalid-password-user.json # User details with incorrect password
│           │   ├── non-existent-user.json    # Non-existent email credentials
│           │   ├── products.json             # Product catalog details test data
│           │   └── upload_files              # Directory containing files for file upload testing
│           └── usercredentials.properties     # Externalized user login test data
├── pom.xml                                   # Maven dependencies and plugin builds
├── suites                                    # TestNG suite files, selected with -Dsuite=<name>
│   ├── smoke.xml                             # Critical happy paths
│   ├── regression.xml                        # All tests (default suite)
│   ├── positive.xml                          # Valid behaviour only
│   └── negative.xml                          # Error handling and validation only
└── README.md
```

---

## Step-by-Step Architecture Components

### Step 1: Driver Factory and Thread Safety
To support parallel test execution without browser state collision, driver creation and lifetime management are decoupled:

1. **`AbstractDriver` (Abstract Factory)**: Declares the blueprint for creating drivers and fetching options.
2. **Specific Factories (`ChromeDriverFactory` / `EdgeDriverFactory`)**:
   - Practice the *Driver Options* concept to optimize browser launches.
   - Configure arguments: `--disable-notifications` and `--start-maximized`.
   - Set `PageLoadStrategy.EAGER` for faster load times.
3. **`Browser` (Enum-based Driver Registry)**:
   - Defines the supported browsers (`CHROME`, `EDGE`, `FIREFOX`).
   - Each enum constant overrides an abstract `getDriver()` method returning its corresponding `AbstractDriver` factory implementation.
4. **`WebDriverFactory` (Simple Factory Facade + ThreadLocal)**:
   - Houses the `ThreadLocal<WebDriver>` storage.
   - Exposes `create(browserName)` which delegates to the `Browser` enum to resolve the factory, protects it with `ThreadGuard.protect(...)`, registers the driver to the thread context, and returns it.
   - Exposes `getDriver()` to fetch the active thread's driver and `unload()` to clear thread references.

### Step 2: Actions Layer & Dynamic Synchronization
UI test failures are commonly caused by dynamic page loading or elements not being ready. We solve this at the action level through an atomic synchronization strategy:

1. **`WaitManager`**: A helper class initializing a ThreadLocal-compatible `FluentWait<WebDriver>` instance. It dynamically reads the timeout (`timeout.seconds`) and polling interval (`polling.ms`) from the configuration properties.
2. **`ElementActions`**: Enforces a strict interaction protocol to maximize execution stability:
   - **Scroll First**: Automatically scrolls to the target element using W3C actions API (`Actions.scrollToElement(element).perform()`) before any interaction.
   - **Atomic Lambda Wait & Retry**: Wraps locating, scrolling, and interacting in a single try-catch lambda expression passed to `FluentWait.until(...)`.
   - **Visibility Checks**: Exposes `isDisplayed(By locator)` returning `false` instead of throwing exceptions if the element is absent.
3. **`AlertActions`**: Enforces dynamic synchronization for native browser alerts.

### Step 3: Fluent Page Object Model (POM) & Component Composition
Pages and shared layouts are modeled as reusable, fluent page objects:

1. **`BasePage` Base Class**:
   Houses shared WebDriver actions, dynamic URL initialization, and fluent page URL validation.
2. **Page-Level Constants**:
   Defines expected paths and error messages as `public static final` constants directly in their owning page classes for better cohesion and IDE autocomplete.
3. **Covariant Method Overrides**:
   Subclasses override base methods (e.g. `verifyPageUrl()`) to return their specific type, allowing continuous method chaining without explicit casting.
4. **Outcome-Based Action Chaining**:
   Divides page transitions into specific methods (e.g. `clickLogInSuccess()` vs `clickLogInFailure()`) to return the correct page object based on the test scenario.
5. **Component Composition (`NavigationBar`)**:
   Encapsulates shared headers/footers into reusable component classes composed inside page objects instead of duplicating locators.
### Step 4: Business/Flow Facades (Facade Design Pattern)
In software engineering, the **Facade Pattern** provides a simplified, higher-level interface to a larger, more complex body of code. In test automation, we apply this pattern to create **Business Workflow Helpers** or **Hybrid UI/API Orchestrators** (located in the `com.automationexercise.facades` package). These facades sit on top of lower-level Page Objects to expose simple, macro-actions to the test scripts.

This prevents test scripts from becoming cluttered with long navigation chains and page transition boilerplate, while decoupling test logic from configuration changes (e.g., swapping a UI-based login setup for a fast API-based login) without modifying the test cases.

**Example Flow in a Test:**
Instead of calling individual page elements sequentially, tests delegate complex workflows directly to the facade:
```java
new LoginFacade(driver)
        .login(validUser)
        .getNavigationBar()
        .verifyUserLabel(validUser.getUsername());
```

---

### Step 5: Test Cases & Execution Lifecycle
Tests are designed to be isolated, parameterizable, and highly readable:

1. **`BaseTest`**: A base class that houses the lifecycle hooks (`@BeforeMethod` and `@AfterMethod`).
   - `setUp()`: Reads the `browser` setting via `PropertyReader.getProperty("browser", "chrome")`, instantiates the driver via `WebDriverFactory.create(browser)`, fetches the target URL dynamically via `PropertyReader.getProperty("baseUrlWeb")`, and launches the browser to navigate to it.
   - `tearDown()`: Quits the active driver and frees up the `ThreadLocal` registry using `WebDriverFactory.unload()`.
2. **Concrete Test Classes (`LoginTest` / `SignUpTest`)**:
   - Inherit from `BaseTest` to automatically receive clean, thread-safe test environment lifecycles.
   - Test methods fetch the active thread-local driver using `WebDriverFactory.getDriver()` and run assertions.
   - Test flows are constructed as a natural sequence of operations utilizing Fluent POM method chaining:
     ```java
     new HomePage(driver)
             .verifyPageUrl(HomePage.EXPECTED_URL)
             .getNavigationBar()
             .navigateToSignUpLogin()
             .verifyPageUrl(SignUpLogin.EXPECTED_URL)
             .enterLoginEmail(validUser.getEmail())
             .enterLoginPassword(validUser.getPassword())
             .clickLogInSuccess()
             .getNavigationBar()
             .verifyUserLabel(validUser.getUsername());
     ```

---

### Step 6: Configuration Management & Scalability
To support scaling up execution across multiple test environments without modifying any code, we externalize settings:

1. **Modular Configuration Files**: Split configurations by domain under resources (`src/main/resources`).
2. **Externalized Test Data**: Stored in `src/test/resources/usercredentials.properties` to isolate credentials and static test inputs from the test code.
3. **`PropertyReader`**: A thread-safe utility class that recursively scans resources directories for all `.properties` files and merges them.
4. **TestNG SPI Execution Listener**: A `TestExecutionListener` is registered through Java SPI (`META-INF/services/org.testng.ITestNGListener`) to automatically invoke property loading at the start of TestNG execution.
5. **CLI & CI/CD Support**: Allows overriding properties by passing Java System Properties (e.g., `-DbaseUrlWeb=https://staging.automationexercise.com`).

### Step 7: Flaky Test Retry Mechanism
To handle transient network latency, the framework integrates an automatic retry mechanism:

1. **`Retry` Analyzer**: A retry controller that implements `IRetryAnalyzer`.
2. **`AnnotationTransformer`**: Implements `IAnnotationTransformer` to programmatically assign the `Retry` analyzer to every test method at suite startup.
3. **SPI Registration**: The `AnnotationTransformer` is registered using Java's Service Provider Interface (SPI) at `META-INF/services/org.testng.ITestNGListener`.

### Step 8: Type-Safe JSON Test Data Management (Records & POJOs)
To enable robust, scalable, and data-driven testing, the framework maps JSON files directly to Java Records:

1. **`JsonReader`**: A thread-safe reader backed by a global cache.
2. **Performance Optimization (Parsing Cache)**: Caches parsed representations in a `ConcurrentHashMap` so that file reading and parsing occur exactly once per file across all parallel threads.
3. **Classpath & Filesystem Resolution**: Searches the classpath first and falls back to filesystem directory lookups.
4. **Resilient Querying**: Utilizes Jayway `JsonPath` configured with the Jackson Provider.
5. **Type-Safe POJO & Record Mapping**:
   - Instead of querying raw string paths directly in test cases (which leads to brittle, runtime-only errors and copy-paste typos), the framework supports deserializing JSON structures directly to immutable Java Records (e.g. `User.java`).
   - Exposes `.as(Class<T> clazz)` to map the entire JSON root to a Java object.
   - Exposes `.getJsonData(String jsonPath, Class<T> clazz)` to extract and deserialize a specific nested JSON path.
   - **Advantage**: Provides compile-time safety, refactoring support, and full IDE auto-complete (IntelliSense) when writing tests.

### Step 9: Thread-Safe Logging and Diagnostics
To facilitate debugging, tracing, and monitoring of test executions, the framework features a thread-safe logging architecture:

1. **SLF4J & Log4j2 Backend**: Standardized on SLF4J as the logging API and Log4j2 as the implementation engine.
2. **`LogsManager`**: A helper facade class that retrieves the active logger class dynamically from the execution call stack (`Thread.currentThread().getStackTrace()`).
3. **MDC (Mapped Diagnostic Context) Test Tracking**: Configured to capture the running test name dynamically. The custom `TestLogListener` (registered via TestNG SPI) automatically registers the test name into SLF4J MDC when a test begins and cleans it up when it finishes.
4. **Custom Assertion Error Logging**: 
   - `TestLogListener` intercepts TestNG assertion failures, parses the mismatch details, and writes a clean `Expected` vs `Actual` comparison directly into the log file (`target/logs/execution.log`) for rapid debugging.

---

## Parallel Execution Tuning

By default, the framework is configured to run tests concurrently at the method level with 3 concurrent threads to optimize execution speed.

This is set in each suite file under `suites/`, so execution behaviour stays identical whether you run tests from your IDE or the Maven CLI.

*   **Parallel Mode**: `methods` (executes individual `@Test` methods in parallel)
*   **Default Thread Pool**: `3` concurrent threads

To change the thread count or parallel mode, modify the `thread-count` and `parallel` attributes in the suite file you want to change (for example, `suites/smoke.xml`).

---

## Suite Management

Tests are organised with **TestNG groups**, not separate folders or classes, because one test can belong to several suites. For example, a successful login is both `smoke` and `positive`. The suites answer two different questions:

- **When do I run it?** `smoke` runs on every change, and `regression` runs before a release.
- **What does it check?** `positive` covers valid behaviour, and `negative` covers error handling.

| Suite | Tests | Purpose | Suite file |
|---|---|---|---|
| `smoke` | 4 | Quick check that the main features work | `suites/smoke.xml` |
| `regression` | 20 | Full check of everything (default) | `suites/regression.xml` |
| `positive` | 8 | Valid behaviour only | `suites/positive.xml` |
| `negative` | 12 | Error handling and validation only | `suites/negative.xml` |

The smoke suite holds one happy path per critical feature: successful login, successful sign-up, product search, and catalog/add to cart.

### Tagging a new test

Group names live in `TestGroups.java`. Every test gets `REGRESSION` plus either `POSITIVE` or `NEGATIVE`. Add `SMOKE` only if the test is a critical happy path, so the suite stays small.

```java
@Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "...")
```

### How suite selection works

`pom.xml` builds the suite path from a `suite` property, which defaults to `regression`:

```xml
<suiteXmlFile>suites/${suite}.xml</suiteXmlFile>
```

- **Why `-Dsuite` and not `-Dgroups`:** Each suite file states exactly what runs, works from the IDE, and can have its own thread count. Surefire's docs warn that suite XML files are incompatible with several of its other parameters, so this avoids depending on how it combines them.
- **Why not Maven profiles:** Only the file name changes between suites, so one property is enough. Profiles become useful when several settings must change together, such as a future `ci` profile.
- **Adding a suite:** Add a group to `TestGroups.java`, tag the tests, and add `suites/<name>.xml`. No `pom.xml` change is needed.

> **Note:** `@BeforeMethod` in `BaseTest` must keep `alwaysRun = true`. Without it, TestNG skips the driver setup when filtering by group, and tests fail with a null driver.

---

## How to Run

Execute a suite from the root directory using Maven:

```bash
# Run the default suite (regression)
mvn clean test

# Run a specific suite
mvn clean test -Dsuite=smoke
mvn clean test -Dsuite=regression
mvn clean test -Dsuite=positive
mvn clean test -Dsuite=negative

# Run tests overriding the URL for a different environment (e.g., QA or Staging)
mvn clean test -DbaseUrlWeb=https://staging.automationexercise.com/

# Run a suite on a specific browser
mvn clean test -Dsuite=smoke -Dbrowser=firefox

# Run a suite in headless mode, or with a visible browser
mvn clean test -Dsuite=smoke -Dheadless=true
mvn clean test -Dsuite=smoke -Dheadless=false
```

From the IDE, right-click a file in `suites/` and run it.

### Runtime Configuration

These settings are read when the tests run. A `-D` value on the command line overrides the value in the properties file.

| Setting | Default is set in | CLI override | Accepted values |
|---|---|---|---|
| `browser` | `browser.properties` | `-Dbrowser=edge` | `chrome`, `edge`, `firefox` |
| `headless` | `browser.properties` | `-Dheadless=true` | `true`, `false` |
| `baseUrlWeb` | `env.properties` | `-DbaseUrlWeb=https://...` | Any URL |

An unsupported browser name stops the run with an error, rather than silently using a different browser.

---

## Running on Jenkins (Planned)

Jenkins is not set up yet. The plan is for it to call the same command used locally, with `SUITE` and `BROWSER` choice parameters:

```bash
mvn clean test -Dsuite=${SUITE} -Dbrowser=${BROWSER} -Dheadless=true
```

| Trigger | Suite |
|---|---|
| Every push | `smoke` |
| Nightly or before a release | `regression` |
| Manual, when needed | `positive` or `negative` |

Things to handle when setting it up:

- **One suite per build:** `mvn clean` and `AllureUtilities.cleanAllureResults()` both wipe earlier results, so running two suites in one build leaves only the last one in the report.
- **Maven:** The project has no Maven wrapper (`mvnw`), so Maven must be configured as a Jenkins tool.
- **Browsers:** The agent needs each browser you offer installed. Pass `-Dheadless=true` so the build doesn't depend on the value in `browser.properties`.
- **One browser per build:** To cover all three browsers, use three builds or a matrix job, which also keeps each browser's Allure report separate.
- **Reports after failures:** Failed tests fail the Maven build, so publish the Allure report in a `post { always { ... } }` block.
- **Live site:** Tests run against the real site, so outages or slowness can fail a build even when the code is fine.

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

