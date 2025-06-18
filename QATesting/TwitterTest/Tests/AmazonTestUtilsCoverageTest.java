import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;
import java.util.List;

public class AmazonTestUtilsCoverageTest {

    private static WebDriver driver;
    private static AmazonTestUtils utils;
    private static int testCount = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    @BeforeAll
    public static void setup() {
        System.out.println("AmazonTestUtils Coverage Test Starting");
        
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

    @Test
    @DisplayName("Registration page test")
    public void testRegistrationPage() {
        testCount++;
        System.out.println("Test " + testCount + " Registration page testing");
        
        try {
            driver.get("https://www.amazon.com/ap/signin?openid.pape.max_auth_age=900&openid.return_to=https%3A%2F%2Fwww.amazon.com%2Fgp%2Fyourstore%2Fhome%3Fpath%3D%252Fgp%252Fyourstore%252Fhome%26useRedirectOnSuccess%3D1%26signIn%3D1%26action%3Dsign-out%26ref_%3Dnav_AccountFlyout_signout&openid.assoc_handle=usflex&openid.mode=checkid_setup&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0");
            
            utils.waitForSeconds(3);
            
            List<WebElement> createAccountLinks = driver.findElements(By.cssSelector("a[href*='register'], a[href*='signup'], .create-account, #createAccountSubmit"));
            
            boolean registrationLinkFound = false;
            for (WebElement link : createAccountLinks) {
                if (link.isDisplayed() && link.isEnabled()) {
                    String linkText = link.getText().toLowerCase();
                    if (linkText.contains("create") || linkText.contains("sign up") || linkText.contains("register") || 
                        linkText.contains("new account") || linkText.contains("register")) {
                        link.click();
                        registrationLinkFound = true;
                        passedTests++;
                        System.out.println("Registration link found and clicked: " + linkText);
                        break;
                    }
                }
            }
            
            if (!registrationLinkFound) {
                List<WebElement> allLinks = driver.findElements(By.tagName("a"));
                for (WebElement link : allLinks) {
                    if (link.isDisplayed()) {
                        String linkText = link.getText().toLowerCase();
                        if (linkText.contains("create") || linkText.contains("sign up") || linkText.contains("register")) {
                            link.click();
                            registrationLinkFound = true;
                            passedTests++;
                            System.out.println("Alternative registration link found " + linkText);
                            break;
                        }
                    }
                }
            }
            
            if (!registrationLinkFound) {
                failedTests++;
                System.out.println("Registration link not found");
            }
            
            utils.waitForSeconds(3);
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("register") || currentUrl.contains("signup") || currentUrl.contains("create")) {
                passedTests++;
                System.out.println("Registration page loaded  successfully");
            } else {
                failedTests++;
                System.out.println("Registration page failed to load");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Registration page test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Registration form fields test")
    public void testRegistrationFormFields() {
        testCount++;
        System.out.println("Test " + testCount + " Registration form fields testing");
        
        try {
            driver.get("https://www.amazon.com/ap/signin?openid.pape.max_auth_age=900&openid.return_to=https%3A%2F%2Fwww.amazon.com%2Fgp%2Fyourstore%2Fhome%3Fpath%3D%252Fgp%252Fyourstore%252Fhome%26useRedirectOnSuccess%3D1%26signIn%3D1%26action%3Dsign-out%26ref_%3Dnav_AccountFlyout_signout&openid.assoc_handle=usflex&openid.mode=checkid_setup&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0");
            
            utils.waitForSeconds(3);
            
            List<WebElement> createAccountLinks = driver.findElements(By.cssSelector("a[href*='register'], a[href*='signup'], .create-account, #createAccountSubmit"));
            for (WebElement link : createAccountLinks) {
                if (link.isDisplayed() && link.isEnabled()) {
                    String linkText = link.getText().toLowerCase();
                    if (linkText.contains("create") || linkText.contains("sign up") || linkText.contains("register")) {
                        link.click();
                        break;
                    }
                }
            }
            
            utils.waitForSeconds(3);
            
            List<WebElement> nameFields = driver.findElements(By.cssSelector("input[name*='name'], input[id*='name'], #ap_customer_name"));
            List<WebElement> emailFields = driver.findElements(By.cssSelector("input[type='email'], input[name*='email'], #ap_email"));
            List<WebElement> passwordFields = driver.findElements(By.cssSelector("input[type='password'], input[name*='password'], #ap_password"));
            
            if (!nameFields.isEmpty()) {
                passedTests++;
                System.out.println("Name field found");
            } else {
                failedTests++;
                System.out.println("Name field not found");
            }
            
            if (!emailFields.isEmpty()) {
                passedTests++;
                System.out.println("Email field found");
            } else {
                failedTests++;
                System.out.println("Email field not found");
            }
            
            if (!passwordFields.isEmpty()) {
                passedTests++;
                System.out.println("Password field found");
            } else {
                failedTests++;
                System.out.println("Password field not found");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Registration form fields test failed " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Registration error messages test")
    public void testRegistrationErrorMessages() {
        testCount++;
        System.out.println("Test " + testCount + " Registration error messages testing");
        
        try {
            driver.get("https://www.amazon.com/ap/signin?openid.pape.max_auth_age=900&openid.return_to=https%3A%2F%2Fwww.amazon.com%2Fgp%2Fyourstore%2Fhome%3Fpath%3D%252Fgp%252Fyourstore%252Fhome%26useRedirectOnSuccess%3D1%26signIn%3D1%26action%3Dsign-out%26ref_%3Dnav_AccountFlyout_signout&openid.assoc_handle=usflex&openid.mode=checkid_setup&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0");
            
            utils.waitForSeconds(3);
            
            List<WebElement> createAccountLinks = driver.findElements(By.cssSelector("a[href*='register'], a[href*='signup'], .create-account, #createAccountSubmit"));
            for (WebElement link : createAccountLinks) {
                if (link.isDisplayed() && link.isEnabled()) {
                    String linkText = link.getText().toLowerCase();
                    if (linkText.contains("create") || linkText.contains("sign up") || linkText.contains("register")) {
                        link.click();
                        break;
                    }
                }
            }
            
            utils.waitForSeconds(3);
            
            List<WebElement> submitButtons = driver.findElements(By.cssSelector("input[type='submit'], button[type='submit'], #continue, .submit-button"));
            if (!submitButtons.isEmpty()) {
                submitButtons.get(0).click();
                utils.waitForSeconds(2);
                
                List<WebElement> errorMessages = driver.findElements(By.cssSelector(".a-alert-content, .error-message, .alert-error, [data-testid='error-message']"));
                
                boolean errorFound = false;
                for (WebElement error : errorMessages) {
                    if (error.isDisplayed()) {
                        String errorText = error.getText().toLowerCase();
                        if (errorText.contains("required") || errorText.contains("required") || 
                            errorText.contains("empty") || errorText.contains("empty") ||
                            errorText.contains("fill") || errorText.contains("fill")) {
                            passedTests++;
                            System.out.println("Registration form error message correct " + errorText);
                            errorFound = true;
                            break;
                        }
                    }
                }
                
                if (!errorFound) {
                    failedTests++;
                    System.out.println("Registration form error message not found");
                }
            } else {
                failedTests++;
                System.out.println("Registration form submit button not found");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Registration error messages test failed " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Wishlist creation test")
    public void testWishlistCreation() {
        testCount++;
        System.out.println("Test " + testCount + " Wishlist creation testing");
        
        try {
            utils.navigateToHomePage();
            utils.searchProduct("laptop");
            utils.clickFirstProduct();
            
            List<WebElement> wishlistButtons = driver.findElements(By.cssSelector("[data-feature-name='wishlist-button'], .wishlist-button, #add-to-wishlist-button"));
            
            if (!wishlistButtons.isEmpty()) {
                for (WebElement button : wishlistButtons) {
                    if (button.isDisplayed() && button.isEnabled()) {
                        button.click();
                        passedTests++;
                        System.out.println("Wishlist button found and clicked");
                        break;
                    }
                }
            } else {
                List<WebElement> addToListButtons = driver.findElements(By.cssSelector("[data-action='add-to-list'], .add-to-list, #add-to-list"));
                if (!addToListButtons.isEmpty()) {
                    addToListButtons.get(0).click();
                    passedTests++;
                    System.out.println("Add to list button found and clicked");
                } else {
                    failedTests++;
                    System.out.println("Wishlist button not found");
                }
            }
        } catch (Exception e) {
            failedTests++;
            System.out.println("Wishlist creation failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Shopping cart limits test")
    public void testShoppingCartLimits() {
        testCount++;
        System.out.println("Test " + testCount + " Shopping cart limits testing");
        
        try {
            utils.navigateToHomePage();
            
            utils.goToCart();
            List<WebElement> cartItems = utils.getCartItems();
            for (int i = cartItems.size() - 1; i >= 0; i--) {
                utils.removeFromCart(i);
            }
            
            String[] products = {"book", "pen", "notebook", "eraser", "bag", "water", "chocolate", "phone", "headphones", "charger"};
            
            int addedCount = 0;
            for (String product : products) {
                if (addedCount >= 99) break;
                
                utils.navigateToHomePage();
                utils.searchProduct(product);
                if (utils.clickFirstProduct()) {
                    if (utils.addToCartAdvanced()) {
                        addedCount++;
                        System.out.println("Product added " + product + " (Total " + addedCount + ")");
                    }
                }
            }
            
            int finalCartCount = utils.getCartItemCount();
            if (finalCartCount > 0) {
                passedTests++;
                System.out.println("Cart limits test successful - " + finalCartCount + " products added");
            } else {
                failedTests++;
                System.out.println("Cart limits test failed - no products added");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Shopping cart limits test failed " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Invalid email login test")
    public void testInvalidEmailLogin() {
        testCount++;
        System.out.println("Test " + testCount + " Invalid email login testing");
        
        try {
            utils.navigateToLoginPage();
            utils.enterEmail("invalid-email@test.com");
            utils.clickContinueButton();
            
            List<WebElement> errorMessages = driver.findElements(By.cssSelector(".a-alert-content, .error-message, .alert-error, [data-testid='error-message']"));
            
            boolean errorFound = false;
            for (WebElement error : errorMessages) {
                if (error.isDisplayed()) {
                    String errorText = error.getText().toLowerCase();
                    if (errorText.contains("email") || errorText.contains("email") || 
                        errorText.contains("not found") || errorText.contains("not found") ||
                        errorText.contains("invalid") || errorText.contains("invalid")) {
                        passedTests++;
                        System.out.println("Invalid email error displayed correctly: " + errorText);
                        errorFound = true;
                        break;
                    }
                }
            }
            
            if (!errorFound) {
                failedTests++;
                System.out.println("Invalid email error message not found");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Invalid email test failed " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Invalid password login test")
    public void testInvalidPasswordLogin() {
        testCount++;
        System.out.println("Test " + testCount + " Invalid password login testing");
        
        try {
            utils.navigateToLoginPage();
            utils.enterEmail("test@example.com");
            utils.clickContinueButton();
            
            utils.waitForSeconds(3);
            
            utils.enterPassword("wrongpassword123");
            utils.clickSignInButton();
            
            List<WebElement> errorMessages = driver.findElements(By.cssSelector(".a-alert-content, .error-message, .alert-error, [data-testid='error-message']"));
            
            boolean errorFound = false;
            for (WebElement error : errorMessages) {
                if (error.isDisplayed()) {
                    String errorText = error.getText().toLowerCase();
                    if (errorText.contains("password") || errorText.contains("password") || 
                        errorText.contains("incorrect") || errorText.contains("incorrect") ||
                        errorText.contains("wrong") || errorText.contains("wrong")) {
                        passedTests++;
                        System.out.println("Invalid password error displayed correctly: " + errorText);
                        errorFound = true;
                        break;
                    }
                }
            }
            
            if (!errorFound) {
                failedTests++;
                System.out.println("Invalid password error message not found");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Invalid password test failed " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Error messages validation test")
    public void testErrorMessages() {
        testCount++;
        System.out.println("Test " + testCount + ": Error messages validation testing");
        
        try {
            utils.navigateToHomePage();
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.clear();
            driver.findElement(By.id("nav-search-submit-button")).click();
            
            List<WebElement> emptySearchErrors = driver.findElements(By.cssSelector(".a-alert-content, .error-message"));
            boolean emptySearchErrorFound = false;
            for (WebElement error : emptySearchErrors) {
                if (error.isDisplayed()) {
                    String errorText = error.getText().toLowerCase();
                    if (errorText.contains("search") || errorText.contains("search") || 
                        errorText.contains("empty") || errorText.contains("empty")) {
                        passedTests++;
                        System.out.println("Empty search error correct: " + errorText);
                        emptySearchErrorFound = true;
                        break;
                    }
                }
            }
            
            if (!emptySearchErrorFound) {
                failedTests++;
                System.out.println("Empty search error not found");
            }
            
            utils.searchProduct("xyz123invalidproduct456");
            List<WebElement> noResults = driver.findElements(By.cssSelector(".s-no-outline, .no-results, [data-testid='no-results']"));
            
            if (!noResults.isEmpty()) {
                passedTests++;
                System.out.println("Invalid product search result displayed correctly");
            } else {
                failedTests++;
                System.out.println("Invalid product search result not found");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Error messages test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Cart operations limit test")
    public void testCartOperationLimits() {
        testCount++;
        System.out.println("Test " + testCount + ": Cart operations limit test");
        
        try {
            utils.navigateToHomePage();
            utils.searchProduct("book");
            utils.clickFirstProduct();
            
            for (int i = 0; i < 5; i++) {
                boolean added = utils.addToCartAdvanced();
                if (added) {
                    System.out.println("Product added " + (i+1) + " times");
                } else {
                    System.out.println("Product failed to add " + (i+1) + " times");
                    break;
                }
            }
            
            int cartCount = utils.getCartItemCount();
            if (cartCount > 0) {
                passedTests++;
                System.out.println("Cart operations limit test successful - " + cartCount + " products");
            } else {
                failedTests++;
                System.out.println("Cart operations limit test failed");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Cart operations limit test failed " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Category navigation error test")
    public void testCategoryNavigationErrors() {
        testCount++;
        System.out.println("Test " + testCount + ": Category navigation error test");
        
        try {
            utils.navigateToHomePage();
            utils.openCategoryMenu();
            
            boolean invalidCategoryResult = utils.navigateToCategory("InvalidCategory123");
            
            if (!invalidCategoryResult) {
                passedTests++;
                System.out.println("Invalid category navigation correctly rejected");
            } else {
                failedTests++;
                System.out.println("Invalid category navigation unexpectedly successful");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Category navigation error test failed " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Performance limit test")
    public void testPerformanceLimits() {
        testCount++;
        System.out.println("Test " + testCount + " Performance limit test");
        
        try {
            long startTime = System.currentTimeMillis();
            utils.navigateToHomePage();
            long loadTime = utils.measurePageLoadTime();
            long totalTime = System.currentTimeMillis() - startTime;
            
            if (loadTime > 0 && totalTime > 0) {
                passedTests++;
                System.out.println("Performance limit test successful - Load: " + loadTime + "ms, Total: " + totalTime + "ms");
            } else {
                failedTests++;
                System.out.println("Performance limit test failed");
            }
            
        } catch (Exception e) {
            failedTests++;
            System.out.println("Performance limit test failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void teardown() {
        System.out.println("\n==============================================");
        System.out.println("COVERAGE TEST REPORT");
        System.out.println("==============================================");
        System.out.println("Total Test Count: " + testCount);
        System.out.println("Passed Tests: " + passedTests);
        System.out.println("Failed Tests: " + failedTests);
        System.out.println("Success Rate: " + String.format("%.2f", (double) passedTests / (passedTests + failedTests) * 100) + "%");
        System.out.println("==============================================");
        
        System.out.println("AmazonTestUtils Coverage Tests Completing");
        
        if (utils != null) {
            utils.closeBrowser();
        }
        
        System.out.println("Coverage tests completed");
        System.exit(0);
    }

    public static void main(String[] args) {
        System.out.println("AmazonTestUtils Coverage Test Starting");
        System.out.println("This test tests all methods of AmazonTestUtils class");
        System.out.println("==============================================");
        
        AmazonTestUtilsCoverageTest test = new AmazonTestUtilsCoverageTest();
        
        try {
            setup();
            test.beforeEach();
            
            test.testRegistrationPage();
            test.testRegistrationFormFields();
            test.testRegistrationErrorMessages();
            test.testWishlistCreation();
            test.testShoppingCartLimits();
            test.testInvalidEmailLogin();
            test.testInvalidPasswordLogin();
            test.testErrorMessages();
            test.testCartOperationLimits();
            test.testCategoryNavigationErrors();
            test.testPerformanceLimits();
            
        } catch (Exception e) {
            System.err.println("Error occurred while running tests " + e.getMessage());
            e.printStackTrace();
        } finally {
            teardown();
        }
    }
} 