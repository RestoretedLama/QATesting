import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.engine.Filter;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("Amazon Turkey Website Test Bot Başlatılıyor...");
        System.out.println("==============================================");
        
        // Test sınıflarını çalıştır
        try {
            // runTestClass(Class.forName("AmazonTurkeyTest")); // Artık yok, kaldırıldı
            runTestClass(Class.forName("AmazonAdvancedTest"));
        } catch (ClassNotFoundException e) {
            System.err.println("Test sınıfları bulunamadı: " + e.getMessage());
            System.err.println("Lütfen test sınıflarının doğru konumda olduğunu kontrol edin.");
        }
        
        System.out.println("\nTüm testler tamamlandı!");
    }
    
    private static void runTestClass(Class<?> testClass) {
        System.out.println("\n" + testClass.getSimpleName() + " sınıfı çalıştırılıyor...");
        
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(selectClass(testClass))
                .build();
        
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        
        launcher.execute(request);
        
        TestExecutionSummary summary = listener.getSummary();
        
        System.out.println("Test Sonuçları:");
        System.out.println("- Toplam test sayısı: " + summary.getTestsFoundCount());
        System.out.println("- Başarılı testler: " + summary.getTestsSucceededCount());
        System.out.println("- Başarısız testler: " + summary.getTestsFailedCount());
        System.out.println("- Atlanan testler: " + summary.getTestsSkippedCount());
        
        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\nBaşarısız testler:");
            summary.getFailures().forEach(failure -> {
                System.out.println("- " + failure.getTestIdentifier().getDisplayName());
                System.out.println("  Hata: " + failure.getException().getMessage());
            });
        }
    }
    
    /**
     * Belirli bir test metodunu çalıştır
     */
    public static void runSpecificTest(Class<?> testClass, String testMethodName) {
        System.out.println("\n" + testClass.getSimpleName() + "." + testMethodName + " çalıştırılıyor...");
        
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(selectMethod(testClass, testMethodName))
                .build();
        
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        
        launcher.execute(request);
        
        TestExecutionSummary summary = listener.getSummary();
        System.out.println("Test tamamlandı. Başarılı: " + summary.getTestsSucceededCount() + 
                          ", Başarısız: " + summary.getTestsFailedCount());
    }
    
    /**
     * Tüm testleri çalıştır ve rapor oluştur
     */
    public static void runAllTestsWithReport() {
        System.out.println("Amazon Turkey Website Test Raporu");
        System.out.println("=================================");
        System.out.println("Test Tarihi: " + java.time.LocalDateTime.now());
        System.out.println("Test Hedefi: https://www.amazon.com.tr/");
        System.out.println();
        // Test sınıflarını çalıştır
        try {
            runTestClass(Class.forName("AmazonAdvancedTest"));
        } catch (ClassNotFoundException e) {
            System.err.println("Test sınıfları bulunamadı: " + e.getMessage());
            System.err.println("Lütfen test sınıflarının doğru konumda olduğunu kontrol edin.");
            System.exit(1);
        }
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Test Raporu Tamamlandı");
        System.out.println("=".repeat(50));
        System.out.println("🔒 Program kapatılıyor...");
        System.exit(0);
    }
} 