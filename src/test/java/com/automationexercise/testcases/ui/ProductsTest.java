package com.automationexercise.testcases.ui;

import com.automationexercise.drivers.WebDriverFactory;
import com.automationexercise.models.Product;
import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.Products;
import com.automationexercise.pages.Cart;
import com.automationexercise.pages.ProductDetails;
import com.automationexercise.utils.JsonReader;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class ProductsTest extends BaseTest {



    @Test(description = "Verify successful product search using product name")
    @Severity(SeverityLevel.CRITICAL)
    public void verifySearchProductByProductName() {
        // Arrange Test Data
        Product product = new JsonReader("products").getJsonData("$.products[0]", Product.class);

        WebDriver driver = WebDriverFactory.getDriver();

        new HomePage(driver)
                .verifyPageUrl(HomePage.EXPECTED_URL)
                .getNavigationBar()
                .navigateToProducts()
                .verifyPageUrl(Products.EXPECTED_URL)
                .searchProduct(product.getName())
                .verifySearchTitle("SEARCHED PRODUCTS")
                .verifyProductDisplayed(product.getName())
                .verifyProductPrice(product.getName(), product.getPrice());
    }

    @Test(description = "Verify products catalog, search, and add to cart functionality")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyProductsCatalogSearchAndCartModal() {
        WebDriver driver = WebDriverFactory.getDriver();
        
        new HomePage(driver)
                .getNavigationBar()
                .navigateToProducts()
                .verifyPageUrl(Products.EXPECTED_URL)
                .verifyProductDisplayed("Blue Top")
                .verifyProductPrice("Blue Top", 500.0)
                .searchProduct("Blue Top")
                .verifySearchTitle("SEARCHED PRODUCTS")
                .verifyProductDisplayed("Blue Top")
                .addProductToCart("Blue Top")
                .verifyCartModalDisplayed()
                .clickContinueShopping()
                .verifyCartModalHidden();
    }

    @Test(description = "Verify product details navigation and cart transition")
    @Severity(SeverityLevel.NORMAL)
    public void verifyProductDetailsAndViewCartNavigation() {
        WebDriver driver = WebDriverFactory.getDriver();

        Products productsPage = new HomePage(driver)
                .getNavigationBar()
                .navigateToProducts()
                .verifyPageUrl(Products.EXPECTED_URL);

        // 1. Click View Product and verify navigation
        ProductDetails detailsPage = productsPage.clickViewProduct("Blue Top");
        detailsPage.verifyPageUrl(Products.BASE_URL + "product_details/1");

        // 2. Go back to products, add to cart and transition to cart
        driver.navigate().back();
        
        Cart cartPage = productsPage
                .addProductToCart("Blue Top")
                .clickViewCart()
                .verifyPageUrl(Cart.EXPECTED_URL);
    }
}
