package com.automationexercise.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import com.automationexercise.utils.LogsManager;
import com.automationexercise.models.Product;

public class ProductDetails extends BasePage {

    // ==========================================
    // Locators
    // ==========================================

    // Product Details
    private final By productName = By.cssSelector(".product-information > h2");
    private final By productCategory = By.xpath("//div[contains(@class, 'product-information')]/p[contains(., 'Category:')]");
    private final By productPrice = By.cssSelector(".product-information > span > span");
    private final By quantityInput = By.id("quantity");
    private final By addToCartButton = By.cssSelector(".product-information button.cart");
    private final By productAvailability = By.xpath("//div[contains(@class, 'product-information')]/p[b[normalize-space()='Availability:']]");
    private final By productCondition = By.xpath("//div[contains(@class, 'product-information')]/p[b[normalize-space()='Condition:']]");
    private final By productBrand = By.xpath("//div[contains(@class, 'product-information')]/p[b[normalize-space()='Brand:']]");

    // Cart Modal
    private final By cartModal = By.id("cartModal");
    private final By continueShoppingButton = By.cssSelector("#cartModal .close-modal");
    private final By viewCartLink = By.cssSelector("#cartModal a[href='/view_cart']");

    // Reviews
    private final By reviewNameInput = By.id("name");
    private final By reviewEmailInput = By.id("email");
    private final By reviewTextArea = By.id("review");
    private final By submitReviewButton = By.id("button-review");
    private final By reviewSuccessAlert = By.cssSelector("#review-form .alert-success");

    // ==========================================
    // Constructor
    // ==========================================
    public ProductDetails(WebDriver driver) {
        super(driver);
    }


    // ==========================================
    // Getters / Query Methods
    // ==========================================

    @Step("Get product name from UI")
    public String getProductName() {
        return elementActions.getText(productName);
    }

    @Step("Get product category from UI")
    public String getProductCategory() {
        String categoryText = elementActions.getText(productCategory);
        return categoryText.replace("Category:", "").trim();
    }

    @Step("Get product price from UI")
    public Integer getProductPrice() {
        String priceText = elementActions.getText(productPrice);
        return Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
    }

    @Step("Get product currency from UI")
    public String getProductCurrency() {
        String priceText = elementActions.getText(productPrice);
        return priceText.split("\\.")[0].trim();
    }

    @Step("Get product availability from UI")
    public String getProductAvailability() {
        String availabilityText = elementActions.getText(productAvailability);
        return availabilityText.replace("Availability:", "").trim();
    }

    @Step("Get product condition from UI")
    public String getProductCondition() {
        String conditionText = elementActions.getText(productCondition);
        return conditionText.replace("Condition:", "").trim();
    }

    @Step("Get product brand from UI")
    public String getProductBrand() {
        String brandText = elementActions.getText(productBrand);
        return brandText.replace("Brand:", "").trim();
    }

    // ==========================================
    // Actions
    // ==========================================

    @Step("Set product quantity to: {quantity}")
    public ProductDetails setQuantity(Integer quantity) {
        elementActions.sendKeys(quantityInput, quantity.toString());
        return this;
    }

    @Step("Click 'Add to Cart' button")
    public ProductDetails clickAddToCart() {
        elementActions.click(addToCartButton);
        return this;
    }

    @Step("Click 'Continue Shopping' on the success modal")
    public ProductDetails clickContinueShopping() {
        elementActions.click(continueShoppingButton);
        return this;
    }

    @Step("Click 'View Cart' on the success modal")
    public Cart clickViewCart() {
        elementActions.click(viewCartLink);
        return new Cart(driver);
    }

    @Step("Submit product review by: {name}")
    public ProductDetails submitReview(String name, String email, String review) {
        elementActions.sendKeys(reviewNameInput, name)
                      .sendKeys(reviewEmailInput, email)
                      .sendKeys(reviewTextArea, review)
                      .click(submitReviewButton);
        return this;
    }

    // ==========================================
    // Verifications / Assertions
    // ==========================================

    @Step("Verify that product details match expected DTO")
    public ProductDetails verifyProductDetails(Product expectedProduct) {
        LogsManager.info("Verifying details of product: {}", expectedProduct.getName());
        Assert.assertEquals(getProductName(), expectedProduct.getName(), "Product name mismatch!");
        Assert.assertEquals(getProductCategory(), expectedProduct.getCategory(), "Product category mismatch!");
        Assert.assertEquals(getProductPrice(), expectedProduct.getPrice(), "Product price mismatch!");
        Assert.assertEquals(getProductCurrency(), expectedProduct.getCurrency(), "Product currency mismatch!");
        
        String expectedAvailabilityText = expectedProduct.getAvailability() ? "In Stock" : "Out of Stock";
        Assert.assertEquals(getProductAvailability(), expectedAvailabilityText, "Product availability status mismatch!");

        Assert.assertEquals(getProductCondition(), expectedProduct.getCondition(), "Product condition mismatch!");
        Assert.assertEquals(getProductBrand(), expectedProduct.getBrand(), "Product brand mismatch!");
        return this;
    }

    @Step("Verify that the review success message is displayed")
    public ProductDetails verifyReviewSuccessMessageDisplayed() {
        LogsManager.info("Verifying review success alert display status");
        Assert.assertTrue(elementActions.isDisplayed(reviewSuccessAlert), "Review success message was not displayed!");
        return this;
    }

    @Override
    @Step("Verify that the Product Details page URL contains: {expectedUrlPart}")
    public ProductDetails verifyPageUrl(String expectedUrlPart) {
        elementActions.waitForUrlToContain(expectedUrlPart);
        String actualUrl = elementActions.getCurrentUrl();
        Assert.assertTrue(actualUrl.contains(expectedUrlPart), 
            String.format("Page URL '%s' does not contain expected part '%s'!", actualUrl, expectedUrlPart));
        return this;
    }
}
