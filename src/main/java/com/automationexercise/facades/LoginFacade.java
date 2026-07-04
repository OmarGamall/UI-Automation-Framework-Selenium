package com.automationexercise.facades;

import com.automationexercise.models.User;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.SignUpLogin;
import org.openqa.selenium.WebDriver;

/**
 * Facade class representing high-level login flows for the automation exercise application.
 */
public class LoginFacade {
    private final WebDriver driver;

    public LoginFacade(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Performs a full UI login flow and returns the HomePage.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return the HomePage page object instance
     */
    public HomePage login(String email, String password) {
        return new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToSignUpLogin()
                .verifyPageUrl(SignUpLogin.EXPECTED_URL)
                .enterLoginEmail(email)
                .enterLoginPassword(password)
                .clickLogInSuccess();
    }

    /**
     * Performs a full UI login flow for the given User model.
     *
     * @param user the user model object
     * @return the HomePage page object instance
     */
    public HomePage login(User user) {
        return login(user.getEmail(), user.getPassword());
    }
}
