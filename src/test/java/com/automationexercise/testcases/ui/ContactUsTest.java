package com.automationexercise.testcases.ui;

import com.automationexercise.drivers.WebDriverFactory;
import com.automationexercise.pages.ContactUs;
import com.automationexercise.pages.HomePage;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class ContactUsTest extends BaseTest {

    @Test(description = "Verify that user can submit contact us form successfully")
    @Severity(SeverityLevel.NORMAL)
    public void verifyContactUsFormSubmission() {
        WebDriver driver = WebDriverFactory.getDriver();
        
        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToContactUs()
                .verifyPageUrl(ContactUs.EXPECTED_URL)
                .verifyGetInTouchVisible()
                .enterName("John Doe")
                .enterEmail("john.doe@example.com")
                .enterSubject("Test Automation Feedback")
                .enterMessage("This is an automated test message to verify the contact us form submission flow.")
                .uploadFile("/src/test/resources/test-data/upload_files/upload_sample.txt")
                .clickSubmit()
                .acceptConfirmationAlert()
                .verifySuccessMessage(ContactUs.SUCCESS_MESSAGE)
                .clickHomeButton()
                .verifyPageUrl(HomePage.EXPECTED_URL);
    }
}
