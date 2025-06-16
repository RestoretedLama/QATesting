public class TestConfig {
    
    // Test URL'leri
    public static final String AMAZON_TURKEY_URL = "https://www.amazon.com.tr/";
    public static final String AMAZON_SEARCH_URL = "https://www.amazon.com.tr/s?k=";
    
    // Test verileri
    public static final String[] SEARCH_TERMS = {
        "laptop", "telefon", "kitap", "ayakkabı", "kulaklık", "tablet", "monitor"
    };
    
    public static final String[] CATEGORIES = {
        "Elektronik", "Kitap", "Moda", "Spor", "Ev ve Yaşam", "Oyuncaklar ve Oyunlar"
    };
    
    // Zaman ayarları
    public static final int DEFAULT_WAIT_TIMEOUT = 20; // saniye
    public static final int PAGE_LOAD_TIMEOUT = 10; // saniye
    public static final int IMPLICIT_WAIT = 5; // saniye
    
    // Performans eşikleri
    public static final long MAX_PAGE_LOAD_TIME = 10000; // milisaniye
    public static final long MAX_ELEMENT_WAIT_TIME = 5000; // milisaniye
    
    // Tarayıcı ayarları
    public static final String[] CHROME_ARGUMENTS = {
        "--start-maximized",
        "--disable-blink-features=AutomationControlled",
        "--disable-extensions",
        "--no-sandbox",
        "--disable-dev-shm-usage",
        "--disable-web-security",
        "--allow-running-insecure-content"
    };
    
    // Responsive test boyutları
    public static final int[][] RESPONSIVE_SIZES = {
        {375, 667},
        {768, 1024},
        {1024, 768},
        {1920, 1080},
        {1366, 768}
    };
    
    // Responsive test cihaz isimleri
    public static final String[] RESPONSIVE_DEVICE_NAMES = {
        "iPhone",
        "iPad",
        "Tablet Landscape",
        "Desktop",
        "Laptop"
    };
    
    // Element seçicileri
    public static final String SEARCH_BOX_ID = "twotabsearchtextbox";
    public static final String SEARCH_BUTTON_ID = "nav-search-submit-button";
    public static final String CART_BUTTON_ID = "nav-cart";
    public static final String LOGO_ID = "nav-logo-sprites";
    public static final String HAMBURGER_MENU_ID = "nav-hamburger-menu";
    public static final String PRODUCT_TITLE_ID = "productTitle";
    public static final String ADD_TO_CART_BUTTON_ID = "add-to-cart-button";
    
    // CSS seçicileri
    public static final String SEARCH_RESULTS_SELECTOR = "[data-component-type='s-search-results'] .s-result-item";
    public static final String PRODUCT_LINK_SELECTOR = "[data-component-type='s-search-results'] .s-result-item h2 a";
    public static final String PRICE_SELECTOR = ".a-price-whole";
    public static final String FILTER_SELECTOR = "#departments .a-spacing-micro";
    
    // Test raporu ayarları
    public static final boolean GENERATE_DETAILED_REPORTS = true;
    public static final boolean SCREENSHOT_ON_FAILURE = true;
    public static final String SCREENSHOT_DIRECTORY = "test-screenshots/";
    
    // Hata mesajları
    public static final String ELEMENT_NOT_FOUND = "Element bulunamadı: ";
    public static final String PAGE_LOAD_TIMEOUT_MSG = "Sayfa yükleme zaman aşımı";
    public static final String ELEMENT_NOT_CLICKABLE = "Element tıklanamıyor: ";
    public static final String PERFORMANCE_THRESHOLD_EXCEEDED = "Performans eşiği aşıldı";
    
    /**
     * Test ortamını kontrol et
     */
    public static boolean isTestEnvironmentReady() {
        // İnternet bağlantısı kontrolü (basit)
        try {
            java.net.InetAddress.getByName("www.google.com");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Test ayarlarını yazdır
     */
    public static void printTestConfiguration() {
        System.out.println("=== Test Konfigürasyonu ===");
        System.out.println("Hedef URL: " + AMAZON_TURKEY_URL);
        System.out.println("Bekleme Süresi: " + DEFAULT_WAIT_TIMEOUT + " saniye");
        System.out.println("Sayfa Yükleme Süresi: " + PAGE_LOAD_TIMEOUT + " saniye");
        System.out.println("Maksimum Sayfa Yükleme Süresi: " + MAX_PAGE_LOAD_TIME + " ms");
        System.out.println("Arama Terimleri: " + String.join(", ", SEARCH_TERMS));
        System.out.println("Kategoriler: " + String.join(", ", CATEGORIES));
        System.out.println("Responsive Boyutlar: " + RESPONSIVE_SIZES.length + " farklı boyut");
        System.out.println("Detaylı Raporlar: " + (GENERATE_DETAILED_REPORTS ? "Açık" : "Kapalı"));
        System.out.println("Hata Ekran Görüntüsü: " + (SCREENSHOT_ON_FAILURE ? "Açık" : "Kapalı"));
        System.out.println("================================");
    }
    
    /**
     * Test ortamını doğrula
     */
    public static void validateTestEnvironment() {
        System.out.println("Test ortamı doğrulanıyor...");
        
        if (!isTestEnvironmentReady()) {
            throw new RuntimeException("İnternet bağlantısı bulunamadı!");
        }
        
        // Java versiyonu kontrolü
        String javaVersion = System.getProperty("java.version");
        System.out.println("Java Versiyonu: " + javaVersion);
        
        // İşletim sistemi bilgisi
        String osName = System.getProperty("os.name");
        System.out.println("İşletim Sistemi: " + osName);
        
        System.out.println("Test ortamı hazır!");
    }
} 