package listeners;

import org.testng.IExecutionListener;
import utils.PropertyReader;

public class TestExecutionListener implements IExecutionListener {

    @Override
    public void onExecutionStart() {
        System.out.println("[TestExecutionListener] TestNG Execution starting. Loading all framework properties...");
        PropertyReader.loadAllProperties();
        utils.AllureUtilities.cleanAllureResults();
    }

    @Override
    public void onExecutionFinish() {
        System.out.println("[TestExecutionListener] TestNG Execution finished.");
    }
}
