import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class AmazonTestUtils {
    
    private final WebDriver driver;
    public final WebDriverWait wait;
    
    public AmazonTestUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    
    public void testDelay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void longDelay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void handleCookieBannerAndPopups() {
        System.out.println("Cookie banner and popups");
        
        try {
            String[] cookieSelectors = {
                "#sp-cc-accept",
                ".a-button-primary[data-action='accept-cookies']",
                "button[data-action='accept-cookies']",
                ".a-button[data-action='accept-cookies']",
                "#acceptCookies",
                ".cookie-accept",
                ".accept-cookies",
                "button[aria-label*='Accept'], button[aria-label*='Accept']",
                "button[title*='Accept'], button[title*='Accept']",
                ".a-button[data-action='accept']",
                "input[value*='Accept'], input[value*='Accept']",
                "input[value*='OK'], input[value*='OK']"
            };
            
            for (String selector : cookieSelectors) {
                try {
                    List<WebElement> cookieButtons = driver.findElements(By.cssSelector(selector));
                    for (WebElement button : cookieButtons) {
                        if (button.isDisplayed() && button.isEnabled()) {
                            button.click();
                            System.out.println("Cookie banner closed " + selector);
                            testDelay();
                            break;
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            String[] popupSelectors = {
                ".a-button-close",
                ".a-popover-close",
                ".a-modal-close",
                "button[aria-label*='Close'], button[aria-label*='Close']",
                "button[title*='Close'], button[title*='Close']",
                ".close-button",
                ".popup-close",
                ".modal-close",
                "span[class*='close']",
                "i[class*='close']"
            };
            
            for (String selector : popupSelectors) {
                try {
                    List<WebElement> popupButtons = driver.findElements(By.cssSelector(selector));
                    for (WebElement button : popupButtons) {
                        if (button.isDisplayed() && button.isEnabled()) {
                            button.click();
                            System.out.println("Popup closed: " + selector);
                            testDelay();
                            break;
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("document.dispatchEvent(new KeyboardEvent('keydown', {'key': 'Escape'}));");
                System.out.println("ESC key sent");
                testDelay();
            } catch (Exception e) {
            }
            
        } catch (Exception e) {
            System.out.println("Cookie banner/popup closing error: " + e.getMessage());
        }
    }
    
    public void navigateToHomePage() {
        System.out.println("Navigating to home page");
        driver.get("https://www.amazon.com.tr/");
        waitForPageLoad();
        
        handleCookieBannerAndPopups();
        testDelay();
        System.out.println("Home page loaded");
    }
    
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
    }
    
    public void searchProduct(String searchTerm) {
        System.out.println("Searching for " + searchTerm);
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
        testDelay();
        
        WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));
        searchButton.click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-component-type='s-search-results']")));
        testDelay();
        System.out.println("Search completed");
    }
    
    public List<WebElement> getSearchResults() {
        return driver.findElements(By.cssSelector("[data-component-type='s-search-result']"));
    }
    
    public boolean clickFirstProduct() {
        System.out.println("Selecting first product (simple & visible)");
        try {
            List<WebElement> results = getSearchResults();
            if (results.isEmpty()) {
                System.out.println("No products found in search results");
                return false;
            }
            // İlk üründeki linki bul
            WebElement firstResult = results.get(0);
            WebElement link = null;
            try {
                link = firstResult.findElement(By.cssSelector("h2 a"));
            } catch (Exception e) {
                System.out.println("First product does not have a clickable link");
                return false;
            }
            // Görünür ve tıklanabilir mi kontrolü
            if (!link.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);
                testDelay();
            }
            link.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("productTitle")));
            testDelay();
            System.out.println("Product detail page opened");
            return true;
        } catch (Exception e) {
            System.out.println("Could not select first product: " + e.getMessage());
            return false;
        }
    }
    
    public String getProductTitle() {
        WebElement productTitle = driver.findElement(By.id("productTitle"));
        return productTitle.getText();
    }
    
    public String getProductPrice() {
        try {
            WebElement priceElement = driver.findElement(By.cssSelector(".a-price-whole"));
            return priceElement.getText();
        } catch (NoSuchElementException e) {
            return "Price not found";
        }
    }
    
    public boolean addToCartAdvanced() {
        System.out.println("Sepete ekleniyor (basit versiyon)...");
        try {
            handleCookieBannerAndPopups();
            // En yaygın buton seçicileri
            String[] selectors = {
                "#add-to-cart-button",
                "[data-feature-id='add-to-cart-button']",
                "input[value*='Sepete Ekle']",
                "input[value*='Add to Cart']",
                "button[name='submit.add-to-cart']"
            };
            for (String selector : selectors) {
                try {
                    WebElement button = driver.findElement(By.cssSelector(selector));
                    if (button.isDisplayed() && button.isEnabled()) {
                        System.out.println("Buton bulundu ve tıklanıyor: " + selector);
                        button.click();
                        // Başarıyı kontrol et
                        if (waitForAddToCartConfirmation()) {
                            System.out.println("Ürün sepete eklendi!");
                            return true;
                        } else {
                            System.out.println("Sepete ekleme onayı alınamadı.");
                        }
                    }
                } catch (Exception e) {
                    // Buton bulunamazsa diğer seçiciye geç
                    continue;
                }
            }
            System.out.println("Hiçbir sepete ekle butonu bulunamadı veya işlem başarısız oldu.");
            return false;
        } catch (Exception e) {
            System.out.println("Sepete ekleme hatası: " + e.getMessage());
            return false;
        }
    }
    
    public boolean waitForAddToCartConfirmation() {
        try {
            String[] confirmationSelectors = {
                "#attachDisplayAddBaseAlert",
                ".a-alert-success",
                "#attach-added-to-cart-alert",
                ".a-alert-content"
            };
            for (String selector : confirmationSelectors) {
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector)));
                    return true;
                } catch (Exception e) {
                    continue;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean navigateToCart() {
        try {
            System.out.println("Navigating to cart...");
            driver.get("https://www.amazon.com.tr/gp/cart/view.html");
            waitForPageLoad();
            testDelay();
            System.out.println("Cart page loaded");
            return true;
        } catch (Exception e) {
            System.out.println("Cart navigation failed: " + e.getMessage());
            return false;
        }
    }
    
    public boolean removeFromCart() {
        return removeFromCart(0);
    }
    
    public boolean goToCart() {
        try {
            System.out.println("Going to cart...");
            
            String[] cartSelectors = {
                "#nav-cart",
                "[data-feature-id='nav-cart']",
                ".nav-cart",
                "a[href*='cart']",
                "[aria-label*='Cart']",
                "[title*='Cart']"
            };
            
            for (String selector : cartSelectors) {
                try {
                    WebElement cartButton = driver.findElement(By.cssSelector(selector));
                    if (cartButton.isDisplayed() && cartButton.isEnabled()) {
                        System.out.println("Cart button found: " + selector);
                        cartButton.click();
                        waitForPageLoad();
                        testDelay();
                        System.out.println("Cart page opened");
                        return true;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            System.out.println("Cart button not found, using direct navigation");
            return navigateToCart();
            
        } catch (Exception e) {
            System.out.println("Go to cart failed: " + e.getMessage());
            return false;
        }
    }
    
    public int getCartItemCount() {
        try {
            String[] countSelectors = {
                "#nav-cart-count",
                ".nav-cart-count",
                "[data-feature-id='nav-cart-count']",
                ".cart-count",
                "#cart-count"
            };
            
            for (String selector : countSelectors) {
                try {
                    WebElement countElement = driver.findElement(By.cssSelector(selector));
                    String countText = countElement.getText().trim();
                    if (!countText.isEmpty()) {
                        int count = Integer.parseInt(countText);
                        System.out.println("Cart item count: " + count);
                        return count;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            System.out.println("Cart count not found");
            return 0;
            
        } catch (Exception e) {
            System.out.println("Get cart count error: " + e.getMessage());
            return 0;
        }
    }
    
    public List<WebElement> getCartItems() {
        try {
            List<WebElement> cartItems = driver.findElements(By.cssSelector("[data-name='Active Items'] .sc-list-item"));
            if (cartItems.isEmpty()) {
                cartItems = driver.findElements(By.cssSelector(".sc-list-item"));
            }
            if (cartItems.isEmpty()) {
                cartItems = driver.findElements(By.cssSelector(".cart-item"));
            }
            System.out.println("Cart items found: " + cartItems.size());
            return cartItems;
        } catch (Exception e) {
            System.out.println("Get cart items error: " + e.getMessage());
            return List.of();
        }
    }
    
    public boolean removeFromCart(int itemIndex) {
        try {
            System.out.println("Removing item " + itemIndex + " from cart");
            
            List<WebElement> removeButtons = driver.findElements(By.cssSelector("input[value*='Delete'], input[value*='Remove'], .a-button[data-action='delete']"));
            
            if (itemIndex < removeButtons.size()) {
                WebElement removeButton = removeButtons.get(itemIndex);
                if (removeButton.isDisplayed() && removeButton.isEnabled()) {
                    removeButton.click();
                    System.out.println("Remove button clicked");
                    testDelay();
                    return true;
                }
            }
            
            System.out.println("Remove button not found for index " + itemIndex);
            return false;
            
        } catch (Exception e) {
            System.out.println("Remove from cart error: " + e.getMessage());
            return false;
        }
    }
    
    public void openCategoryMenu() {
        try {
            System.out.println("Opening category menu...");
            
            String[] menuSelectors = {
                "#nav-hamburger-menu",
                ".nav-hamburger-menu",
                "[data-feature-id='nav-hamburger-menu']",
                ".hamburger-menu",
                "button[aria-label*='Menu']",
                "button[title*='Menu']"
            };
            
            for (String selector : menuSelectors) {
                try {
                    WebElement menuButton = driver.findElement(By.cssSelector(selector));
                    if (menuButton.isDisplayed() && menuButton.isEnabled()) {
                        menuButton.click();
                        System.out.println("Category menu opened");
                        testDelay();
                        return;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            System.out.println("Category menu button not found");
            
        } catch (Exception e) {
            System.out.println("Open category menu error: " + e.getMessage());
        }
    }
    
    public boolean navigateToCategory(String categoryName) {
        try {
            System.out.println("Navigating to category: " + categoryName);
            // Sayfanın üst kısmındaki ana kategori sekmelerini bul
            List<WebElement> categoryTabs = driver.findElements(By.cssSelector("a, span, div"));
            for (WebElement tab : categoryTabs) {
                String text = tab.getText().trim().toLowerCase();
                if (!text.isEmpty() && text.contains(categoryName.toLowerCase())) {
                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tab);
                        testDelay();
                        tab.click();
                        System.out.println("Category tab clicked: " + text);
                        testDelay();
                        return true;
                    } catch (Exception e) {
                        System.out.println("Kategoriye tıklanamadı: " + text);
                    }
                }
            }
            System.out.println("Category not found on main page: " + categoryName);
            return false;
        } catch (Exception e) {
            System.out.println("Navigate to category error: " + e.getMessage());
            return false;
        }
    }
    
    public void scrollToBottom() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            System.out.println("Scrolled to bottom");
            testDelay();
        } catch (Exception e) {
            System.out.println("Scroll to bottom error: " + e.getMessage());
        }
    }
    
    public void setWindowSize(int width, int height) {
        try {
            driver.manage().window().setSize(new Dimension(width, height));
            System.out.println("Window size set to: " + width + "x" + height);
            testDelay();
        } catch (Exception e) {
            System.out.println("Set window size error: " + e.getMessage());
        }
    }
    
    public void maximizeWindow() {
        try {
            driver.manage().window().maximize();
            System.out.println("Window maximized");
            testDelay();
        } catch (Exception e) {
            System.out.println("Maximize window error: " + e.getMessage());
        }
    }
    
    public void openNewTab(String url) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.open('" + url + "', '_blank');");
            System.out.println("New tab opened: " + url);
            testDelay();
        } catch (Exception e) {
            System.out.println("Open new tab error: " + e.getMessage());
        }
    }
    
    public void switchToTab(int tabIndex) {
        try {
            driver.switchTo().window(driver.getWindowHandles().toArray()[tabIndex].toString());
            System.out.println("Switched to tab: " + tabIndex);
            testDelay();
        } catch (Exception e) {
            System.out.println("Switch to tab error: " + e.getMessage());
        }
    }
    
    public void closeCurrentTab() {
        try {
            driver.close();
            System.out.println("Current tab closed");
            testDelay();
        } catch (Exception e) {
            System.out.println("Close current tab error: " + e.getMessage());
        }
    }
    
    public boolean isElementVisible(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isElementClickable(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed() && element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    public long measurePageLoadTime() {
        long startTime = System.currentTimeMillis();
        waitForPageLoad();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    
    public List<WebElement> getFilterOptions() {
        return driver.findElements(By.cssSelector(".a-spacing-micro"));
    }
    
    public boolean applyFilter(int filterIndex) {
        try {
            List<WebElement> filters = getFilterOptions();
            if (filterIndex < filters.size()) {
                filters.get(filterIndex).click();
                System.out.println("Filter applied: " + filterIndex);
                testDelay();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Apply filter error: " + e.getMessage());
            return false;
        }
    }
    
    public void closeBrowser() {
        try {
            if (driver != null) {
                driver.quit();
                System.out.println("Browser closed");
            }
        } catch (Exception e) {
            System.out.println("Close browser error: " + e.getMessage());
        }
    }
    
    public boolean addToCart() {
        return addToCartAdvanced();
    }
    
    public void navigateToLoginPage() {
        try {
            System.out.println("Navigating to login page...");
            driver.get("https://www.amazon.com/ap/signin?openid.pape.max_auth_age=900&openid.return_to=https%3A%2F%2Fwww.amazon.com%2Fgp%2Fyourstore%2Fhome%3Fpath%3D%252Fgp%252Fyourstore%252Fhome%26useRedirectOnSuccess%3D1%26signIn%3D1%26action%3Dsign-out%26ref_%3Dnav_AccountFlyout_signout&openid.assoc_handle=usflex&openid.mode=checkid_setup&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0");
            waitForPageLoad();
            testDelay();
            System.out.println("Login page loaded");
        } catch (Exception e) {
            System.out.println("Navigate to login page error: " + e.getMessage());
        }
    }
    
    public void enterEmail(String email) {
        try {
            WebElement emailField = driver.findElement(By.id("ap_email"));
            emailField.clear();
            emailField.sendKeys(email);
            System.out.println("Email entered: " + email);
            testDelay();
        } catch (Exception e) {
            System.out.println("Enter email error: " + e.getMessage());
        }
    }
    
    public void clickContinueButton() {
        try {
            WebElement continueButton = driver.findElement(By.id("continue"));
            continueButton.click();
            System.out.println("Continue button clicked");
            testDelay();
        } catch (Exception e) {
            System.out.println("Click continue button error: " + e.getMessage());
        }
    }
    
    public void enterPassword(String password) {
        try {
            WebElement passwordField = driver.findElement(By.id("ap_password"));
            passwordField.clear();
            passwordField.sendKeys(password);
            System.out.println("Password entered");
            testDelay();
        } catch (Exception e) {
            System.out.println("Enter password error: " + e.getMessage());
        }
    }
    
    public void clickSignInButton() {
        try {
            WebElement signInButton = driver.findElement(By.id("signInSubmit"));
            signInButton.click();
            System.out.println("Sign in button clicked");
            testDelay();
        } catch (Exception e) {
            System.out.println("Click sign in button error: " + e.getMessage());
        }
    }
    
    public boolean isUserLoggedIn() {
        try {
            String currentUrl = driver.getCurrentUrl();
            return !currentUrl.contains("signin") && !currentUrl.contains("login");
        } catch (Exception e) {
            System.out.println("Check login status error: " + e.getMessage());
            return false;
        }
    }
    
    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            System.out.println("Waited for " + seconds + " seconds");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Amazon'da arama yapar, ilk ürünü sepete ekler (en basit ve doğrudan yöntem).
     */
    public boolean addFirstProductToCart(String searchTerm) {
        try {
            driver.get("https://www.amazon.com.tr/");
            waitForPageLoad();
            handleCookieBannerAndPopups();
            testDelay();

            // Arama
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.clear();
            searchBox.sendKeys(searchTerm);
            driver.findElement(By.id("nav-search-submit-button")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-component-type='s-search-result']")));
            testDelay();

            // İlk ürünün ilk <a> etiketini bul ve tıkla
            List<WebElement> results = driver.findElements(By.cssSelector("[data-component-type='s-search-result']"));
            if (results.isEmpty()) {
                System.out.println("Arama sonucu yok!");
                return false;
            }
            WebElement firstResult = results.get(0);
            WebElement firstLink = null;
            try {
                firstLink = firstResult.findElement(By.tagName("a"));
            } catch (Exception e) {
                System.out.println("İlk üründe <a> etiketi yok: " + e.getMessage());
                return false;
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstLink);
            testDelay();
            firstLink.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("productTitle")));
            testDelay();

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
            boolean confirmed = waitForAddToCartConfirmation();
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

    public boolean clickFirstRealProduct(String searchTerm) {
        try {
            driver.get("https://www.amazon.com.tr/");
            waitForPageLoad();
            handleCookieBannerAndPopups();
            testDelay();

            // Arama
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.clear();
            searchBox.sendKeys(searchTerm);
            driver.findElement(By.id("nav-search-submit-button")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-component-type='s-search-result']")));
            testDelay();

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
            testDelay();
            System.out.println("Tıklanan ürün: " + productText);
            firstProductLink.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("productTitle")));
            testDelay();
            return true;
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            return false;
        }
    }
} 