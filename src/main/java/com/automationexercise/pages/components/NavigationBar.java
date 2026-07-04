package com.automationexercise.pages.components;

import com.automationexercise.pages.*;
import com.automationexercise.utils.LogsManager;
import com.automationexercise.utils.actions.ElementActions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class NavigationBar {
    // ==========================================
    // Fields & Drivers
    // ==========================================
    private final WebDriver driver;
    private final ElementActions elementActions;

    // ==========================================
    // Constructor
    // ==========================================
    public NavigationBar(WebDriver driver) {
        this.driver = driver;
        this.elementActions = new ElementActions(driver);
    }

    // ==========================================
    // Locators
    // ==========================================
    private final By homeLink = By.cssSelector(".shop-menu a[href='/']");
    private final By productsLink = By.cssSelector(".shop-menu a[href='/products']");
    private final By cartLink = By.cssSelector(".shop-menu a[href='/view_cart']");
    private final By signUpLoginLink = By.cssSelector(".shop-menu a[href='/login']");
    private final By logOut = By.cssSelector(".shop-menu a[href='/logout']");
    private final By deleteAccount = By.cssSelector(".shop-menu a[href='/delete_account']");
    private final By videoTutorials = By.cssSelector(".shop-menu a[href*='youtube.com']");
    private final By contactUs = By.cssSelector(".shop-menu a[href='/contact_us']");
    
    // Dynamic "Logged in as {username}" Locators
    private final By loggedInText = By.xpath("//div[contains(@class, 'shop-menu')]//a[contains(., 'Logged in as')]");
    private final By loggedInUsername = By.xpath("//div[contains(@class, 'shop-menu')]//a[contains(., 'Logged in as')]/b");

    // ==========================================
    // Page Actions
    // ==========================================
    @Step("Navigate to Home")
    public HomePage navigateToHome() {
        elementActions.click(homeLink);
        return new HomePage(driver);
    }

    @Step("Navigate to Products")
    public Products navigateToProducts() {
        elementActions.click(productsLink);
        return new Products(driver);
    }

    @Step("Navigate to Cart")
    public Cart navigateToCart() {
        elementActions.click(cartLink);
        return new Cart(driver);
    }

    @Step("Navigate to Signup / Login")
    public SignUpLogin navigateToSignUpLogin() {
        elementActions.click(signUpLoginLink);
        return new SignUpLogin(driver);
    }

    @Step("Navigate to Logout")
    public SignUpLogin navigateToLogout() {
        elementActions.click(logOut);
        return new SignUpLogin(driver);
    }

    @Step("Navigate to Delete Account")
    public DeleteAccount navigateToDeleteAccount() {
        elementActions.click(deleteAccount);
        return new DeleteAccount(driver);
    }

    @Step("Navigate to Video Tutorials")
    public void navigateToVideoTutorials() {
        elementActions.click(videoTutorials);
    }

    @Step("Navigate to Contact Us")
    public ContactUs navigateToContactUs() {
        elementActions.click(contactUs);
        return new ContactUs(driver);
    }

    @Step("Check if logged in message is displayed")
    public boolean isLoggedInMessageDisplayed() {
        return elementActions.isDisplayed(loggedInText);
    }


    @Step("Get logged in username")
    public String getLoggedInUsername() {
        return elementActions.getText(loggedInUsername);
    }

    // ==========================================
    // Assertions
    // ==========================================
    @Step("Verify that the logged in message is displayed and the username matches")
    public NavigationBar verifyUserLabel(String userName)
    {
        String actualLoggedInMessage = getLoggedInUsername();
        LogsManager.info("Verifying that the logged in message is displayed and the username matches: {} " , actualLoggedInMessage);
        Assert.assertTrue(isLoggedInMessageDisplayed(), "Logged in message should be displayed");
        Assert.assertEquals(actualLoggedInMessage, userName, "Logged in username should match");
        return this;
    }

    @Step("Verify that the logged in user label is not displayed")
    public NavigationBar verifyUserLabelNotDisplayed() {
        Assert.assertTrue(elementActions.isNotDisplayed(loggedInText), "Logged in message should not be displayed");
        return this;
    }

    @Step("Verify that the logout button is not displayed")
    public NavigationBar verifyLogoutButtonNotDisplayed() {
        Assert.assertTrue(elementActions.isNotDisplayed(logOut), "Logout button should not be displayed");
        return this;
    }
}
