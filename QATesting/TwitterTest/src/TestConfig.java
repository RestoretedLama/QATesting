public class TestConfig {

    public static final String AMAZON_TURKEY_URL = "https://www.amazon.com.tr/";
    public static final String AMAZON_SEARCH_URL = "https://www.amazon.com.tr/s?k=";

    public static final String[] SEARCH_TERMS = {
        "laptop", "phone", "book", "shoes", "headphones", "tablet", "monitor"
    };
    public static final String[] CATEGORIES = {
        "Electronics", "Books", "Fashion", "Sports", "Home and Living", "Toys and Games"
    };

    public static final int DEFAULT_WAIT_TIMEOUT = 20; // saniye
    public static final int PAGE_LOAD_TIMEOUT = 10; // saniye
    public static final int IMPLICIT_WAIT = 5; // saniye

    public static final long MAX_PAGE_LOAD_TIME = 10000; // milisaniye
    public static final long MAX_ELEMENT_WAIT_TIME = 5000; // milisaniye
    

    public static final String[] CHROME_ARGUMENTS = {
        "--start-maximized",
        "--disable-blink-features=AutomationControlled",
        "--disable-extensions",
        "--no-sandbox",
        "--disable-dev-shm-usage",
        "--disable-web-security",
        "--allow-running-insecure-content"
    };
    

    public static final int[][] RESPONSIVE_SIZES = {
        {375, 667},
        {768, 1024},
        {1024, 768},
        {1920, 1080},
        {1366, 768}
    };
    

    public static final String[] RESPONSIVE_DEVICE_NAMES = {
        "iPhone",
        "iPad",
        "Tablet Landscape",
        "Desktop",
        "Laptop"
    };
    

    public static final String SEARCH_BOX_ID = "twotabsearchtextbox";
    public static final String SEARCH_BUTTON_ID = "nav-search-submit-button";
    public static final String CART_BUTTON_ID = "nav-cart";
    public static final String LOGO_ID = "nav-logo-sprites";
    public static final String HAMBURGER_MENU_ID = "nav-hamburger-menu";
    public static final String PRODUCT_TITLE_ID = "productTitle";
    public static final String ADD_TO_CART_BUTTON_ID = "add-to-cart-button";

    public static final String SEARCH_RESULTS_SELECTOR = "[data-component-type='s-search-results'] .s-result-item";
    public static final String PRODUCT_LINK_SELECTOR = "[data-component-type='s-search-results'] .s-result-item h2 a";
    public static final String PRICE_SELECTOR = ".a-price-whole";
    public static final String FILTER_SELECTOR = "#departments .a-spacing-micro";
    

    public static final boolean GENERATE_DETAILED_REPORTS = true;
    public static final boolean SCREENSHOT_ON_FAILURE = true;
    public static final String SCREENSHOT_DIRECTORY = "test-screenshots/";
    

    public static final String ELEMENT_NOT_FOUND = "Element not found: ";
    public static final String PAGE_LOAD_TIMEOUT_MSG = "Page load timeout";
    public static final String ELEMENT_NOT_CLICKABLE = "Element not clickable: ";
    public static final String PERFORMANCE_THRESHOLD_EXCEEDED = "Performance threshold exceeded";
    

    public static boolean isTestEnvironmentReady() {
        // İnternet bağlantısı kontrolü (basit)
        try {
            java.net.InetAddress.getByName("www.google.com");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    

    public static void printTestConfiguration() {
        System.out.println("=== Test Configuration ===");
        System.out.println("Target URL: " + AMAZON_TURKEY_URL);
        System.out.println("Wait Timeout: " + DEFAULT_WAIT_TIMEOUT + " seconds");
        System.out.println("Page Load Timeout: " + PAGE_LOAD_TIMEOUT + " seconds");
        System.out.println("Max Page Load Time: " + MAX_PAGE_LOAD_TIME + " ms");
        System.out.println("Search Terms: " + String.join(", ", SEARCH_TERMS));
        System.out.println("Categories: " + String.join(", ", CATEGORIES));
        System.out.println("Responsive Sizes: " + RESPONSIVE_SIZES.length + " different sizes");
        System.out.println("Detailed Reports: " + (GENERATE_DETAILED_REPORTS ? "Enabled" : "Disabled"));
        System.out.println("Screenshot on Failure: " + (SCREENSHOT_ON_FAILURE ? "Enabled" : "Disabled"));
        System.out.println("================================");
    }

    public static void validateTestEnvironment() {
        System.out.println("Validating test environment...");
        
        if (!isTestEnvironmentReady()) {
            throw new RuntimeException("Internet connection not found!");
        }
        
        // Java versiyonu kontrolü
        String javaVersion = System.getProperty("java.version");
        System.out.println("Java Version: " + javaVersion);
        
        // İşletim sistemi bilgisi
        String osName = System.getProperty("os.name");
        System.out.println("Operating System: " + osName);
        
        System.out.println("Test environment ready!");
    }
} 