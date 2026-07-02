package com.blazedemo.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import com.blazedemo.utils.PropertyReader;
import com.blazedemo.utils.LogsManager;

public class Retry implements IRetryAnalyzer {
    private int count = 0;
    private static final int MAX_LIMIT;

    static {
        int limit = 0; // Default to 0 retries
        try {
            String limitProp = PropertyReader.getProperty("retry.limit");
            if (limitProp != null) {
                limit = Integer.parseInt(limitProp.trim());
            }
        } catch (NumberFormatException e) {
            LogsManager.error("Invalid retry.limit configuration. Defaulting to 0.", e);
        }
        MAX_LIMIT = limit;
    }

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess()) {
            if (count < MAX_LIMIT) {
                count++;
                LogsManager.warn("Retrying test {} for the {} time(s) out of {}", result.getName(), count, MAX_LIMIT);
                return true;
            }
        }
        return false;
    }
}
