package com.automationexercise.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class ContactUs extends BasePage {
    
    // ==========================================
    // Expected Constants
    // ==========================================
    public static final String EXPECTED_URL = BASE_URL + "contact_us";
    public static final String SUCCESS_MESSAGE = "Success! Your details have been submitted successfully.";

    // ==========================================
    // Locators
    // ==========================================
    private final By getInTouchHeader = By.xpath("//h2[contains(text(), 'Get In Touch')]");
    private final By nameInput = By.cssSelector("input[data-qa='name']");
    private final By emailInput = By.cssSelector("input[data-qa='email']");
    private final By subjectInput = By.cssSelector("input[data-qa='subject']");
    private final By messageInput = By.cssSelector("textarea[data-qa='message']");
    private final By fileUploadInput = By.name("upload_file");
    private final By submitButton = By.cssSelector("input[data-qa='submit-button']");
    private final By successMessageAlert = By.cssSelector(".status.alert-success");
    private final By homeButton = By.cssSelector("#form-section a.btn-success");

    // ==========================================
    // Constructor
    // ==========================================
    public ContactUs(WebDriver driver) {
        super(driver);
    }

    // ==========================================
    // Page Actions
    // ==========================================
    @Step("Enter name: {name}")
    public ContactUs enterName(String name) {
        elementActions.sendKeys(nameInput, name);
        return this;
    }

    @Step("Enter email: {email}")
    public ContactUs enterEmail(String email) {
        elementActions.sendKeys(emailInput, email);
        return this;
    }

    @Step("Enter subject: {subject}")
    public ContactUs enterSubject(String subject) {
        elementActions.sendKeys(subjectInput, subject);
        return this;
    }

    @Step("Enter message: {message}")
    public ContactUs enterMessage(String message) {
        elementActions.sendKeys(messageInput, message);
        return this;
    }

    @Step("Upload file: {relativeFilePath}")
    public ContactUs uploadFile(String relativeFilePath) {
        elementActions.uploadFile(fileUploadInput, relativeFilePath);
        return this;
    }

    @Step("Click Submit button")
    public ContactUs clickSubmit() {
        elementActions.click(submitButton);
        return this;
    }

    @Step("Accept the confirmation alert")
    public ContactUs acceptConfirmationAlert() {
        alertActions.acceptAlert();
        return this;
    }

    @Step("Click Home button")
    public HomePage clickHomeButton() {
        elementActions.click(homeButton);
        return new HomePage(driver);
    }

    // ==========================================
    // Verification
    // ==========================================
    @Step("Verify 'Get In Touch' header is visible")
    public ContactUs verifyGetInTouchVisible() {
        Assert.assertTrue(elementActions.isDisplayed(getInTouchHeader), "'Get In Touch' header should be visible");
        return this;
    }

    @Step("Verify success message matches: {expectedMessage}")
    public ContactUs verifySuccessMessage(String expectedMessage) {
        Assert.assertTrue(elementActions.isDisplayed(successMessageAlert), "Success message should be visible");
        Assert.assertEquals(elementActions.getText(successMessageAlert), expectedMessage, "Success message text does not match!");
        return this;
    }

    @Override
    public ContactUs verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }
}
