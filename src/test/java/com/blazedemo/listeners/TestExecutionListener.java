package com.blazedemo.listeners;

import com.blazedemo.utils.AllureUtilities;
import org.testng.IExecutionListener;
import com.blazedemo.utils.PropertyReader;

public class TestExecutionListener implements IExecutionListener {

    @Override
    public void onExecutionStart() {
        System.out.println("[TestExecutionListener] TestNG Execution starting. Loading all framework properties...");
        PropertyReader.loadAllProperties();
        AllureUtilities.cleanAllureResults();
    }

    @Override
    public void onExecutionFinish() {
        System.out.println("[TestExecutionListener] TestNG Execution finished.");
        AllureUtilities.writeEnvironmentProperties();
    }
}
