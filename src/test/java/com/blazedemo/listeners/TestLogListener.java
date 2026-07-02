package com.blazedemo.listeners;

import org.slf4j.MDC;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestLogListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        MDC.put("testName", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        MDC.remove("testName");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        MDC.remove("testName");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        MDC.remove("testName");
    }
}
