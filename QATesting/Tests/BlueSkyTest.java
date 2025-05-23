import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class BlueSkyTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Equivalence Class 1: Geçerli URL + 3 Scroll
    @Test
    public void testValidScenario() {
        executeTestFlow("https://bsky.app/?ref=buffer.com", 3, true);
    }

    // Equivalence Class 2: Geçersiz URL
    @Test
    public void testInvalidUrl() {
        executeTestFlow("https://wrong-url.example.com", 3, false);
    }

    // Boundary Value 1: 0 Scroll (Minimum)
    @Test
    public void testZeroScroll() {
        executeTestFlow("https://bsky.app/?ref=buffer.com", 0, false);
    }

    // Boundary Value 2: 10 Scroll (Pratik Maksimum)
    @Test
    public void testMaxScroll() {
        executeTestFlow("https://bsky.app/?ref=buffer.com", 10, true);
    }

    private void executeTestFlow(String url, int scrollCount, boolean expectedResult) {
        try {
            driver.get(url);
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            performScrolls(scrollCount);
            assertEquals(expectedResult, verifyTitleAndContent());
        } catch (WebDriverException e) {
            if(!expectedResult) {
                assertTrue(true); // Beklenen hata
            } else {
                fail("Beklenmeyen hata: " + e.getMessage());
            }
        }
    }

    private void performScrolls(int count) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for(int i = 0; i < count; i++) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            wait.until(ExpectedConditions.jsReturnsValue(
                    "return window.pageYOffset + window.innerHeight >= document.body.scrollHeight - 10"));
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
    }

    private boolean verifyTitleAndContent() {
        return driver.getTitle().toLowerCase().contains("bluesky") &&
                !driver.findElements(By.cssSelector(".post-container")).isEmpty();
    }

    @AfterAll
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}