package com.automationexercise.testcases.ui;

import com.automationexercise.drivers.WebDriverFactory;
import com.automationexercise.models.User;
import com.automationexercise.pages.AccountCreated;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.SignUp;
import com.automationexercise.pages.SignUpLogin;
import com.automationexercise.utils.JsonReader;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.automationexercise.utils.UserDataGenerator.generateRandomUser;

public class SignUpTest extends BaseTest {

    private final User alreadyRegisteredUser = new JsonReader("valid-user").as(User.class);

    @Test(groups = {TestGroups.SMOKE, TestGroups.REGRESSION, TestGroups.POSITIVE}, description = "Verify that a new user can sign up successfully")
    @Severity(SeverityLevel.BLOCKER)
    public void verifySuccessfulSignUp()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName(validUser.getUsername())
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser)
                .clickCreateAccount()
                .verifyPageUrl(AccountCreated.EXPECTED_URL)
                .clickContinue()
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .verifyUserLabel(validUser.getUsername());
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that signing up with an already registered email address displays an error message")
    @Severity(SeverityLevel.CRITICAL)
    public void verifySignUpWithRegisteredEmailAddress()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(alreadyRegisteredUser.getEmail())
                .enterSignUpName(alreadyRegisteredUser.getUsername())
                .clickSignUpFailure()
                .verifySignUpErrorMessage(SignUpLogin.ERROR_EMAIL_ALREADY_EXISTS);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that trying to register without password triggers native HTML5 validation message")
    @Severity(SeverityLevel.CRITICAL)
    public void verifySignUpWithMissingPassword()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        validUser.setPassword(null); // Clear password to trigger validation

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName(validUser.getUsername())
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser)
                .clickCreateAccountFailure()
                .verifyPasswordValidationMessage(SignUp.ERROR_REQUIRED_FIELD);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that trying to register without Address triggers native HTML5 validation message")
    @Severity(SeverityLevel.NORMAL)
    public void verifySignUpWithMissingAddress()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        validUser.setAddress1(null); // Clear Address 1 to trigger validation

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName(validUser.getUsername())
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser)
                .clickCreateAccountFailure()
                .verifyAddress1ValidationMessage(SignUp.ERROR_REQUIRED_FIELD);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that trying to register without Mobile Number triggers native HTML5 validation message")
    @Severity(SeverityLevel.NORMAL)
    public void verifySignUpWithMissingMobileNumber()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        validUser.setMobileNumber(null); // Clear Mobile Number to trigger validation

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName(validUser.getUsername())
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser)
                .clickCreateAccountFailure()
                .verifyMobileNumberValidationMessage(SignUp.ERROR_REQUIRED_FIELD);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that trying to register without First Name triggers native HTML5 validation message")
    @Severity(SeverityLevel.NORMAL)
    public void verifySignUpWithMissingFirstName()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        validUser.setFirstName(null); // Clear First Name

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName(validUser.getUsername())
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser)
                .clickCreateAccountFailure()
                .verifyFirstNameValidationMessage(SignUp.ERROR_REQUIRED_FIELD);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that trying to register without Last Name triggers native HTML5 validation message")
    @Severity(SeverityLevel.NORMAL)
    public void verifySignUpWithMissingLastName()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        validUser.setLastName(null); // Clear Last Name

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName(validUser.getUsername())
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser)
                .clickCreateAccountFailure()
                .verifyLastNameValidationMessage(SignUp.ERROR_REQUIRED_FIELD);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that trying to register without State triggers native HTML5 validation message")
    @Severity(SeverityLevel.NORMAL)
    public void verifySignUpWithMissingState()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        validUser.setState(null); // Clear State

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName(validUser.getUsername())
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser)
                .clickCreateAccountFailure()
                .verifyStateValidationMessage(SignUp.ERROR_REQUIRED_FIELD);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that trying to register without City triggers native HTML5 validation message")
    @Severity(SeverityLevel.NORMAL)
    public void verifySignUpWithMissingCity()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        validUser.setCity(null); // Clear City

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName(validUser.getUsername())
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser)
                .clickCreateAccountFailure()
                .verifyCityValidationMessage(SignUp.ERROR_REQUIRED_FIELD);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that trying to register without Zipcode triggers native HTML5 validation message")
    @Severity(SeverityLevel.NORMAL)
    public void verifySignUpWithMissingZipcode()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        validUser.setZipcode(null); // Clear Zipcode

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName(validUser.getUsername())
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser)
                .clickCreateAccountFailure()
                .verifyZipcodeValidationMessage(SignUp.ERROR_REQUIRED_FIELD);
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.NEGATIVE}, description = "Verify that trying to register without Name triggers native HTML5 validation message")
    @Severity(SeverityLevel.CRITICAL)
    public void verifySignUpWithMissingName()
    {
        WebDriver driver = WebDriverFactory.getDriver();
        User validUser = generateRandomUser();
        validUser.setUsername(""); // Set to empty string to clear the prefilled name

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterSignUpEmail(validUser.getEmail())
                .enterSignUpName("TempName") // Fill name for pre-signup page
                .clickSignUpSuccess()
                .verifyPageUrl(SignUp.EXPECTED_URL)
                .fillRegistrationForm(validUser) // clears the name field inside SignUp page
                .clickCreateAccountFailure()
                .verifyNameValidationMessage(SignUp.ERROR_REQUIRED_FIELD);
    }
}
