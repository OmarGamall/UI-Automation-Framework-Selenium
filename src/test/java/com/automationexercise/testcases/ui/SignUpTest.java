package com.automationexercise.testcases.ui;

import com.automationexercise.drivers.WebDriverFactory;
import com.automationexercise.models.User;
import com.automationexercise.pages.AccountCreated;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.SignUp;
import com.automationexercise.pages.SignUpLogin;
import com.automationexercise.utils.JsonReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import static com.automationexercise.utils.UserDataGenerator.generateRandomUser;

public class SignUpTest extends BaseTest {

    private final User alreadyRegisteredUser = new JsonReader("valid-user").as(User.class);

    @Test(description = "Verify that a new user can sign up successfully")
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

    @Test(description = "Verify that signing up with an already registered email address displays an error message")
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
}
