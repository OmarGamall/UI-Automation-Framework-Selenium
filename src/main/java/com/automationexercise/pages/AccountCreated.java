package com.automationexercise.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountCreated extends BasePage {
    public static final String EXPECTED_URL = BASE_URL + "account_created";

    private final By accountCreatedHeader = By.cssSelector("[data-qa='account-created']");
    private final By continueButton = By.cssSelector("[data-qa='continue-button']");

    public AccountCreated(WebDriver driver) {
        super(driver);
    }

    @Step("Check if 'ACCOUNT CREATED!' message is displayed")
    public boolean isAccountCreatedMessageDisplayed() {
        return elementActions.isDisplayed(accountCreatedHeader);
    }

    @Step("Click Continue button")
    public HomePage clickContinue() {
        elementActions.click(continueButton);
        return new HomePage(driver);
    }

    @Override
    public AccountCreated verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }
}
