package com.automationexercise.facades;

import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.Products;
import org.openqa.selenium.WebDriver;

/**
 * Facade class representing high-level products flows for the automation exercise application.
 */
public class ProductFacade {
    private final WebDriver driver;

    public ProductFacade(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Performs a full UI flow to search for a product by its full name starting from the HomePage.
     *
     * @param productName the full name of the product to search for
     * @return the Products page object instance containing the search results
     */
    public Products searchForProductByFullName(String productName) {
        return new HomePage(driver)
                .getNavigationBar()
                .navigateToProducts()
                .verifyPageUrl(Products.EXPECTED_URL)
                .searchProduct(productName)
                .verifySearchTitle("SEARCHED PRODUCTS")
                .verifyProductDisplayed(productName);
    }
}
