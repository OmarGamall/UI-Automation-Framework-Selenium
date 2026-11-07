package com.automationexercise.testcases.ui;

import com.automationexercise.drivers.WebDriverFactory;
import com.automationexercise.models.ContactUsMessage;
import com.automationexercise.pages.ContactUs;
import com.automationexercise.pages.HomePage;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import static com.automationexercise.utils.UserDataGenerator.generateRandomContactUsMessage;

public class ContactUsTest extends BaseTest {

    @Test(groups = {TestGroups.REGRESSION, TestGroups.POSITIVE}, description = "Verify that user can submit contact us form successfully")
    @Severity(SeverityLevel.NORMAL)
    public void verifyContactUsFormSubmission() {
        WebDriver driver = WebDriverFactory.getDriver();
        // Arrange Test Data
        ContactUsMessage contactUsMessage = generateRandomContactUsMessage();
        String uploadFilePath = "/src/test/resources/test-data/upload_files/upload_sample.txt";

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToContactUs()
                .verifyPageUrl(ContactUs.EXPECTED_URL)
                .verifyGetInTouchVisible()
                .enterName(contactUsMessage.getName())
                .enterEmail(contactUsMessage.getEmail())
                .enterSubject(contactUsMessage.getSubject())
                .enterMessage(contactUsMessage.getMessage())
                .uploadFile(uploadFilePath)
                .clickSubmit()
                .acceptConfirmationAlert()
                .verifySuccessMessage(ContactUs.SUCCESS_MESSAGE)
                .clickHomeButton()
                .verifyPageUrl(HomePage.EXPECTED_URL);
    }
}
