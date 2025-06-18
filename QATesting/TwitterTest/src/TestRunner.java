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
        System.out.println("Amazon Turkey Website Test Bot Starting");
        System.out.println("==============================================");
        
        try {
            runTestClass(Class.forName("AmazonAdvancedTest"));
        } catch (ClassNotFoundException e) {
            System.err.println("Test classes not found: " + e.getMessage());
            System.err.println("Please check that test classes are in the correct location.");
        }
        
        System.out.println("\nAll tests completed!");
    }
    
    private static void runTestClass(Class<?> testClass) {
        System.out.println("\nRunning " + testClass.getSimpleName() + " class");
        
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(selectClass(testClass))
                .build();
        
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        
        launcher.execute(request);
        
        TestExecutionSummary summary = listener.getSummary();
        
        System.out.println("Test Results:");
        System.out.println("- Total test count " + summary.getTestsFoundCount());
        System.out.println("- Successful tests " + summary.getTestsSucceededCount());
        System.out.println("- Failed tests " + summary.getTestsFailedCount());
        System.out.println("- Skipped tests " + summary.getTestsSkippedCount());
        
        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\nFailed tests");
            summary.getFailures().forEach(failure -> {
                System.out.println("- " + failure.getTestIdentifier().getDisplayName());
                System.out.println("  Error " + failure.getException().getMessage());
            });
        }
    }
    
    public static void runSpecificTest(Class<?> testClass, String testMethodName) {
        System.out.println("\nRunning " + testClass.getSimpleName() + "." + testMethodName );
        
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(selectMethod(testClass, testMethodName))
                .build();
        
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        
        launcher.execute(request);
        
        TestExecutionSummary summary = listener.getSummary();
        System.out.println("Test completed. Successful: " + summary.getTestsSucceededCount() + 
                          ", Failed " + summary.getTestsFailedCount());
    }
    
    public static void runAllTestsWithReport() {
        System.out.println("Amazon Turkey Website Test Report");
        System.out.println("=================================");
        System.out.println("Test Date " + java.time.LocalDateTime.now());
        System.out.println("Test Target https://www.amazon.com.tr/");
        System.out.println();
        
        try {
            runTestClass(Class.forName("AmazonAdvancedTest"));
        } catch (ClassNotFoundException e) {
            System.err.println("Test classes not found: " + e.getMessage());
            System.err.println("Please check that test classes are in the correct location.");
            System.exit(1);
        }
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Test Report Completed");
        System.out.println("=".repeat(50));
        System.out.println("Program closing byü");
        System.exit(0);
    }
} 