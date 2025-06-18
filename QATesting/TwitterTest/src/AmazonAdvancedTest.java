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

    //Farklı ürünler için arama testi
    @ParameterizedTest
    @ValueSource(strings = {"laptop", "telefon", "kitap", "ayakkabı", "kulaklık"})
    @DisplayName("Farklı ürünler için arama testi")
    public void testProductSearchWithDifferentTerms(String searchTerm) {
        System.out.println("\n'" + searchTerm + "' arama testi");
        System.out.println("----------------------------------------");
        
        utils.searchProduct(searchTerm);
        
        List<WebElement> searchResults = utils.getSearchResults();
        assertFalse(searchResults.isEmpty(), searchTerm + " için arama sonuçları görünmeli");
        
        String pageTitle = utils.getPageTitle();
        assertTrue(pageTitle.toLowerCase().contains(searchTerm.toLowerCase()) || 
                  pageTitle.contains("Amazon"), 
                  "Sayfa başlığı arama terimini veya Amazon'u içermeli");
        
        System.out.println("'" + searchTerm + "' arama testi başarılı - " + searchResults.size() + " sonuç");
    }

    // farklı kategoriler için navigasyon testi
    @ParameterizedTest
    @CsvSource({
        "Elektronik, true",
        "Kitap, true", 
        "Moda, true",
        "Spor, true",
        "Ev ve Yaşam, true"
    })
    @DisplayName("Kategori navigasyonu testi")
    public void testCategoryNavigation(String categoryName, boolean expectedResult) {
        System.out.println("\n'" + categoryName + "' kategori testi");
        System.out.println("----------------------------------------");
        
        utils.openCategoryMenu();
        
        boolean result = utils.navigateToCategory(categoryName);
        assertEquals(expectedResult, result, categoryName + " kategorisine navigasyon " + 
                    (expectedResult ? "başarılı" : "başarısız") + " olmalı");
        
        if (result) {
            String pageTitle = utils.getPageTitle();
            assertTrue(pageTitle.contains(categoryName) || pageTitle.contains("Amazon"), 
                      "Kategori sayfası doğru şekilde yüklenmeli");
            System.out.println("'" + categoryName + "' kategori testi başarılı");
        } else {
            System.out.println("'" + categoryName + "' kategori testi başarısız");
        }
    }

    @Test
    @DisplayName("Ürün detay sayfası bilgileri testi")
    public void testProductDetailInformation() {
        System.out.println("\nÜrün detay sayfası bilgileri testi");
        System.out.println("----------------------------------------");
        
        utils.searchProduct("laptop");
        utils.clickFirstProduct();

        String productTitle = utils.getProductTitle();
        assertNotNull(productTitle, "Ürün başlığı null olmamalı");
        assertFalse(productTitle.trim().isEmpty(), "Ürün başlığı boş olmamalı");

        String productPrice = utils.getProductPrice();
        assertNotNull(productPrice, "Ürün fiyatı null olmamalı");

        String currentUrl = utils.getCurrentUrl();
        assertTrue(currentUrl.contains("amazon.com.tr"), "URL Amazon Turkey içermeli");
        
        System.out.println("Ürün: " + productTitle);
        System.out.println("Fiyat: " + productPrice);
        System.out.println("URL: " + currentUrl);
        System.out.println("Ürün detay bilgileri testi başarılı");
    }

    @Test
    @DisplayName("Sepete ekleme işlemi testi")
    public void testAddToCartProcess() {
        System.out.println("\nTest: Sepete ekleme işlemi testi");
        System.out.println("----------------------------------------");
        
        utils.searchProduct("kitap");
        utils.clickFirstProduct();
        
        boolean addedToCart = utils.addToCartAdvanced();
        assertTrue(addedToCart, "Ürün sepete eklenebilmeli");

        int cartCount = utils.getCartItemCount();
        System.out.println("🛒 Sepetteki ürün sayısı: " + cartCount);
        
        System.out.println("Sepete ekleme işlemi testi başarılı");
    }

    @Test
    @DisplayName("Sepete gitme ve ürün kontrolü testi")
    public void testGoToCartAndCheckItems() {
        System.out.println("\nTest: Sepete gitme ve ürün kontrolü testi");
        System.out.println("----------------------------------------");
        
        // ürün ekle
        utils.searchProduct("kalem");
        utils.clickFirstProduct();
        utils.addToCartAdvanced();
        
        // Sepete gitmek
        boolean cartOpened = utils.goToCart();

        assertTrue(cartOpened, "Sepet sayfası açılabilmeli");
        
        // Sepetteki ürünleri listele
        List<WebElement> cartItems = utils.getCartItems();
        System.out.println("Sepetteki ürün sayısı: " + cartItems.size());
        
        if (!cartItems.isEmpty()) {
            System.out.println("Sepete gitme ve ürün kontrolü testi başarılı");
        } else {
            System.out.println("Sepet boş görünüyor????");
        }
    }

    @Test
    @DisplayName("Filtreleme işlemi testi")
    public void testFilteringProcess() {
        System.out.println("\nTest: Filtreleme işlemi testi");
        System.out.println("----------------------------------------");
        
        utils.searchProduct("telefon");
        
        List<WebElement> filterOptions = utils.getFilterOptions();
        if (!filterOptions.isEmpty()) {
            System.out.println("filtre sayısı: " + filterOptions.size());
            
            boolean filterApplied = utils.applyFilter(0);
            assertTrue(filterApplied, "İlk filtre uygulanabilmeli");

            List<WebElement> filteredResults = utils.getSearchResults();
            assertFalse(filteredResults.isEmpty(), "Filtrelenmiş sonuçlar görünür olmalı");
            
            System.out.println("Filtreleme işlemi testi başarılı - " + filteredResults.size() + " filtrelenmiş sonuç");
        } else {
            System.out.println("Filtreleme seçenekleri bulunamadı");
        }
    }

    @ParameterizedTest
    @CsvSource({
        "375, 667, iPhone",
        "768, 1024, iPad", 
        "1920, 1080, Desktop"
    })
    @DisplayName("Responsive tasarım testi")
    public void testResponsiveDesign(int width, int height, String device) {
        System.out.println("\nParameterized Test: " + device + " responsive tasarım testi");
        System.out.println("----------------------------------------");
        
        utils.setWindowSize(width, height);
        
        // Temel elementlerin görünür olduğunu kontrol et
        assertTrue(utils.isElementVisible(By.id("nav-logo-sprites")), 
                  device + " için Amazon logosu görünür olmalı");
        assertTrue(utils.isElementVisible(By.id("nav-search")), 
                  device + " için arama kutusu görünür olmalı");
        
        // Mobil görünümde hamburger menünün görünür olduğunu kontrol et
        if (width <= 768) {
            assertTrue(utils.isElementVisible(By.id("nav-hamburger-menu")), 
                      device + " için hamburger menü görünür olmalı");
            System.out.println(device + " için mobil menü görünür");
        }
        
        utils.maximizeWindow();
        System.out.println( device + " responsive tasarım testi başarılı");
    }

    // PERFORMANS testi
    @Test
    @DisplayName("Performans testi")
    public void testPerformance() {
        System.out.println("\ntest: Performans testi");
        System.out.println("----------------------------------------");
        
        long loadTime = utils.measurePageLoadTime();
        
        // Sayfa yükleme süresinin ok olduğunu kontrol et
        assertTrue(loadTime < 10000, "Sayfa 10 saniyeden kısa sürede yüklenmeli. Süre: " + loadTime + "ms");
        
        System.out.println("Ana sayfa yükleme süresi: " + loadTime + "ms");
        System.out.println("Performans testi başarılı oh ");
    }

    //Çoklu sekme işlemleri testi
    @Test
    @DisplayName("Çoklu sekme işlemleri testi")
    public void testMultipleTabs() {
        System.out.println("\nest: Çoklu sekme işlemleri testi");
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
        
        // Yeni sekmeyi kapat byü
        utils.closeCurrentTab();
        utils.switchToTab(0);
        
        System.out.println("çoklu sekme işlemleri testi başarılı");
    }

    @Test
    @DisplayName("Scroll işlemleri testi")
    public void testScrolling() {
        System.out.println("\nTest: Scroll işlemleri testi");
        System.out.println("----------------------------------------");

        utils.scrollToBottom();
        
        // Scroll pozisyonunu kontrol et
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long scrollPosition = (Long) js.executeScript("return window.pageYOffset");
        
        assertTrue(scrollPosition > 0, "Sayfa scroll edilmiş olmalı");
        
        // Sayfanın başına geri dön
        js.executeScript("window.scrollTo(0, 0)");
        
        Long newScrollPosition = (Long) js.executeScript("return window.pageYOffset");
        assertEquals(0, newScrollPosition, "Sayfa başına dönülmüş olmalı");
        
        System.out.println("Scroll pozisyonu: " + scrollPosition + "px");
        System.out.println("Scroll işlemleri testi başarılı");
    }

    @Test
    @DisplayName("Element etkileşimleri testi ")
    public void testElementInteractions() {
        System.out.println("\ntest Element etkileşimleri testi");
        System.out.println("----------------------------------------");
        
        // Arama kutusu tıklanabilir
        assertTrue(utils.isElementClickable(By.id("twotabsearchtextbox")), 
                  "Arama kutusu tıklanabilir olmalı");
        
        // Arama butonu tıklanabilir ?
        assertTrue(utils.isElementClickable(By.id("nav-search-submit-button")), 
                  "Arama butonu tıklanabilir olmalı");
        
        // Sepet ikonu görünür ?
        assertTrue(utils.isElementVisible(By.id("nav-cart")), 
                  "Sepet ikonu görünür olmalı");
        
        System.out.println("Element etkileşimleri testi başarılı");
    }

    @Test
    @DisplayName("URL doğrulama testi")
    public void testUrlValidation() {
        System.out.println("\nTest: URL doğrulama testi");
        System.out.println("----------------------------------------");
        
        String currentUrl = utils.getCurrentUrl();
        
        // URL'nin Amazon Turkey olduğunu kontrol et
        assertTrue(currentUrl.contains("amazon.com.tr"), "URL Amazon Turkey içermeli");
        //güvenli mi?
        assertTrue(currentUrl.startsWith("https://"), "URL HTTPS protokolü kullanmalı");
        
        System.out.println("URL: " + currentUrl);
        System.out.println("URL doğrulama testi başarılı");
    }

    @Test
    @DisplayName("Sayfa başlığı doğrulama testi")
    public void testPageTitleValidation() {
        System.out.println("\nTest: Sayfa başlığı doğrulama testi");
        System.out.println("----------------------------------------");
        
        String pageTitle = utils.getPageTitle();
        
        // Sayfa başlığı geçerli ?
        assertNotNull(pageTitle, "Sayfa başlığı null olmamalı");
        assertFalse(pageTitle.trim().isEmpty(), "Sayfa başlığı boş olmamalı");
        assertTrue(pageTitle.contains("Amazon") || pageTitle.contains("amazon"), 
                  "Sayfa başlığı Amazon içermeli");
        
        System.out.println("Sayfa başlığı: " + pageTitle);
        System.out.println("Sayfa başlığı doğrulama testi başarılı");
    }

    @Test
    @DisplayName("Giriş yapma işlemi testi")
    public void testLoginProcess() {
        System.out.println("\ntest: Giriş yapma işlemi testi");
        System.out.println("----------------------------------------");
        
        // Giriş sayfasına git
        utils.navigateToLoginPage();
        
        // E-posta girişi
        utils.enterEmail("your-email@example.com");
        utils.clickContinueButton();
        
        System.out.println("Robot kontrolü için gellll!!!!!!!!!");
        
        // Robot kontrolü için bekle
        utils.waitForSeconds(30);
        
        // Şifre girişi
        utils.enterPassword("your-password");
        utils.clickSignInButton();
        
        // Giriş başarılı mı kontrol et!
        boolean isLoggedIn = utils.isUserLoggedIn();
        assertTrue(isLoggedIn, "Kullanıcı başarıyla giriş yapabilmeli");
        
        System.out.println("Giriş yapma işlemi testi başarılı");
    }

    @AfterAll
    public static void teardown() {
        System.out.println("\nADVANCED testler tamamlanıyor...");
        System.out.println("==============================================");
        
        if (utils != null) {
            utils.closeBrowser();
        }
        
        System.out.println("Tüm illeri gelişmiş testler tamamlandı! ehehe");
        System.out.println("==============================================");
        
        // Programı kapat byü
        System.out.println("Program kapatılıyor...");
        System.exit(0);
    }
} 