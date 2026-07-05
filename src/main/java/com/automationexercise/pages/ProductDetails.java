package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;

public class ProductDetails extends BasePage {

    public ProductDetails(WebDriver driver) {
        super(driver);
    }

    @Override
    public ProductDetails verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }
}
