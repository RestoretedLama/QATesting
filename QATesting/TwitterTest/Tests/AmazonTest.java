import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AmazonTest {

    private static WebDriver driver;
    private static AmazonTestUtils utils;

    @BeforeAll
    public static void setup() {
        System.out.println("Amazon Advanced Test");
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        
        driver = new ChromeDriver(options);
        utils = new AmazonTestUtils(driver);
    }

    @BeforeEach
    public void beforeEach() {
        utils.navigateToHomePage();
    }

    @ParameterizedTest
    @ValueSource(strings = {"laptop", "phone", "book", "shoes", "headphones"})
    @DisplayName("Product search test with different terms")
    public void testProductSearchWithDifferentTerms(String searchTerm) {
        System.out.println("\n'" + searchTerm + "' search test");
        System.out.println("----------------------------------------");
        
        utils.searchProduct(searchTerm);
        
        List<WebElement> searchResults = utils.getSearchResults();
        assertFalse(searchResults.isEmpty(), searchTerm + " search results should be visible");
        
        String pageTitle = utils.getPageTitle();
        assertTrue(pageTitle.toLowerCase().contains(searchTerm.toLowerCase()) || 
                  pageTitle.contains("Amazon"), 
                  "Page title should contain search term or Amazon");
        
        System.out.println("'" + searchTerm + "' search test successful - " + searchResults.size() + " results");
    }

    @ParameterizedTest
    @CsvSource({
        "Electronics, true",
        "Books, true", 
        "Fashion, true",
        "Sports, true",
        "Home and Living, true"
    })
    @DisplayName("Category navigation test")
    public void testCategoryNavigation(String categoryName, boolean expectedResult) {
        System.out.println("\n'" + categoryName + "' category test");
        System.out.println("----------------------------------------");
        
        utils.openCategoryMenu();
        
        boolean result = utils.navigateToCategory(categoryName);
        assertEquals(expectedResult, result, categoryName + " category navigation should be " + 
                    (expectedResult ? "successful" : "unsuccessful"));
        
        if (result) {
            String pageTitle = utils.getPageTitle();
            assertTrue(pageTitle.contains(categoryName) || pageTitle.contains("Amazon"), 
                      "Category page should load correctly");
            System.out.println("'" + categoryName + "' category test successful");
        } else {
            System.out.println("'" + categoryName + "' category test failed");
        }
    }

    @Test
    @DisplayName("Product detail page information test")
    public void testProductDetailInformation() {
        System.out.println("\nProduct detail page information test");
        System.out.println("----------------------------------------");
        
        utils.searchProduct("laptop");
        utils.clickFirstProduct();

        String productTitle = utils.getProductTitle();
        assertNotNull(productTitle, "Product title should not be null");
        assertFalse(productTitle.trim().isEmpty(), "Product title should not be empty");

        String productPrice = utils.getProductPrice();
        assertNotNull(productPrice, "Product price should not be null");

        String currentUrl = utils.getCurrentUrl();
        assertTrue(currentUrl.contains("amazon.com.tr"), "URL should contain Amazon Turkey");
        
        System.out.println("Product: " + productTitle);
        System.out.println("Price: " + productPrice);
        System.out.println("URL: " + currentUrl);
        System.out.println("Product detail information test successful");
    }

    @Test
    @DisplayName("Go to cart and check items test")
    public void testGoToCartAndCheckItems() {
        System.out.println("\nTest: Go to cart and check items test");
        System.out.println("----------------------------------------");
        
        utils.searchProduct("pen");
        utils.clickFirstProduct();
        utils.addToCartAdvanced();
        
        boolean cartOpened = utils.goToCart();

        assertTrue(cartOpened, "Cart page should open");
        
        List<WebElement> cartItems = utils.getCartItems();
        System.out.println("Items in cart: " + cartItems.size());
        
        if (!cartItems.isEmpty()) {
            System.out.println("Go to cart and check items test successful");
        } else {
            System.out.println("Cart appears to be empty????");
        }
    }

    @Test
    @DisplayName("Filtering process test")
    public void testFilteringProcess() {
        System.out.println("\nTest: Filtering process test");
        System.out.println("----------------------------------------");
        
        utils.searchProduct("phone");
        
        List<WebElement> filterOptions = utils.getFilterOptions();
        if (!filterOptions.isEmpty()) {
            System.out.println("filter count: " + filterOptions.size());
            
            boolean filterApplied = utils.applyFilter(0);
            assertTrue(filterApplied, "First filter should be applicable");

            List<WebElement> filteredResults = utils.getSearchResults();
            assertFalse(filteredResults.isEmpty(), "Filtered results should be visible");
            
            System.out.println("Filtering process test successful - " + filteredResults.size() + " filtered results");
        } else {
            System.out.println("Filtering options not found");
        }
    }

    @ParameterizedTest
    @CsvSource({
        "375, 667, iPhone",
        "768, 1024, iPad", 
        "1920, 1080, Desktop"
    })
    @DisplayName("Responsive design test")
    public void testResponsiveDesign(int width, int height, String device) {
        System.out.println("\nParameterized Test: " + device + " responsive design test");
        System.out.println("----------------------------------------");
        
        utils.setWindowSize(width, height);
        
        assertTrue(utils.isElementVisible(By.id("nav-logo-sprites")), 
                  "Amazon logo should be visible for " + device);
        assertTrue(utils.isElementVisible(By.id("nav-search")), 
                  "Search box should be visible for " + device);
        
        if (width <= 768) {
            assertTrue(utils.isElementVisible(By.id("nav-hamburger-menu")), 
                      "Hamburger menu should be visible for " + device);
            System.out.println("Mobile menu visible for " + device);
        }
        
        utils.maximizeWindow();
        System.out.println( device + " responsive design test successful");
    }

    @Test
    @DisplayName("Performance test")
    public void testPerformance() {
        System.out.println("\ntest: Performance test");
        System.out.println("----------------------------------------");
        
        long loadTime = utils.measurePageLoadTime();
        
        assertTrue(loadTime < 10000, "Page should load in less than 10 seconds. Time: " + loadTime + "ms");
        
        System.out.println("Home page load time: " + loadTime + "ms");
        System.out.println("Performance test successful oh ");
    }

    @Test
    @DisplayName("Multiple tabs operations test")
    public void testMultipleTabs() {
        System.out.println("\nest: Multiple tabs operations test");
        System.out.println("----------------------------------------");
        
        String originalUrl = utils.getCurrentUrl();
        
        utils.openNewTab("https://www.amazon.com.tr/");
        utils.switchToTab(1);
        
        utils.waitForPageLoad();
        assertTrue(utils.getPageTitle().contains("Amazon"), "Amazon page should load in new tab");
        
        utils.switchToTab(0);
        assertEquals(originalUrl, utils.getCurrentUrl(), "Original tab should have correct URL");
        
        utils.closeCurrentTab();
        utils.switchToTab(0);
        
        System.out.println("multiple tabs operations test successful");
    }

    @Test
    @DisplayName("Scroll operations test")
    public void testScrolling() {
        System.out.println("\nTest: Scroll operations test");
        System.out.println("----------------------------------------");

        utils.scrollToBottom();
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long scrollPosition = (Long) js.executeScript("return window.pageYOffset");
        
        assertTrue(scrollPosition > 0, "Page should be scrolled");
        
        js.executeScript("window.scrollTo(0, 0)");
        
        Long newScrollPosition = (Long) js.executeScript("return window.pageYOffset");
        assertEquals(0, newScrollPosition, "Page should return to top");
        
        System.out.println("Scroll position: " + scrollPosition + "px");
        System.out.println("Scroll operations test successful");
    }

    @Test
    @DisplayName("Element interactions test ")
    public void testElementInteractions() {
        System.out.println("\ntest Element interactions test");
        System.out.println("----------------------------------------");
        
        assertTrue(utils.isElementClickable(By.id("twotabsearchtextbox")), 
                  "Search box should be clickable");
        
        assertTrue(utils.isElementClickable(By.id("nav-search-submit-button")), 
                  "Search button should be clickable");
        
        assertTrue(utils.isElementVisible(By.id("nav-cart")), 
                  "Cart icon should be visible");
        
        System.out.println("Element interactions test successful");
    }

    @Test
    @DisplayName("URL validation test")
    public void testUrlValidation() {
        System.out.println("\nTest: URL validation test");
        System.out.println("----------------------------------------");
        
        String currentUrl = utils.getCurrentUrl();
        
        assertTrue(currentUrl.contains("amazon.com.tr"), "URL should contain Amazon Turkey");
        assertTrue(currentUrl.startsWith("https://"), "URL should use HTTPS protocol");
        
        System.out.println("URL: " + currentUrl);
        System.out.println("URL validation test successful");
    }

    @Test
    @DisplayName("Page title validation test")
    public void testPageTitleValidation() {
        System.out.println("\nTest: Page title validation test");
        System.out.println("----------------------------------------");
        
        String pageTitle = utils.getPageTitle();
        
        assertNotNull(pageTitle, "Page title should not be null");
        assertFalse(pageTitle.trim().isEmpty(), "Page title should not be empty");
        assertTrue(pageTitle.contains("Amazon") || pageTitle.contains("amazon"), 
                  "Page title should contain Amazon");
        
        System.out.println("Page title: " + pageTitle);
        System.out.println("Page title validation test successful");
    }

    @Test
    @DisplayName("Login process test")
    public void testLoginProcess() {
        System.out.println("\ntest: Login process test");
        System.out.println("----------------------------------------");
        
        utils.navigateToLoginPage();
        
        utils.enterEmail("your-email@example.com");
        utils.clickContinueButton();
        
        System.out.println("Waiting for robot check!!!!!!!!!");
        
        utils.waitForSeconds(30);
        
        utils.enterPassword("your-password");
        utils.clickSignInButton();
        
        boolean isLoggedIn = utils.isUserLoggedIn();
        assertTrue(isLoggedIn, "User should be able to login successfully");
        
        System.out.println("Login process test successful");
    }

    @Test
    @DisplayName("Add to cart process test with new method")
    public void testAddToCartProcessWithNewMethod() {
        System.out.println("\nTest: Add to cart process test with new method");
        System.out.println("----------------------------------------");
        
        assertTrue(utils.addFirstProductToCart("book"));
        
        System.out.println("Add to cart process test with new method successful");
    }

    @AfterAll
    public static void teardown() {
        System.out.println("\nADVANCED tests completing...");
        System.out.println("==============================================");
        
        if (utils != null) {
            utils.closeBrowser();
        }
        
        System.out.println("All advanced tests completed! ehehe");
        System.out.println("==============================================");
        
        System.out.println("Program closing...");
        System.exit(0);
    }

    public boolean addFirstProductToCart(String searchTerm) {
        try {
            driver.get("https://www.amazon.com.tr/");
            utils.waitForPageLoad();
            utils.handleCookieBannerAndPopups();
            utils.testDelay();

            // Arama
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.clear();
            searchBox.sendKeys(searchTerm);
            driver.findElement(By.id("nav-search-submit-button")).click();
            utils.wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-component-type='s-search-result']")));
            utils.testDelay();

            // Gerçek ürün detayına giden ilk linki bul
            List<WebElement> results = driver.findElements(By.cssSelector("[data-component-type='s-search-result']"));
            WebElement firstProductLink = null;
            String productText = "";
            for (WebElement result : results) {
                // Önce <h2> altındaki <a> ile dene
                try {
                    WebElement h2 = result.findElement(By.tagName("h2"));
                    WebElement link = h2.findElement(By.tagName("a"));
                    String href = link.getAttribute("href");
                    if (href != null && href.contains("/dp/") && link.isDisplayed() && link.isEnabled()) {
                        firstProductLink = link;
                        productText = link.getText();
                        break;
                    }
                } catch (Exception ignore) {}
                // Olmazsa kutudaki tüm <a> etiketlerini dene
                if (firstProductLink == null) {
                    List<WebElement> links = result.findElements(By.tagName("a"));
                    for (WebElement link : links) {
                        String href = link.getAttribute("href");
                        if (href != null && href.contains("/dp/") && link.isDisplayed() && link.isEnabled()) {
                            firstProductLink = link;
                            productText = link.getText();
                            break;
                        }
                    }
                }
                if (firstProductLink != null) break;
            }
            if (firstProductLink == null) {
                System.out.println("Hiçbir gerçek ürün linki bulunamadı!");
                return false;
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstProductLink);
            utils.testDelay();
            System.out.println("Tıklanan ürün: " + productText);
            firstProductLink.click();
            utils.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("productTitle")));
            utils.testDelay();

            // Sepete ekle butonunu bul ve tıkla
            WebElement addToCartBtn = null;
            String[] selectors = {
                "#add-to-cart-button",
                "input[name='submit.add-to-cart']",
                "button[name='submit.add-to-cart']"
            };
            for (String selector : selectors) {
                try {
                    addToCartBtn = driver.findElement(By.cssSelector(selector));
                    if (addToCartBtn.isDisplayed() && addToCartBtn.isEnabled()) {
                        break;
                    }
                } catch (Exception ignore) {}
            }
            if (addToCartBtn == null) {
                System.out.println("Sepete ekle butonu yok!");
                return false;
            }
            addToCartBtn.click();
            System.out.println("Sepete ekle tıklandı");
            boolean confirmed = utils.waitForAddToCartConfirmation();
            if (confirmed) {
                System.out.println("Ürün sepete eklendi!");
            } else {
                System.out.println("Sepete ekleme onayı alınamadı!");
            }
            return confirmed;
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            return false;
        }
    }
} 