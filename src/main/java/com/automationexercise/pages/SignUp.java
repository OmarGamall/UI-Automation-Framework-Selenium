package com.automationexercise.pages;

import com.automationexercise.models.User;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class SignUp extends BasePage {
    
    // Expected URL
    public static final String EXPECTED_URL = BASE_URL + "signup";
    
    // Expected Constants
    public static final String ERROR_REQUIRED_FIELD = "Please fill out this field.";

    // ==========================================
    // Locators
    // ==========================================
    // Account Information
    private final By genderMrRadio = By.id("id_gender1");
    private final By genderMrsRadio = By.id("id_gender2");
    private final By nameInput = By.id("name");
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By daysSelect = By.id("days");
    private final By monthsSelect = By.id("months");
    private final By yearsSelect = By.id("years");
    private final By newsletterCheckbox = By.id("newsletter");
    private final By optionCheckbox = By.id("optin");

    // Address Information
    private final By firstNameInput = By.id("first_name");
    private final By lastNameInput = By.id("last_name");
    private final By companyInput = By.id("company");
    private final By address1Input = By.id("address1");
    private final By address2Input = By.id("address2");
    private final By countrySelect = By.id("country");
    private final By stateInput = By.id("state");
    private final By cityInput = By.id("city");
    private final By zipcodeInput = By.id("zipcode");
    private final By mobileNumberInput = By.id("mobile_number");
    private final By createAccountButton = By.cssSelector("button[data-qa='create-account']");

    // ==========================================
    // Constructor
    // ==========================================
    public SignUp(WebDriver driver) {
        super(driver);
    }


    // ==========================================
    // Page Actions
    // ==========================================
    @Step("Select Title: Mr.")
    public SignUp selectTitleMr() {
        elementActions.click(genderMrRadio);
        return this;
    }

    @Step("Select Title: Mrs.")
    public SignUp selectTitleMrs() {
        elementActions.click(genderMrsRadio);
        return this;
    }

    @Step("Select Title by option: {title}")
    public SignUp selectTitle(String title) {
        if ("Mr".equalsIgnoreCase(title) || "Mr.".equalsIgnoreCase(title)) {
            selectTitleMr();
        } else if ("Mrs".equalsIgnoreCase(title) || "Mrs.".equalsIgnoreCase(title)) {
            selectTitleMrs();
        }
        return this;
    }

    @Step("Enter Name: {name}")
    public SignUp enterName(String name) {
        elementActions.sendKeys(nameInput, name);
        return this;
    }

    @Step("Get disabled Email value")
    public String getEmail() {
        return driver.findElement(emailInput).getAttribute("value");
    }

    @Step("Enter Password: {password}")
    public SignUp enterPassword(String password) {
        elementActions.sendKeys(passwordInput, password);
        return this;
    }

    @Step("Select Date of Birth Day: {day}")
    public SignUp selectDay(String day) {
        elementActions.selectByValue(daysSelect, day);
        return this;
    }

    @Step("Select Date of Birth Month: {month}")
    public SignUp selectMonth(String month) {
        elementActions.selectByVisibleText(monthsSelect, month);
        return this;
    }

    @Step("Select Date of Birth Year: {year}")
    public SignUp selectYear(String year) {
        elementActions.selectByValue(yearsSelect, year);
        return this;
    }

    @Step("Set Newsletter Checkbox: {select}")
    public SignUp setNewsletterCheckbox(boolean select) {
        elementActions.setCheckboxState(newsletterCheckbox, select);
        return this;
    }

    @Step("Set Special Offers Checkbox: {select}")
    public SignUp setSpecialOffersCheckbox(boolean select) {
        elementActions.setCheckboxState(optionCheckbox, select);
        return this;
    }

    @Step("Enter First Name: {firstName}")
    public SignUp enterFirstName(String firstName) {
        elementActions.sendKeys(firstNameInput, firstName);
        return this;
    }

    @Step("Enter Last Name: {lastName}")
    public SignUp enterLastName(String lastName) {
        elementActions.sendKeys(lastNameInput, lastName);
        return this;
    }

    @Step("Enter Company: {company}")
    public SignUp enterCompany(String company) {
        elementActions.sendKeys(companyInput, company);
        return this;
    }

    @Step("Enter Address 1: {address1}")
    public SignUp enterAddress1(String address1) {
        elementActions.sendKeys(address1Input, address1);
        return this;
    }

    @Step("Enter Address 2: {address2}")
    public SignUp enterAddress2(String address2) {
        elementActions.sendKeys(address2Input, address2);
        return this;
    }

    @Step("Select Country: {country}")
    public SignUp selectCountry(String country) {
        elementActions.selectByVisibleText(countrySelect, country);
        return this;
    }

    @Step("Enter State: {state}")
    public SignUp enterState(String state) {
        elementActions.sendKeys(stateInput, state);
        return this;
    }

    @Step("Enter City: {city}")
    public SignUp enterCity(String city) {
        elementActions.sendKeys(cityInput, city);
        return this;
    }

    @Step("Enter Zipcode: {zipcode}")
    public SignUp enterZipcode(String zipcode) {
        elementActions.sendKeys(zipcodeInput, zipcode);
        return this;
    }

    @Step("Enter Mobile Number: {mobileNumber}")
    public SignUp enterMobileNumber(String mobileNumber) {
        elementActions.sendKeys(mobileNumberInput, mobileNumber);
        return this;
    }

    @Step("Click 'Create Account' button")
    public AccountCreated clickCreateAccount() {
        elementActions.click(createAccountButton);
        return new AccountCreated(driver);
    }

    @Step("Click 'Create Account' button expecting failure")
    public SignUp clickCreateAccountFailure() {
        elementActions.click(createAccountButton);
        return this;
    }

    @Step("Get password field native validation message")
    public String getPasswordValidationMessage() {
        return elementActions.getHtml5ValidationMessage(passwordInput);
    }

    @Step("Get Address 1 field native validation message")
    public String getAddress1ValidationMessage() {
        return elementActions.getHtml5ValidationMessage(address1Input);
    }

    @Step("Get Mobile Number field native validation message")
    public String getMobileNumberValidationMessage() {
        return elementActions.getHtml5ValidationMessage(mobileNumberInput);
    }

    @Step("Get Name field native validation message")
    public String getNameValidationMessage() {
        return elementActions.getHtml5ValidationMessage(nameInput);
    }

    @Step("Get First Name field native validation message")
    public String getFirstNameValidationMessage() {
        return elementActions.getHtml5ValidationMessage(firstNameInput);
    }

    @Step("Get Last Name field native validation message")
    public String getLastNameValidationMessage() {
        return elementActions.getHtml5ValidationMessage(lastNameInput);
    }

    @Step("Get State field native validation message")
    public String getStateValidationMessage() {
        return elementActions.getHtml5ValidationMessage(stateInput);
    }

    @Step("Get City field native validation message")
    public String getCityValidationMessage() {
        return elementActions.getHtml5ValidationMessage(cityInput);
    }

    @Step("Get Zipcode field native validation message")
    public String getZipcodeValidationMessage() {
        return elementActions.getHtml5ValidationMessage(zipcodeInput);
    }

    // ==========================================
    // Verifications
    // ==========================================
    @Step("Verify password validation message matches: {expectedMessage}")
    public SignUp verifyPasswordValidationMessage(String expectedMessage) {
        Assert.assertEquals(getPasswordValidationMessage(), expectedMessage, 
                "Password field validation message does not match!");
        return this;
    }

    @Step("Verify Address 1 validation message matches: {expectedMessage}")
    public SignUp verifyAddress1ValidationMessage(String expectedMessage) {
        Assert.assertEquals(getAddress1ValidationMessage(), expectedMessage, 
                "Address 1 field validation message does not match!");
        return this;
    }

    @Step("Verify Mobile Number validation message matches: {expectedMessage}")
    public SignUp verifyMobileNumberValidationMessage(String expectedMessage) {
        Assert.assertEquals(getMobileNumberValidationMessage(), expectedMessage, 
                "Mobile Number field validation message does not match!");
        return this;
    }

    @Step("Verify Name validation message matches: {expectedMessage}")
    public SignUp verifyNameValidationMessage(String expectedMessage) {
        Assert.assertEquals(getNameValidationMessage(), expectedMessage, 
                "Name field validation message does not match!");
        return this;
    }

    @Step("Verify First Name validation message matches: {expectedMessage}")
    public SignUp verifyFirstNameValidationMessage(String expectedMessage) {
        Assert.assertEquals(getFirstNameValidationMessage(), expectedMessage, 
                "First Name field validation message does not match!");
        return this;
    }

    @Step("Verify Last Name validation message matches: {expectedMessage}")
    public SignUp verifyLastNameValidationMessage(String expectedMessage) {
        Assert.assertEquals(getLastNameValidationMessage(), expectedMessage, 
                "Last Name field validation message does not match!");
        return this;
    }

    @Step("Verify State validation message matches: {expectedMessage}")
    public SignUp verifyStateValidationMessage(String expectedMessage) {
        Assert.assertEquals(getStateValidationMessage(), expectedMessage, 
                "State field validation message does not match!");
        return this;
    }

    @Step("Verify City validation message matches: {expectedMessage}")
    public SignUp verifyCityValidationMessage(String expectedMessage) {
        Assert.assertEquals(getCityValidationMessage(), expectedMessage, 
                "City field validation message does not match!");
        return this;
    }

    @Step("Verify Zipcode validation message matches: {expectedMessage}")
    public SignUp verifyZipcodeValidationMessage(String expectedMessage) {
        Assert.assertEquals(getZipcodeValidationMessage(), expectedMessage, 
                "Zipcode field validation message does not match!");
        return this;
    }

    @Step("Fill complete registration form for user: {user}")
    public SignUp fillRegistrationForm(User user) {
        if (user.getTitle() != null) {
            selectTitle(user.getTitle());
        }
        if (user.getUsername() != null) {
            enterName(user.getUsername());
        }
        if (user.getPassword() != null) {
            enterPassword(user.getPassword());
        }
        if (user.getBirthDay() != null) {
            selectDay(user.getBirthDay());
        }
        if (user.getBirthMonth() != null) {
            selectMonth(user.getBirthMonth());
        }
        if (user.getBirthYear() != null) {
            selectYear(user.getBirthYear());
        }
        setNewsletterCheckbox(user.isNewsletter());
        setSpecialOffersCheckbox(user.isOptin());

        // Address Details
        if (user.getFirstName() != null) {
            enterFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            enterLastName(user.getLastName());
        }
        if (user.getCompany() != null) {
            enterCompany(user.getCompany());
        }
        if (user.getAddress1() != null) {
            enterAddress1(user.getAddress1());
        }
        if (user.getAddress2() != null) {
            enterAddress2(user.getAddress2());
        }
        if (user.getCountry() != null) {
            selectCountry(user.getCountry());
        }
        if (user.getState() != null) {
            enterState(user.getState());
        }
        if (user.getCity() != null) {
            enterCity(user.getCity());
        }
        if (user.getZipcode() != null) {
            enterZipcode(user.getZipcode());
        }
        if (user.getMobileNumber() != null) {
            enterMobileNumber(user.getMobileNumber());
        }
        return this;
    }



    // ==========================================
    // Verification
    // ==========================================
    @Override
    public SignUp verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }

}
