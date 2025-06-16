import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AmazonAdvancedTest {

    private static WebDriver driver;
    private static AmazonTestUtils utils;

    @BeforeAll
    public static void setup() {
        System.out.println("🚀 Amazon Advanced Test Bot Başlatılıyor...");
        System.out.println("==============================================");
        
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
        
        System.out.println("✅ Gelişmiş test ortamı hazırlandı");
    }

    @BeforeEach
    public void beforeEach() {
        // Her test öncesi ana sayfaya git
        utils.navigateToHomePage();
    }

    // Parametreli test: Farklı ürünler için arama testi
    @ParameterizedTest
    @ValueSource(strings = {"laptop", "telefon", "kitap",})
    @DisplayName("Farklı ürünler için arama testi")
    public void testProductSearchWithDifferentTerms(String searchTerm) {
        System.out.println("\n📋 Parametreli Test: '" + searchTerm + "' arama testi");
        System.out.println("----------------------------------------");
        
        utils.searchProduct(searchTerm);
        
        List<WebElement> searchResults = utils.getSearchResults();
        assertFalse(searchResults.isEmpty(), searchTerm + " için arama sonuçları görünür olmalı");
        
        String pageTitle = utils.getPageTitle();
        assertTrue(pageTitle.toLowerCase().contains(searchTerm.toLowerCase()) || 
                  pageTitle.contains("Amazon"), 
                  "Sayfa başlığı arama terimini veya Amazon'u içermeli");
        
        System.out.println("✅ '" + searchTerm + "' arama testi başarılı - " + searchResults.size() + " sonuç");
    }

    // Parametreli test: Farklı kategoriler için navigasyon testi
    @ParameterizedTest
    @CsvSource({
        "Elektronik, true",
        "Kitap, true", 
        "Moda, true",
    })
    @DisplayName("Kategori navigasyonu testi")
    public void testCategoryNavigation(String categoryName, boolean expectedResult) {
        System.out.println("\n📋 Parametreli Test: '" + categoryName + "' kategori testi");
        System.out.println("----------------------------------------");
        
        utils.openCategoryMenu();
        
        boolean result = utils.navigateToCategory(categoryName);
        assertEquals(expectedResult, result, categoryName + " kategorisine navigasyon " + 
                    (expectedResult ? "başarılı" : "başarısız") + " olmalı");
        
        if (result) {
            String pageTitle = utils.getPageTitle();
            assertTrue(pageTitle.contains(categoryName) || pageTitle.contains("Amazon"), 
                      "Kategori sayfası doğru yüklenmeli");
            System.out.println("✅ '" + categoryName + "' kategori testi başarılı");
        } else {
            System.out.println("⚠️ '" + categoryName + "' kategori testi başarısız");
        }
    }

    // Test: Ürün detay sayfası bilgilerini kontrol et
    @Test
    @DisplayName("Ürün detay sayfası bilgileri testi")
    public void testProductDetailInformation() {
        System.out.println("\n📋 Test: Ürün detay sayfası bilgileri testi");
        System.out.println("----------------------------------------");
        
        utils.searchProduct("laptop");
        utils.clickFirstProduct();
        
        // Ürün başlığını kontrol et
        String productTitle = utils.getProductTitle();
        assertNotNull(productTitle, "Ürün başlığı null olmamalı");
        assertFalse(productTitle.trim().isEmpty(), "Ürün başlığı boş olmamalı");
        
        // Ürün fiyatını kontrol et
        String productPrice = utils.getProductPrice();
        assertNotNull(productPrice, "Ürün fiyatı null olmamalı");
        
        // Sayfa URL'sini kontrol et
        String currentUrl = utils.getCurrentUrl();
        assertTrue(currentUrl.contains("amazon.com.tr"), "URL Amazon Turkey içermeli");
        
        System.out.println("📦 Ürün: " + productTitle);
        System.out.println("💰 Fiyat: " + productPrice);
        System.out.println("🌐 URL: " + currentUrl);
        System.out.println("✅ Ürün detay bilgileri testi başarılı");
    }

    // Test: Sepete ekleme işlemi
    @Test
    @DisplayName("Sepete ekleme işlemi testi")
    public void testAddToCartProcess() {
        System.out.println("\n📋 Test: Sepete ekleme işlemi testi");
        System.out.println("----------------------------------------");
        
        utils.searchProduct("kitap");
        utils.clickFirstProduct();
        
        boolean addedToCart = utils.addToCartAdvanced();
        assertTrue(addedToCart, "Ürün sepete eklenebilmeli");
        
        // Sepetteki ürün sayısını kontrol et
        int cartCount = utils.getCartItemCount();
        System.out.println("🛒 Sepetteki ürün sayısı: " + cartCount);
        
        System.out.println("✅ Sepete ekleme işlemi testi başarılı");
    }

    // Test: Sepete gitme ve ürün kontrolü
    @Test
    @DisplayName("Sepete gitme ve ürün kontrolü testi")
    public void testGoToCartAndCheckItems() {
        System.out.println("\n📋 Test: Sepete gitme ve ürün kontrolü testi");
        System.out.println("----------------------------------------");
        
        // Önce bir ürün ekle
        utils.searchProduct("kalem");
        utils.clickFirstProduct();
        utils.addToCartAdvanced();
        
        // Sepete git
        boolean cartOpened = utils.goToCart();
        assertTrue(cartOpened, "Sepet sayfası açılabilmeli");
        
        // Sepetteki ürünleri listele
        List<WebElement> cartItems = utils.getCartItems();
        System.out.println("🛒 Sepetteki ürün sayısı: " + cartItems.size());
        
        if (!cartItems.isEmpty()) {
            System.out.println("✅ Sepete gitme ve ürün kontrolü testi başarılı");
        } else {
            System.out.println("⚠️ Sepet boş görünüyor");
        }
    }

    // Test: Filtreleme işlemi
    @Test
    @DisplayName("Filtreleme işlemi testi")
    public void testFilteringProcess() {
        System.out.println("\n📋 Test: Filtreleme işlemi testi");
        System.out.println("----------------------------------------");
        
        utils.searchProduct("telefon");
        
        List<WebElement> filterOptions = utils.getFilterOptions();
        if (!filterOptions.isEmpty()) {
            System.out.println("🔍 Bulunan filtre sayısı: " + filterOptions.size());
            
            boolean filterApplied = utils.applyFilter(0);
            assertTrue(filterApplied, "İlk filtre uygulanabilmeli");
            
            // Filtrelenmiş sonuçları kontrol et
            List<WebElement> filteredResults = utils.getSearchResults();
            assertFalse(filteredResults.isEmpty(), "Filtrelenmiş sonuçlar görünür olmalı");
            
            System.out.println("✅ Filtreleme işlemi testi başarılı - " + filteredResults.size() + " filtrelenmiş sonuç");
        } else {
            System.out.println("⚠️ Filtreleme seçenekleri bulunamadı");
        }
    }
    @Test
    @DisplayName("Fiyatı en düşükten sırala ve sepete ekle/sil testi")
    public void testSortByLowestPriceAndCartRemove() {
        utils.searchProduct("laptop");
        utils.sortByLowestPrice();

        // İlk ürünü sepete ekle
        boolean productClicked = utils.clickFirstProduct();
        assertTrue(productClicked, "İlk ürün seçilebilmeli");
        boolean added = utils.addToCartAdvanced();
        assertTrue(added, "Ürün sepete eklenebilmeli");

        // Sepete git ve ilk ürünü sil
        boolean cartOpened = utils.goToCart();
        assertTrue(cartOpened, "Sepet açılabilmeli");
        int beforeCount = utils.getCartItemCount();
        utils.removeFirstItemFromCart();
        int afterCount = utils.getCartItemCount();
        assertTrue(afterCount < beforeCount, "Sepetteki ürün sayısı azalmış olmalı");
    }

    // Test: Responsive tasarım kontrolü
    @ParameterizedTest
    @CsvSource({
        "375, 667, iPhone",
        "768, 1024, iPad", 
        "1920, 1080, Desktop"
    })
    @DisplayName("Responsive tasarım testi")
    public void testResponsiveDesign(int width, int height, String device) {
        System.out.println("\n📋 Parametreli Test: " + device + " responsive tasarım testi");
        System.out.println("----------------------------------------");
        
        utils.setWindowSize(width, height);
        
        // Temel elementlerin görünür olduğunu kontrol et
        assertTrue(utils.isElementVisible(By.id("nav-logo-sprites")), 
                  device + " için Amazon logosu görünür olmalı");
        assertTrue(utils.isElementVisible(By.id("nav-search")), 
                  device + " için arama kutusu görünür olmalı");
        
        // Mobil görünümde hamburger menü kontrolü kaldırıldı
        
        utils.maximizeWindow();
        System.out.println("✅ " + device + " responsive tasarım testi başarılı");
    }

    // Test: Performans testi
    @Test
    @DisplayName("Performans testi")
    public void testPerformance() {
        System.out.println("\n📋 Test: Performans testi");
        System.out.println("----------------------------------------");
        
        long loadTime = utils.measurePageLoadTime();
        
        // Sayfa yükleme süresinin makul olduğunu kontrol et
        assertTrue(loadTime < 10000, "Sayfa 10 saniyeden kısa sürede yüklenmeli. Süre: " + loadTime + "ms");
        
        System.out.println("⏱️ Ana sayfa yükleme süresi: " + loadTime + "ms");
        System.out.println("✅ Performans testi başarılı");
    }

    // Test: Çoklu sekme işlemleri
    @Test
    @DisplayName("Çoklu sekme işlemleri testi")
    public void testMultipleTabs() {
        System.out.println("\n📋 Test: Çoklu sekme işlemleri testi");
        System.out.println("----------------------------------------");
        
        String originalUrl = utils.getCurrentUrl();
        
        // Yeni sekme aç
        utils.openNewTab("https://www.amazon.com.tr/");
        utils.switchToTab(1);
        
        // Yeni sekmede sayfanın yüklendiğini kontrol et
        utils.waitForPageLoad();
        assertTrue(utils.getPageTitle().contains("Amazon"), "Yeni sekmede Amazon sayfası yüklenmeli");
        
        // Orijinal sekmeye geri dön
        utils.switchToTab(0);
        assertEquals(originalUrl, utils.getCurrentUrl(), "Orijinal sekmede doğru URL olmalı");
        
        // Yeni sekmeyi kapat
        utils.closeCurrentTab();
        utils.switchToTab(0);
        
        System.out.println("✅ Çoklu sekme işlemleri testi başarılı");
    }

    // Test: Scroll işlemleri
    @Test
    @DisplayName("Scroll işlemleri testi")
    public void testScrolling() {
        System.out.println("\n📋 Test: Scroll işlemleri testi");
        System.out.println("----------------------------------------");
        
        // Sayfanın sonuna scroll yap
        utils.scrollToBottom();
        
        // Scroll pozisyonunu kontrol et
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long scrollPosition = (Long) js.executeScript("return window.pageYOffset");
        
        assertTrue(scrollPosition > 0, "Sayfa scroll edilmiş olmalı");
        
        // Sayfanın başına geri dön
        js.executeScript("window.scrollTo(0, 0)");
        
        Long newScrollPosition = (Long) js.executeScript("return window.pageYOffset");
        assertEquals(0, newScrollPosition, "Sayfa başına dönülmüş olmalı");
        
        System.out.println("📜 Scroll pozisyonu: " + scrollPosition + "px");
        System.out.println("✅ Scroll işlemleri testi başarılı");
    }

    // Test: Element etkileşimleri
    @Test
    @DisplayName("Element etkileşimleri testi")
    public void testElementInteractions() {
        System.out.println("\n📋 Test: Element etkileşimleri testi");
        System.out.println("----------------------------------------");
        
        // Arama kutusunun tıklanabilir olduğunu kontrol et
        assertTrue(utils.isElementClickable(By.id("twotabsearchtextbox")), 
                  "Arama kutusu tıklanabilir olmalı");
        
        // Arama butonunun tıklanabilir olduğunu kontrol et
        assertTrue(utils.isElementClickable(By.id("nav-search-submit-button")), 
                  "Arama butonu tıklanabilir olmalı");
        
        // Sepet ikonunun görünür olduğunu kontrol et
        assertTrue(utils.isElementVisible(By.id("nav-cart")), 
                  "Sepet ikonu görünür olmalı");
        
        System.out.println("✅ Element etkileşimleri testi başarılı");
    }

    // Test: URL doğrulama
    @Test
    @DisplayName("URL doğrulama testi")
    public void testUrlValidation() {
        System.out.println("\n📋 Test: URL doğrulama testi");
        System.out.println("----------------------------------------");
        
        String currentUrl = utils.getCurrentUrl();
        
        // URL'nin Amazon Turkey olduğunu kontrol et
        assertTrue(currentUrl.contains("amazon.com.tr"), "URL Amazon Turkey içermeli");
        assertTrue(currentUrl.startsWith("https://"), "URL HTTPS protokolü kullanmalı");
        
        System.out.println("🌐 URL: " + currentUrl);
        System.out.println("✅ URL doğrulama testi başarılı");
    }

    // Test: Sayfa başlığı doğrulama
    @Test
    @DisplayName("Sayfa başlığı doğrulama testi")
    public void testPageTitleValidation() {
        System.out.println("\n📋 Test: Sayfa başlığı doğrulama testi");
        System.out.println("----------------------------------------");
        
        String pageTitle = utils.getPageTitle();
        
        // Sayfa başlığının geçerli olduğunu kontrol et
        assertNotNull(pageTitle, "Sayfa başlığı null olmamalı");
        assertFalse(pageTitle.trim().isEmpty(), "Sayfa başlığı boş olmamalı");
        assertTrue(pageTitle.contains("Amazon") || pageTitle.contains("amazon"), 
                  "Sayfa başlığı Amazon içermeli");
        
        System.out.println("📄 Sayfa başlığı: " + pageTitle);
        System.out.println("✅ Sayfa başlığı doğrulama testi başarılı");
    }

    // Test: Giriş yapma işlemi
    @Test
    @DisplayName("Giriş yapma işlemi testi")
    public void testLoginProcess() {
        System.out.println("\n📋 Test: Giriş yapma işlemi testi");
        System.out.println("----------------------------------------");
        
        // Giriş sayfasına git
        utils.navigateToLoginPage();
        
        // E-posta girişi
        utils.enterEmail("iloveselfcare@gmail.com");
        utils.clickContinueButton();
        
        System.out.println("⚠️ Robot kontrolü için manuel müdahale gerekiyor!");
        System.out.println("Robot kontrolünü tamamladıktan sonra test devam edecek...");
        
        // Robot kontrolü için 30 saniye bekle
        utils.waitForSeconds(30);
        
        // Şifre girişi
        utils.enterPassword("PS5nxQ8Dfa3HsgV");
        utils.clickSignInButton();
        
        // Giriş başarılı mı kontrol et
        boolean isLoggedIn = utils.isUserLoggedIn();
        assertTrue(isLoggedIn, "Kullanıcı başarıyla giriş yapabilmeli");
        
        System.out.println("✅ Giriş yapma işlemi testi başarılı");
    }

    @Test
    @DisplayName("Amazon login testi (kayıtlı bilgilerle)")
    public void testAmazonLoginWithCredentials() {
        utils.loginWithCredentials();
        assertTrue(utils.isUserLoggedIn(), "Kullanıcı başarıyla giriş yapabilmeli");
    }

    /**
     * Sepetten ilk ürünü sil
     */
    public void removeFirstItemFromCart() {
        System.out.println("🗑️ Sepetten ilk ürünü silme işlemi başlatılıyor...");
        utils.goToCart();
        utils.waitForPageLoad();
        List<WebElement> deleteButtons = driver.findElements(By.xpath("//input[@value='Sil' or @value='Delete']"));
        if (!deleteButtons.isEmpty()) {
            int beforeCount = utils.getCartItemCount();
            deleteButtons.get(0).click();
            utils.waitForPageLoad();
            int afterCount = utils.getCartItemCount();
            assertTrue(afterCount < beforeCount, "Sepetteki ürün sayısı azalmış olmalı");
            System.out.println("✅ İlk ürün sepetten silindi");
        } else {
            System.out.println("❌ Sepette silinecek ürün bulunamadı");
        }
    }

    @Test
    @DisplayName("Sepete ekle, kontrol et ve ürünü kaldır")
    public void testAddAndRemoveFromCart() {
        // 1. Ürün ara ve ilk ürünü seç
        utils.searchProduct("laptop");
        boolean productClicked = utils.clickFirstProduct();
        assertTrue(productClicked, "İlk ürün seçilebilmeli");

        // 2. Sepete ekle
        boolean added = utils.addToCart();
        assertTrue(added, "Ürün sepete eklenebilmeli");

        // 3. Sepete git ve ürünün sepette olduğunu kontrol et
        boolean cartOpened = utils.goToCart();
        assertTrue(cartOpened, "Sepet açılabilmeli");
        int beforeCount = utils.getCartItemCount();
        assertTrue(beforeCount > 0, "Sepette en az bir ürün olmalı");

        // 4. Sepetten ürünü kaldır
        boolean removed = utils.removeFromCart();
        assertTrue(removed, "Ürün sepetten silinebilmeli");

        // 5. Sepetin güncellendiğini kontrol et
        int afterCount = utils.getCartItemCount();
        assertTrue(afterCount < beforeCount, "Sepetteki ürün sayısı azalmış olmalı");
    }

    @AfterAll
    public static void teardown() {
        System.out.println("\n🔒 Gelişmiş testler tamamlanıyor...");
        System.out.println("==============================================");
        if (utils != null) {
            utils.closeBrowser();
        }
        System.out.println("🎉 Tüm gelişmiş testler tamamlandı!");
        System.out.println("==============================================");
    }
} 