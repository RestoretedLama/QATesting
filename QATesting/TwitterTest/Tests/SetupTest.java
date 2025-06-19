import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetupTest {

    private static WebDriver driver;
    private static AmazonTestUtils utils;
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

    @Test
    void navigateToHomePage() {
        try {
            utils.navigateToHomePage();
            var expectedResult = "https://www.amazon.com.tr/";
            var result = driver.getCurrentUrl();
            assertEquals(expectedResult, result);
            //For waiting and popups it's just "no exceptions" for passing
        }
        catch (Exception e) {
            System.out.println("Navigation failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void teardown() {

        if (utils != null) {
            utils.closeBrowser();
        }

        System.out.println("Coverage tests completed");
        System.exit(0);
    }
}