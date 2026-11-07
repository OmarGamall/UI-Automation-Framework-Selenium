package com.automationexercise.testcases.ui;

/**
 * TestNG group names used to build the suites in the suites/ folder.
 * Every test has REGRESSION plus either POSITIVE or NEGATIVE. Critical happy paths also have SMOKE.
 */
public final class TestGroups {

    public static final String SMOKE = "smoke";
    public static final String REGRESSION = "regression";
    public static final String POSITIVE = "positive";
    public static final String NEGATIVE = "negative";

    private TestGroups() {
    }
}
