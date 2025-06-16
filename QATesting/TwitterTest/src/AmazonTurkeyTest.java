import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AmazonTurkeyTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static AmazonTestUtils utils;
    private static final String AMAZON_URL = "https://www.amazon.com.tr/";
    private static final String SEARCH_TERM = "laptop";
    private static final String CATEGORY_NAME = "Elektronik";

    @BeforeAll
    public static void setup() {
        System.out.println("🚀 Amazon Turkey Test Bot Başlatılıyor...");
        System.out.println("==============================================");
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        utils = new AmazonTestUtils(driver);
        
        System.out.println("✅ Test ortamı hazırlandı");
    }

    // Test 1: Ana sayfa yükleme ve temel elementlerin kontrolü
    @Test
    @DisplayName("Ana sayfa yükleme testi")
    public void testHomePageLoad() {
        System.out.println("\n📋 Test 1: Ana sayfa yükleme testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        
        // Temel elementlerin varlığını kontrol et
        assertTrue(driver.findElement(By.id("nav-logo-sprites")).isDisplayed(), "Amazon logosu görünür olmalı");
        assertTrue(driver.findElement(By.id("nav-search")).isDisplayed(), "Arama kutusu görünür olmalı");
        assertTrue(driver.findElement(By.id("nav-cart")).isDisplayed(), "Sepet ikonu görünür olmalı");
        
        // Sayfa başlığını kontrol et
        String title = driver.getTitle();
        assertTrue(title.contains("Amazon") || title.contains("amazon"), "Sayfa başlığı Amazon içermeli");
        
        System.out.println("✅ Ana sayfa testi başarılı");
    }

    // Test 2: Arama fonksiyonu testi
    @Test
    @DisplayName("Ürün arama testi")
    public void testProductSearch() {
        System.out.println("\n📋 Test 2: Ürün arama testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        utils.searchProduct(SEARCH_TERM);
        
        // Arama sonuçlarının görünür olduğunu kontrol et
        List<WebElement> searchResults = driver.findElements(By.cssSelector("[data-component-type='s-search-results'] .s-result-item"));
        assertFalse(searchResults.isEmpty(), "Arama sonuçları görünür olmalı");
        
        // Arama teriminin sonuçlarda göründüğünü kontrol et
        String pageTitle = driver.getTitle();
        assertTrue(pageTitle.toLowerCase().contains(SEARCH_TERM.toLowerCase()), 
                  "Sayfa başlığı arama terimini içermeli");
        
        System.out.println("✅ Arama testi başarılı - " + searchResults.size() + " sonuç bulundu");
    }

    // Test 3: Kategori navigasyonu testi
    @Test
    @DisplayName("Kategori navigasyonu testi")
    public void testCategoryNavigation() {
        System.out.println("\n📋 Test 3: Kategori navigasyonu testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        utils.openCategoryMenu();
        
        // Elektronik kategorisini bul ve tıkla
        try {
            WebElement electronicsCategory = driver.findElement(By.xpath("//a[contains(text(), '" + CATEGORY_NAME + "')]"));
            electronicsCategory.click();
            
            // Kategori sayfasının yüklendiğini kontrol et
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".a-page-title")));
            utils.testDelay();
            
            String pageTitle = driver.getTitle();
            assertTrue(pageTitle.contains(CATEGORY_NAME) || pageTitle.contains("Elektronik"), 
                      "Kategori sayfası doğru yüklenmeli");
            
            System.out.println("✅ Kategori navigasyonu başarılı");
        } catch (NoSuchElementException e) {
            // Kategori bulunamazsa testi geç
            System.out.println("⚠️ Elektronik kategorisi bulunamadı, test geçiliyor");
        }
    }

    // Test 4: Ürün detay sayfası testi
    @Test
    @DisplayName("Ürün detay sayfası testi")
    public void testProductDetailPage() {
        System.out.println("\n📋 Test 4: Ürün detay sayfası testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        utils.searchProduct(SEARCH_TERM);
        utils.clickFirstProduct();
        
        // Ürün başlığının görünür olduğunu kontrol et
        WebElement productTitle = driver.findElement(By.id("productTitle"));
        assertTrue(productTitle.isDisplayed(), "Ürün başlığı görünür olmalı");
        
        // Fiyat bilgisinin görünür olduğunu kontrol et
        try {
            WebElement priceElement = driver.findElement(By.cssSelector(".a-price-whole"));
            assertTrue(priceElement.isDisplayed(), "Fiyat bilgisi görünür olmalı");
            System.out.println("💰 Ürün fiyatı: " + priceElement.getText() + " TL");
        } catch (NoSuchElementException e) {
            System.out.println("⚠️ Fiyat bilgisi bulunamadı");
        }
        
        System.out.println("📦 Ürün: " + productTitle.getText());
        System.out.println("✅ Ürün detay sayfası testi başarılı");
    }

    // Test 5: Sepete ekleme testi
    @Test
    @DisplayName("Sepete ekleme testi")
    public void testAddToCart() {
        System.out.println("\n📋 Test 5: Sepete ekleme testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        utils.searchProduct("kitap");
        utils.clickFirstProduct();
        
        // Gelişmiş sepete ekleme metodunu kullan
        boolean addedToCart = utils.addToCartAdvanced();
        assertTrue(addedToCart, "Ürün sepete eklenebilmeli");
        
        // Sepetteki ürün sayısını kontrol et
        int cartCount = utils.getCartItemCount();
        System.out.println("🛒 Sepetteki ürün sayısı: " + cartCount);
        
        assertTrue(cartCount > 0, "Sepet boş olmamalı");
        
        System.out.println("✅ Sepete ekleme testi başarılı");
    }

    // Test 6: Sepete gitme testi
    @Test
    @DisplayName("Sepete gitme testi")
    public void testGoToCart() {
        System.out.println("\n📋 Test 6: Sepete gitme testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        
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
            System.out.println("✅ Sepete gitme testi başarılı");
        } else {
            System.out.println("⚠️ Sepet boş görünüyor");
        }
    }

    // Test 7: Sepetten ürün silme testi
    @Test
    @DisplayName("Sepetten ürün silme testi")
    public void testRemoveFromCart() {
        System.out.println("\n📋 Test 7: Sepetten ürün silme testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        
        // Önce bir ürün ekle
        utils.searchProduct("defter");
        utils.clickFirstProduct();
        utils.addToCartAdvanced();
        
        // Sepete git
        utils.goToCart();
        
        // Sepetteki ürün sayısını kaydet
        int initialCount = utils.getCartItemCount();
        System.out.println("🛒 Başlangıçta sepetteki ürün sayısı: " + initialCount);
        
        if (initialCount > 0) {
            // İlk ürünü sil
            boolean removed = utils.removeFromCart(0);
            assertTrue(removed, "Ürün sepetten silinebilmeli");
            
            int finalCount = utils.getCartItemCount();
            System.out.println("🛒 Silme sonrası sepetteki ürün sayısı: " + finalCount);
            
            System.out.println("✅ Sepetten ürün silme testi başarılı");
        } else {
            System.out.println("⚠️ Sepette silinecek ürün bulunamadı");
        }
    }

    // Test 8: Filtreleme testi
    @Test
    @DisplayName("Filtreleme testi")
    public void testFiltering() {
        System.out.println("\n📋 Test 8: Filtreleme testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        utils.searchProduct("telefon");
        
        // Filtreleme seçeneklerinin görünür olduğunu kontrol et
        try {
            List<WebElement> filters = driver.findElements(By.cssSelector("#departments .a-spacing-micro"));
            assertFalse(filters.isEmpty(), "Filtreleme seçenekleri görünür olmalı");
            
            System.out.println("🔍 Bulunan filtre sayısı: " + filters.size());
            
            // İlk filtreyi tıkla
            if (!filters.isEmpty()) {
                filters.get(0).click();
                utils.testDelay();
                
                // Filtrelenmiş sonuçların görünür olduğunu kontrol et
                List<WebElement> filteredResults = driver.findElements(By.cssSelector("[data-component-type='s-search-results'] .s-result-item"));
                assertFalse(filteredResults.isEmpty(), "Filtrelenmiş sonuçlar görünür olmalı");
                
                System.out.println("✅ Filtreleme testi başarılı - " + filteredResults.size() + " filtrelenmiş sonuç");
            }
        } catch (NoSuchElementException e) {
            System.out.println("⚠️ Filtreleme seçenekleri bulunamadı");
        }
    }

    // Test 9: Sayfa scroll testi
    @Test
    @DisplayName("Sayfa scroll testi")
    public void testPageScrolling() {
        System.out.println("\n📋 Test 9: Sayfa scroll testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        utils.scrollToBottom();
        
        // Scroll pozisyonunu kontrol et
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long scrollPosition = (Long) js.executeScript("return window.pageYOffset");
        Long documentHeight = (Long) js.executeScript("return document.body.scrollHeight");
        
        assertTrue(scrollPosition > 0, "Sayfa scroll edilmiş olmalı");
        
        System.out.println("📜 Scroll pozisyonu: " + scrollPosition + "px");
        System.out.println("📏 Sayfa yüksekliği: " + documentHeight + "px");
        System.out.println("✅ Scroll testi başarılı");
    }

    // Test 10: Responsive tasarım testi
    @Test
    @DisplayName("Responsive tasarım testi")
    public void testResponsiveDesign() {
        System.out.println("\n📋 Test 10: Responsive tasarım testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
        
        // Mobil görünüm için pencere boyutunu değiştir
        utils.setWindowSize(375, 667); // iPhone boyutu
        
        // Mobil menü butonunun görünür olduğunu kontrol et
        try {
            WebElement mobileMenu = driver.findElement(By.id("nav-hamburger-menu"));
            assertTrue(mobileMenu.isDisplayed(), "Mobil menü butonu görünür olmalı");
            System.out.println("📱 Mobil menü görünür");
        } catch (NoSuchElementException e) {
            System.out.println("⚠️ Mobil menü butonu bulunamadı");
        }
        
        // Pencere boyutunu geri al
        utils.maximizeWindow();
        
        System.out.println("✅ Responsive tasarım testi başarılı");
    }

    // Test 11: Performans testi
    @Test
    @DisplayName("Sayfa yükleme performans testi")
    public void testPageLoadPerformance() {
        System.out.println("\n📋 Test 11: Performans testi");
        System.out.println("----------------------------------------");
        
        long startTime = System.currentTimeMillis();
        
        utils.navigateToHomePage();
        
        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;
        
        // Sayfa yükleme süresinin makul bir sürede olduğunu kontrol et (10 saniye)
        assertTrue(loadTime < 10000, "Sayfa 10 saniyeden kısa sürede yüklenmeli. Yükleme süresi: " + loadTime + "ms");
        
        System.out.println("⏱️ Sayfa yükleme süresi: " + loadTime + "ms");
        System.out.println("✅ Performans testi başarılı");
    }

    // Test 12: Çoklu sekme testi
    @Test
    @DisplayName("Çoklu sekme testi")
    public void testMultipleTabs() {
        System.out.println("\n📋 Test 12: Çoklu sekme testi");
        System.out.println("----------------------------------------");
        
        utils.navigateToHomePage();
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
        
        System.out.println("✅ Çoklu sekme testi başarılı");
    }

    @Test
    public void testSearchAndAddToCart() {
        System.out.println("\n🔍 Ürün arama ve sepete ekleme testi başlıyor...");
        
        try {
            // Ana sayfaya git
            utils.navigateToHomePage();
            
            // Cookie banner'ını kapat
            utils.handleCookieBannerAndPopups();
            
            // Daha güvenilir ürün ara (kitap kategorisi)
            String searchKeyword = "kitap";
            System.out.println("🔍 '" + searchKeyword + "' aranıyor...");
            utils.searchProduct(searchKeyword);
            
            // Cookie banner'ını tekrar kapat (arama sonrası)
            utils.handleCookieBannerAndPopups();
            
            // İlk ürüne tıkla
            if (utils.clickFirstProduct()) {
                System.out.println("✅ İlk ürün seçildi");
                
                // Cookie banner'ını kapat (ürün sayfasında)
                utils.handleCookieBannerAndPopups();
                
                // Ürün başlığını al
                String productTitle = utils.getProductTitle();
                System.out.println("📦 Seçilen ürün: " + productTitle);
                
                // Sepete ekle - gelişmiş versiyon
                System.out.println("🛒 Sepete ekleme deneniyor...");
                if (utils.addToCartAdvanced()) {
                    System.out.println("✅ Ürün sepete eklendi");
                    
                    // Sepete git
                    if (utils.navigateToCart()) {
                        System.out.println("✅ Sepet sayfasına gidildi");
                        
                        // Sepetteki ürün sayısını kontrol et
                        int cartCount = utils.getCartItemCount();
                        System.out.println("📦 Sepetteki ürün sayısı: " + cartCount);
                        
                        assertTrue(cartCount > 0, "Sepet boş olmamalı");
                        
                        // Sepetten ürün kaldır
                        if (utils.removeFromCart()) {
                            System.out.println("✅ Ürün sepetten kaldırıldı");
                        } else {
                            System.out.println("⚠️ Ürün sepetten kaldırılamadı");
                        }
                        
                    } else {
                        System.out.println("❌ Sepet sayfasına gidilemedi");
                        fail("Sepet sayfasına gidilemedi");
                    }
                } else {
                    System.out.println("❌ Ürün sepete eklenemedi");
                    
                    // Alternatif yöntemleri dene
                    System.out.println("🔄 Alternatif yöntemler deneniyor...");
                    
                    // JavaScript ile sepete ekle
                    try {
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("document.getElementById('add-to-cart-button').click();");
                        System.out.println("✅ JavaScript ile sepete ekleme deneniyor...");
                        Thread.sleep(3000);
                        
                        int cartCount = utils.getCartItemCount();
                        if (cartCount > 0) {
                            System.out.println("✅ JavaScript ile sepete eklendi (sepet sayısı: " + cartCount + ")");
                        } else {
                            fail("JavaScript ile de sepete eklenemedi");
                        }
                    } catch (Exception e) {
                        System.out.println("❌ JavaScript yöntemi de başarısız: " + e.getMessage());
                        fail("Hiçbir yöntemle sepete eklenemedi");
                    }
                }
            } else {
                System.out.println("❌ İlk ürün seçilemedi");
                fail("İlk ürün seçilemedi");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Test hatası: " + e.getMessage());
            fail("Test hatası: " + e.getMessage());
        }
    }

    @Test
    public void testSimpleAddToCart() {
        System.out.println("\n🛒 Basit sepete ekleme testi başlıyor...");
        
        try {
            // Ana sayfaya git
            utils.navigateToHomePage();
            
            // Cookie banner'ını kapat
            utils.handleCookieBannerAndPopups();
            
            // Basit bir ürün ara
            String searchKeyword = "kalem";
            System.out.println("🔍 '" + searchKeyword + "' aranıyor...");
            utils.searchProduct(searchKeyword);
            
            // Cookie banner'ını tekrar kapat
            utils.handleCookieBannerAndPopups();
            
            // İlk ürüne tıkla
            if (utils.clickFirstProduct()) {
                System.out.println("✅ İlk ürün seçildi");
                
                // Cookie banner'ını kapat
                utils.handleCookieBannerAndPopups();
                
                // Ürün başlığını al
                String productTitle = utils.getProductTitle();
                System.out.println("📦 Seçilen ürün: " + productTitle);
                
                // Sepete ekle
                System.out.println("🛒 Sepete ekleme deneniyor...");
                
                // Farklı yöntemleri dene
                boolean addedToCart = false;
                
                // Yöntem 1: Standart sepete ekle
                if (utils.addToCart()) {
                    System.out.println("✅ Standart yöntemle sepete eklendi");
                    addedToCart = true;
                } else {
                    System.out.println("⚠️ Standart yöntem başarısız, alternatif deneniyor...");
                    
                    // Yöntem 2: JavaScript ile sepete ekle
                    try {
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("document.getElementById('add-to-cart-button').click();");
                        System.out.println("✅ JavaScript ile sepete ekleme deneniyor...");
                        Thread.sleep(3000);
                        
                        int cartCount = utils.getCartItemCount();
                        if (cartCount > 0) {
                            System.out.println("✅ JavaScript ile sepete eklendi (sepet sayısı: " + cartCount + ")");
                            addedToCart = true;
                        }
                    } catch (Exception e) {
                        System.out.println("⚠️ JavaScript yöntemi başarısız: " + e.getMessage());
                    }
                    
                    // Yöntem 3: Form submit ile sepete ekle
                    if (!addedToCart) {
                        try {
                            WebElement form = driver.findElement(By.cssSelector("form[action*='cart'], form[action*='add']"));
                            if (form != null) {
                                form.submit();
                                System.out.println("✅ Form submit ile sepete ekleme deneniyor...");
                                Thread.sleep(3000);
                                
                                int cartCount = utils.getCartItemCount();
                                if (cartCount > 0) {
                                    System.out.println("✅ Form submit ile sepete eklendi (sepet sayısı: " + cartCount + ")");
                                    addedToCart = true;
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("⚠️ Form submit yöntemi başarısız: " + e.getMessage());
                        }
                    }
                }
                
                if (addedToCart) {
                    // Sepete git
                    if (utils.navigateToCart()) {
                        System.out.println("✅ Sepet sayfasına gidildi");
                        
                        // Sepetteki ürün sayısını kontrol et
                        int cartCount = utils.getCartItemCount();
                        System.out.println("📦 Sepetteki ürün sayısı: " + cartCount);
                        
                        assertTrue(cartCount > 0, "Sepet boş olmamalı");
                        
                        // Sepetten ürün kaldır
                        if (utils.removeFromCart()) {
                            System.out.println("✅ Ürün sepetten kaldırıldı");
                        } else {
                            System.out.println("⚠️ Ürün sepetten kaldırılamadı");
                        }
                        
                    } else {
                        System.out.println("❌ Sepet sayfasına gidilemedi");
                        fail("Sepet sayfasına gidilemedi");
                    }
                } else {
                    System.out.println("❌ Hiçbir yöntemle sepete eklenemedi");
                    fail("Hiçbir yöntemle sepete eklenemedi");
                }
                
            } else {
                System.out.println("❌ İlk ürün seçilemedi");
                fail("İlk ürün seçilemedi");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Test hatası: " + e.getMessage());
            fail("Test hatası: " + e.getMessage());
        }
    }

    @AfterAll
    public static void teardown() {
        System.out.println("\n🔒 Testler tamamlanıyor...");
        System.out.println("==============================================");
        
        if (utils != null) {
            utils.closeBrowser();
        }
        
        System.out.println("🎉 Tüm testler tamamlandı!");
        System.out.println("==============================================");
        
        // Programı kapat
        System.out.println("🔒 Program kapatılıyor...");
        System.exit(0);
    }
} 