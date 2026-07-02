package com.blazedemo.utils;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AllureUtilities {

    /**
     * Deletes the Allure results directory quietly to ensure a clean slate before each test run.
     */
    public static void cleanAllureResults() {
        // 1. Clean Allure Results Directory
        String resultsDir = PropertyReader.getProperty("allure.results.directory", "target/allure-results");
        File resultsDirectory = new File(resultsDir);
        if (resultsDirectory.exists()) {
            System.out.println("[AllureUtilities] Cleaning allure results directory: " + resultsDirectory.getAbsolutePath());
            FileUtils.deleteQuietly(resultsDirectory);
        }

        // 2. Clean Allure Report Directory (Added)
        String reportDir = PropertyReader.getProperty("allure.report.directory", "target/allure-report");
        File reportDirectory = new File(reportDir);
        if (reportDirectory.exists()) {
            System.out.println("[AllureUtilities] Cleaning allure report directory: " + reportDirectory.getAbsolutePath());
            FileUtils.deleteQuietly(reportDirectory);
        }
    }

    /**
     * Dynamically writes environment.properties file containing environment details
     * (OS, JDK, and Test Env URL) to the Allure results directory.
     */
    public static void writeEnvironmentProperties() {
        String resultsDir = PropertyReader.getProperty("allure.results.directory", "target/allure-results");
        File resultsDirectory = new File(resultsDir);

        if (!resultsDirectory.exists()) {
            resultsDirectory.mkdirs();
        }

        File envPropertiesFile = new File(resultsDirectory, "environment.properties");
        Properties props = new Properties();

        String os = System.getProperty("os.name");
        String jdk = System.getProperty("java.version");
        String envUrl = PropertyReader.getProperty("url");

        if (os != null) {
            props.setProperty("OS", os);
        }
        if (jdk != null) {
            props.setProperty("JDK Version", jdk);
        }
        if (envUrl != null) {
            props.setProperty("Test Env URL", envUrl);
        }

        try (FileOutputStream fos = new FileOutputStream(envPropertiesFile)) {
            props.store(fos, "Allure Environment Properties");
            System.out.println("[AllureUtilities] Successfully wrote environment.properties to " + envPropertiesFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[AllureUtilities] Failed to write environment.properties: " + e.getMessage());
        }
    }
}
