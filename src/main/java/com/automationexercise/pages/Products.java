package com.automationexercise.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import com.automationexercise.utils.LogsManager;

public class Products extends BasePage {
    
    // Expected URL
    public static final String EXPECTED_URL = BASE_URL + "products";

    // ==========================================
    // Constructor
    // ==========================================
    public Products(WebDriver driver) {
        super(driver);
    }


    // ==========================================
    // Locators
    // ==========================================

    // Static Locators
    private final By searchBar = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By scrollUpButton = By.id("scrollUp");
    private final By continueShoppingButton = By.cssSelector("#cartModal button.close-modal");
    private final By viewCartLink = By.cssSelector("#cartModal a[href*='view_cart']");
    private final By searchTitle = By.cssSelector(".features_items > h2.title");
    private final By cartModal = By.id("cartModal");

    // Dynamic Locators

    // 1. The Global Root Anchor for the entire product component block
    private String productWrapperRoot(String productName) {
        return String.format("//div[contains(@class, 'product-image-wrapper')][.//p[normalize-space()='%s']]", productName);
    }

    // 2. The static product name display (Safe for verification)
    private By productName(String productName) {
        return By.xpath(productWrapperRoot(productName) + "//div[contains(@class, 'productinfo')]/p");
    }

    // 3. The static product price display
    private By productPrice(String productName) {
        return By.xpath(productWrapperRoot(productName) + "//div[contains(@class, 'productinfo')]/h2");
    }

    // 4. View Product Link
    private By viewProduct(String productName) {
        return By.xpath(productWrapperRoot(productName) + "//div[contains(@class, 'choose')]//a[contains(@href, 'product_details')]");
    }

    // 5. The Hover-dependent Add to Cart Button
    private By addToCartButton(String productName) {
        return By.xpath(productWrapperRoot(productName) + "//div[contains(@class, 'product-overlay')]//a[contains(@class, 'add-to-cart')]");
    }

    // ==========================================
    // Actions
    // ==========================================

    @Step("Search for product: {productName}")
    public Products searchProduct(String productName){
        elementActions.sendKeys(searchBar, productName).click(searchButton);
        return this;
    }

    @Step("Add product to cart: {productName}")
    public Products addProductToCart(String productName) {
        elementActions.hover(productName(productName))
                      .click(addToCartButton(productName));
        return this;
    }

    @Step("Click 'Continue Shopping' on the success modal")
    public Products clickContinueShopping() {
        elementActions.click(continueShoppingButton);
        return this;
    }

    @Step("Click 'View Cart' on the success modal")
    public Cart clickViewCart() {
        elementActions.click(viewCartLink);
        return new Cart(driver);
    }

    @Step("Click 'View Product' for product: {productName}")
    public ProductDetails clickViewProduct(String productName) {
        elementActions.click(viewProduct(productName));
        return new ProductDetails(driver);
    }

    // ==========================================
    // Verification
    // ==========================================
    @Override
    public Products verifyPageUrl(String expectedUrl) {
        super.verifyPageUrl(expectedUrl);
        return this;
    }

    @Step("Verify that search results title matches: {expectedTitle}")
    public Products verifySearchTitle(String expectedTitle) {
        LogsManager.info("Verifying search results title: {}", expectedTitle);
        String actualTitle = elementActions.getText(searchTitle);
        Assert.assertEquals(actualTitle, expectedTitle, "Search results title does not match!");
        return this;
    }

    @Step("Verify that product '{productName}' is displayed")
    public Products verifyProductDisplayed(String productName) {
        LogsManager.info("Verifying that product is displayed: {}", productName);
        Assert.assertTrue(elementActions.isDisplayed(productName(productName)), "Product was not displayed!");
        return this;
    }

    @Step("Get price for product: {productName}")
    public Double getProductPrice(String productName) {
        String priceText = elementActions.getText(productPrice(productName));
        return Double.parseDouble(priceText.replaceAll("[^0-9]", ""));
    }

    @Step("Verify that product '{productName}' has price '{expectedPrice}'")
    public Products verifyProductPrice(String productName, Double expectedPrice) {
        LogsManager.info("Verifying price for product '{}': {}", productName, expectedPrice);
        Double actualPrice = getProductPrice(productName);
        Assert.assertEquals(actualPrice, expectedPrice, "Product price does not match!");
        return this;
    }

    @Step("Verify that the Cart success modal is displayed")
    public Products verifyCartModalDisplayed() {
        LogsManager.info("Verifying that Cart success modal is displayed");
        Assert.assertTrue(elementActions.isDisplayed(cartModal), "Cart success modal is not displayed!");
        return this;
    }

    @Step("Verify that the Cart success modal is hidden/not displayed")
    public Products verifyCartModalHidden() {
        LogsManager.info("Verifying that Cart success modal is hidden/not displayed");
        Assert.assertTrue(elementActions.isNotDisplayed(cartModal), "Cart success modal is still displayed!");
        return this;
    }
}
