import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class AmazonTestUtils {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    public AmazonTestUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    
    /**
     * (görünürlük için)
     */
    public void testDelay() {
        try {
            Thread.sleep(2000); // 2 saniye bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Uzun bekleme süresi (önemli şeyler için)
     */
    public void longDelay() {
        try {
            Thread.sleep(3000); // 3 saniye bekle
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    
    /**
     * Cookie banner'ını ve popup'ları hallet
     */
    public void handleCookieBannerAndPopups() {
        System.out.println("Cookie banner ve popup'lar");
        
        try {
            // Cookie banner'ını kapat
            String[] cookieSelectors = {
                "#sp-cc-accept", // Amazon cookie banner
                ".a-button-primary[data-action='accept-cookies']",
                "button[data-action='accept-cookies']",
                ".a-button[data-action='accept-cookies']",
                "#acceptCookies",
                ".cookie-accept",
                ".accept-cookies",
                "button[aria-label*='Accept'], button[aria-label*='Kabul']",
                "button[title*='Accept'], button[title*='Kabul']",
                ".a-button[data-action='accept']",
                "input[value*='Accept'], input[value*='Kabul']",
                "input[value*='Tamam'], input[value*='OK']"
            };
            
            for (String selector : cookieSelectors) {
                try {
                    List<WebElement> cookieButtons = driver.findElements(By.cssSelector(selector));
                    for (WebElement button : cookieButtons) {
                        if (button.isDisplayed() && button.isEnabled()) {
                            button.click();
                            System.out.println("Cookie banner kapatıldı " + selector);
                            testDelay();
                            break;
                        }
                    }
                } catch (Exception e) {
                    // Bu selector çalışmadı, diğerini dene
                    continue;
                }
            }
            
            // Popup'ları kapat
            String[] popupSelectors = {
                ".a-button-close",
                ".a-popover-close",
                ".a-modal-close",
                "button[aria-label*='Close'], button[aria-label*='Kapat']",
                "button[title*='Close'], button[title*='Kapat']",
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
                            System.out.println("Popup kapatıldı: " + selector);
                            testDelay();
                            break;
                        }
                    }
                } catch (Exception e) {
                    // Bu selector çalışmadı, diğerini dene
                    continue;
                }
            }
            
            // JavaScript ile ESC tuşu gönder (popup'ları kapatmak için)
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("document.dispatchEvent(new KeyboardEvent('keydown', {'key': 'Escape'}));");
                System.out.println("ESC tuşu gönderildi");
                testDelay();
            } catch (Exception e) {
                // ESC tuşu çalışmadı?
            }
            
        } catch (Exception e) {
            System.out.println("Cookie banner/popup kapatma hatası: " + e.getMessage());
        }
    }
    
    /**
     * Ana sayfaya git yüklenmesini bekle
     */
    public void navigateToHomePage() {
        System.out.println("Ana sayfaya gidiliyo");
        driver.get("https://www.amazon.com.tr/");
        waitForPageLoad();
        
        // Cookie banner'ını ve popup'ları kapat
        handleCookieBannerAndPopups();
        testDelay();
        System.out.println("Ana sayfa yüklendi");
    }
    
    /**
     * Sayfa yüklenmesini bekle
     */
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
    }
    
    /**
     * Ürün ara
     */
    public void searchProduct(String searchTerm) {
        System.out.println(searchTerm + "aranıyor");
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
        testDelay();
        
        WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));
        searchButton.click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-component-type='s-search-results']")));
        testDelay();
        System.out.println("Arama tamamlandı");
    }
    
    /**
     * Arama sonuçlarını al
     */
    public List<WebElement> getSearchResults() {
        return driver.findElements(By.cssSelector("[data-component-type='s-search-results'] .s-result-item"));
    }
    
    /**
     * İlk ürünü seç detay sayfasına git
     */
    public boolean clickFirstProduct() {
        System.out.println("İlk ürün seçiliyo");
        try {
            List<WebElement> products = driver.findElements(By.cssSelector("[data-component-type='s-search-results'] .s-result-item h2 a"));
            if (!products.isEmpty()) {
                products.get(0).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("productTitle")));
                testDelay();
                System.out.println("Ürün detay sayfası açıldı");
                return true;
            } else {
                System.out.println("Arama sonucunda ürün bulunamadı");
                return false;
            }
        } catch (Exception e) {
            System.out.println("İlk ürün seçilemedi: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Ürün başlığını al
     */
    public String getProductTitle() {
        WebElement productTitle = driver.findElement(By.id("productTitle"));
        return productTitle.getText();
    }
    
    /**
     * Ürün fiyatını al
     */
    public String getProductPrice() {
        try {
            WebElement priceElement = driver.findElement(By.cssSelector(".a-price-whole"));
            return priceElement.getText();
        } catch (NoSuchElementException e) {
            return "Fiyat bulunamadı";
        }
    }
    
    /**
     * Sepete ekle - Gelişmiş versiyon
     */
    public boolean addToCartAdvanced() {
        System.out.println("Sepete ekleniyor (gelişmiş versiyon)...");
        try {
            // cookie banner'ını ve popup'ları kapat
            handleCookieBannerAndPopups();
            
            // Strateji 1 Standart sepete ekle butonu
            if (tryStandardAddToCart()) {
                return true;
            }
            
            // Strateji 2  JavaScript ile sepete ekle
            if (tryJavaScriptAddToCart()) {
                return true;
            }
            
            // Strateji 3 Farklı seçicilerle sepete ekle
            if (tryAlternativeAddToCart()) {
                return true;
            }
            
            // Strateji 4 Form submit ile sepete ekle
            if (tryFormSubmitAddToCart()) {
                return true;
            }
            
            System.out.println("Hiçbir sepete ekleme stratejisi çalışmadı");
            return false;
            
        } catch (Exception e) {
            System.out.println("Sepete ekleme hatası: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Standart sepete ekle butonuyla dene
     */
    private boolean tryStandardAddToCart() {
        try {
            String[] selectors = {
                "#add-to-cart-button",
                "[data-feature-id='add-to-cart-button']",
                "input[value*='Sepete Ekle']",
                "input[value*='Add to Cart']",
                "[aria-label*='Sepete Ekle']",
                "[aria-label*='Add to Cart']",
                "input[type='submit'][value*='Sepete']",
                "input[type='submit'][value*='Cart']",
                ".a-button-input[value*='Sepete']",
                ".a-button-input[value*='Cart']"
            };
            
            for (String selector : selectors) {
                try {
                    WebElement button = driver.findElement(By.cssSelector(selector));
                    if (button.isDisplayed() && button.isEnabled()) {
                        System.out.println("*Standart buton bulundu: " + selector);
                        button.click();
                        System.out.println("*Standart butona tıklandı");
                        waitForAddToCartConfirmation();
                        return true;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * JavaScript ile sepete ekle
     */
    private boolean tryJavaScriptAddToCart() {
        try {
            System.out.println("JavaScript ile sepete ekleme deneniyor...");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // JavaScript ile sepete ekle butonunu bul ve tıkla
            String[] jsScripts = {
                "document.getElementById('add-to-cart-button').click();",
                "document.querySelector('[data-feature-id=\"add-to-cart-button\"]').click();",
                "document.querySelector('input[value*=\"Sepete Ekle\"]').click();",
                "document.querySelector('input[value*=\"Add to Cart\"]').click();",
                "document.querySelector('[aria-label*=\"Sepete Ekle\"]').click();",
                "document.querySelector('[aria-label*=\"Add to Cart\"]').click();"
            };
            
            for (String script : jsScripts) {
                try {
                    js.executeScript(script);
                    System.out.println("JavaScript ile sepete ekleme başarılı");
                    waitForAddToCartConfirmation();
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
    
    /**
     * Alternatif seçicilerle sepete ekle
     */
    private boolean tryAlternativeAddToCart() {
        try {
            System.out.println("Alternatif seçiciler deneniyor...");
            
            // XPath ile farklı butonları dene
            String[] xpathSelectors = {
                "//input[@type='submit' and contains(@value, 'Sepete')]",
                "//input[@type='submit' and contains(@value, 'Cart')]",
                "//button[contains(text(), 'Sepete Ekle')]",
                "//button[contains(text(), 'Add to Cart')]",
                "//a[contains(text(), 'Sepete Ekle')]",
                "//a[contains(text(), 'Add to Cart')]",
                "//span[contains(text(), 'Sepete Ekle')]/parent::button",
                "//span[contains(text(), 'Add to Cart')]/parent::button"
            };
            
            for (String xpath : xpathSelectors) {
                try {
                    WebElement button = driver.findElement(By.xpath(xpath));
                    if (button.isDisplayed() && button.isEnabled()) {
                        System.out.println("Alternatif buton bulundu: " + xpath);
                        button.click();
                        System.out.println("Alternatif butona tıklandı");
                        waitForAddToCartConfirmation();
                        return true;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Form submit ile sepete ekle
     */
    private boolean tryFormSubmitAddToCart() {
        try {
            System.out.println("Form submit deneniyor...");
            
            // Form elementini bul
            WebElement form = driver.findElement(By.cssSelector("form[action*='cart'], form[action*='add']"));
            if (form != null) {
                form.submit();
                System.out.println("Form submit başarılı");
                waitForAddToCartConfirmation();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Sepete ekleme onayını beklemek
     */
    private void waitForAddToCartConfirmation() {
        try {
            // Sepete eklendi mesajını bekle
            String[] confirmationSelectors = {
                "#attachDisplayAddBaseAlert",
                ".a-alert-success",
                ".a-alert-content",
                "[data-feature-id='add-to-cart-alert']",
                ".a-popover-content"
            };
            
            for (String selector : confirmationSelectors) {
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector)));
                    System.out.println("Sepete eklendi mesajı görüldü: " + selector);
                    break;
                } catch (Exception e) {
                    continue;
                }
            }

            Thread.sleep(2000);
            int cartCount = getCartItemCount();
            if (cartCount > 0) {
                System.out.println("Sepete eklendi (sepet sayısı: " + cartCount + ")");
            } else {
                System.out.println("Sepete ekleme durumu belirsiz");
            }
            
            longDelay();
            
        } catch (Exception e) {
            System.out.println("sepete ekleme onayı beklenemedi: " + e.getMessage());
        }
    }
    
    /**
     * Sepete git
     */
    public boolean navigateToCart() {
        System.out.println("Sepet sayfasına gidiliyor...");
        try {
            WebElement cartIcon = driver.findElement(By.id("nav-cart"));
            cartIcon.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sc-active-cart")));
            testDelay();
            System.out.println("Sepet sayfası açıldı");
            return true;
        } catch (Exception e) {
            System.out.println("Sepet sayfasına gidilemedi: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Sepetten ürün kaldır (ilk ürünü)
     */
    public boolean removeFromCart() {
        return removeFromCart(0);
    }
    
    /**
     * Sepete git
     */
    public boolean goToCart() {
        System.out.println("Sepete gidiliyor...");
        try {
            // Farklı sepet butonlarını dene
            WebElement cartButton = null;

            try {
                cartButton = driver.findElement(By.id("nav-cart"));
            } catch (NoSuchElementException e) {
                try {
                    cartButton = driver.findElement(By.cssSelector("[data-feature-id='nav-cart']"));
                } catch (NoSuchElementException e2) {
                    try {
                        cartButton = driver.findElement(By.cssSelector("[aria-label*='Sepet'], [aria-label*='Cart']"));
                    } catch (NoSuchElementException e3) {
                        try {
                            cartButton = driver.findElement(By.xpath("//a[contains(text(), 'Sepet') or contains(text(), 'Cart')]"));
                        } catch (NoSuchElementException e4) {
                            System.out.println("Sepet butonu bulunamadı");
                            return false;
                        }
                    }
                }
            }
            if (cartButton != null && cartButton.isDisplayed() && cartButton.isEnabled()) {
                cartButton.click();
                System.out.println("Sepet butonuna tıklandı");
                
                // Sepet sayfasının yüklendiğini bekle
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sc-cart-header, .a-page-title")));
                    System.out.println("Sepet sayfası açıldı");
                } catch (Exception e) {
                    // Alternatif sepet sayfası elementlerini kontrol et
                    try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sc-list, .a-list-item")));
                        System.out.println("Sepet sayfası açıldı (alternatife)");
                    } catch (Exception e2) {
                        System.out.println("Sepet sayfası yükleme durumu belirsiz");
                    }
                }
                
                longDelay();
                return true;
            } else {
                System.out.println("Sepet butonu tıklanamıyor");
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Sepete gitme hatası " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Sepetteki ürün sayısını al
     */
    public int getCartItemCount() {
        try {
            // Farklı sepet sayısı elementlerini dene
            WebElement cartCount = null;
            try {
                cartCount = driver.findElement(By.id("nav-cart-count"));
            } catch (NoSuchElementException e) {
                try {
                    cartCount = driver.findElement(By.cssSelector("[data-feature-id='nav-cart-count']"));
                } catch (NoSuchElementException e2) {
                    try {
                        cartCount = driver.findElement(By.cssSelector("[aria-label*='sepet'], [aria-label*='cart']"));
                    } catch (NoSuchElementException e3) {
                        try {
                            cartCount = driver.findElement(By.xpath("//span[contains(text(), 'sepet') or contains(text(), 'cart')]"));
                        } catch (NoSuchElementException e4) {
                            return 0;
                        }
                    }
                }
            }
            
            if (cartCount != null) {
                String countText = cartCount.getText().trim();
                // Sayısal değeri çıkar
                countText = countText.replaceAll("[^0-9]", "");
                if (!countText.isEmpty()) {
                    return Integer.parseInt(countText);
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Sepetteki ürünleri listele
     */
    public List<WebElement> getCartItems() {
        try {
            // Farklı sepet ürünleri
            List<WebElement> cartItems = driver.findElements(By.cssSelector(".sc-list-item"));
            if (cartItems.isEmpty()) {
                cartItems = driver.findElements(By.cssSelector(".a-list-item"));
            }
            if (cartItems.isEmpty()) {
                cartItems = driver.findElements(By.cssSelector("[data-feature-id='sc-list-item']"));
            }
            return cartItems;
        } catch (Exception e) {
            return driver.findElements(By.cssSelector(".sc-list-item"));
        }
    }
    
    /**
     * Sepetten ürün sil
     */
    public boolean removeFromCart(int itemIndex) {
        System.out.println("Sepetten ürün siliniyor");
        try {
            // Farklı silme butonlarını
            List<WebElement> removeButtons = driver.findElements(By.cssSelector(".sc-action-delete"));
            if (removeButtons.isEmpty()) {
                removeButtons = driver.findElements(By.cssSelector("[data-feature-id='sc-action-delete']"));
            }
            if (removeButtons.isEmpty()) {
                removeButtons = driver.findElements(By.cssSelector("input[value*='Sil'], input[value*='Delete']"));
            }
            if (removeButtons.isEmpty()) {
                removeButtons = driver.findElements(By.xpath("//input[@type='submit' and contains(@value, 'Sil')]"));
            }
            
            if (itemIndex < removeButtons.size()) {
                removeButtons.get(itemIndex).click();
                longDelay();
                System.out.println("Ürün sepetten silindi");
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Ürün silinemedi " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Kategori menüsünü aç
     */
    public void openCategoryMenu() {
        System.out.println("Kategori menüsü açılıyor");
        WebElement hamburgerMenu = driver.findElement(By.id("nav-hamburger-menu"));
        hamburgerMenu.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#hmenu-content")));
        testDelay();
        System.out.println("Kategori menüsü açıldı");
    }
    
    /**
     * Belirtilen kategoriye git
     */
    public boolean navigateToCategory(String categoryName) {
        System.out.println(categoryName + " kategorisine gidiliyor");
        try {
            WebElement category = driver.findElement(By.xpath("//a[contains(text(), '" + categoryName + "')]"));
            category.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".a-page-title")));
            testDelay();
            System.out.println("Kategori sayfası açıldı ");
            return true;
        } catch (NoSuchElementException e) {

            System.out.println("Kategori bulunamadı " + categoryName);
            return false;
        }
    }
    
    /**
     * Sayfayı scroll et
     */
    public void scrollToBottom() {
        System.out.println("Sayfa aşağı kaydırılıyor");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        wait.until(ExpectedConditions.jsReturnsValue(
                "return window.pageYOffset + window.innerHeight >= document.body.scrollHeight - 10"));
        testDelay();
        System.out.println("Sayfa sonuna ulaşıldı");
    }
    

    public void setWindowSize(int width, int height) {
        System.out.println("Pencere boyutu değiştiriliyor  " + width + "x" + height);
        driver.manage().window().setSize(new Dimension(width, height));
        testDelay();
    }
    
    /**
     * Pencereyi maksimize et
     */
    public void maximizeWindow() {
        System.out.println("Pencere maksimise ediliyor...");
        driver.manage().window().maximize();
        testDelay();
    }

    public void openNewTab(String url) {
        System.out.println("Yeni sekme açılıyor...");
        ((JavascriptExecutor) driver).executeScript("window.open('" + url + "', '_blank');");
        testDelay();
    }
    
    /**
     * Sekmeler arası geçiş yap
     */
    public void switchToTab(int tabIndex) {
        System.out.println("Sekme " + tabIndex + "'e geçiliyor...");
        String[] windowHandles = driver.getWindowHandles().toArray(new String[0]);
        if (tabIndex < windowHandles.length) {
            driver.switchTo().window(windowHandles[tabIndex]);
            testDelay();
        }
    }

    public void closeCurrentTab() {
        System.out.println("Sekme kapatılıyor...");
        driver.close();
        testDelay();
    }
    

    public boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }

    }
    

    public boolean isElementClickable(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
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
        return driver.findElements(By.cssSelector("#departments .a-spacing-micro"));
    }

    public boolean applyFilter(int filterIndex) {
        System.out.println("Filtre uygulanıyor");
        try {
            List<WebElement> filters = getFilterOptions();
            if (filterIndex < filters.size()) {
                filters.get(filterIndex).click();
                waitForPageLoad();
                testDelay();
                System.out.println("Filtre uygulandı");
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Filtre uygulanamadı");
            return false;
        }
    }
    

    public void closeBrowser() {
        System.out.println("Tarayıcı kapatılıyor...");
        if (driver != null) {
            driver.quit();
        }
        System.out.println("Tarayıcı kapatıldı");
    }
    
    /**
     * Sepete ekle
     */
    public boolean addToCart() {
        System.out.println("Sepete ekleniyor...");
        return addToCartAdvanced();
    }
    
    /**
     * Giriş sayfasına git
     */
    public void navigateToLoginPage() {
        System.out.println("Giriş sayfasına gidiliyor...");
        driver.get("https://www.amazon.com.tr/ap/signin");
        waitForPageLoad();
        testDelay();
        System.out.println("Giriş sayfası yüklendi");
    }

    public void enterEmail(String email) {
        System.out.println("E-posta adresi giriliyor...");
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ap_email")));
        emailInput.clear();
        emailInput.sendKeys(email);
        testDelay();
        System.out.println("E-posta adresi girildi");
    }

    public void clickContinueButton() {
        System.out.println("Devam et butonuna tıklanıyor...");
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("continue")));
        continueButton.click();
        testDelay();
        System.out.println("Devam et butonuna tıklandı");
    }

    /**
     * Şifreyi gir
     */
    public void enterPassword(String password) {
        System.out.println("Şifre giriliyor...");
        WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ap_password")));
        passwordInput.clear();
        passwordInput.sendKeys(password);
        testDelay();
        System.out.println("Şifre girildi");
    }

    /**
     * Giriş yap butonuna tıkla
     */
    public void clickSignInButton() {
        System.out.println("Giriş yap butonuna tıklanıyor...");
        WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("signInSubmit")));
        signInButton.click();
        testDelay();
        System.out.println("Giriş yap butonuna tıklandı");
    }

    /**
     * Kullanıcının giriş yapmış olup olmadığını kontrol et
     */
    public boolean isUserLoggedIn() {
        try {
            // Giriş yapmış kullanıcı için özel bir element var mı kontrol et
            WebElement accountElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-link-accountList")));
            String accountText = accountElement.getText().toLowerCase();
            return !accountText.contains("giriş yap") && !accountText.contains("sign in");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Belirtilen süre kadar bekle
     */
    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 