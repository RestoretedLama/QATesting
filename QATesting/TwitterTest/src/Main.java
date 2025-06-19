//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Amazon Turkey Website Test Bot ===");
        System.out.println("This application tests the Amazon Turkey website.\n");

        System.out.println("    Features:");
        System.out.println("  • Slow and visible test execution");
        System.out.println("  • Add to cart and go to cart tests");
        System.out.println("  • Automatic browser closing");
        System.out.println("  • Detailed test reports\n");


        try {
            System.out.println(" Tests starting");
            System.out.println(" Target URL: https://www.amazon.com.tr/");
            System.out.println(" Cart operations will be tested");
            System.out.println(" Browser will close automatically when tests complete\n");

            

            TestRunner.runAllTestsWithReport();
            
        } catch (Exception e) {
            System.err.println(" Error occurred while running tests: " + e.getMessage());
            System.err.println("Please check the following:");
            System.err.println("1. Your internet connection is active");
            System.err.println("2. Chrome browser is installed");
            System.err.println("3. Selenium dependencies are correctly installed");
            e.printStackTrace();
        }

        System.out.println("\n Program completed!");
        System.out.println(" Test results displayed above.");
        System.out.println(" Browser closed automatically.\n");
        System.out.println("Application closing...");
        

        System.exit(0);
    }
}