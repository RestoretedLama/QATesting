public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("=== Amazon Turkey Test Bot - Compilation Test ===");
        
        try {
            System.out.println("1. Testing TestConfig class...");
            System.out.println("   - AMAZON_URL: " + TestConfig.AMAZON_TURKEY_URL);
            System.out.println("   - Responsive sizes: " + TestConfig.RESPONSIVE_SIZES.length);
            System.out.println("   - Device names: " + TestConfig.RESPONSIVE_DEVICE_NAMES.length);
            System.out.println("   TestConfig successful!");
            
            System.out.println("2. Testing AmazonTestUtils class...");
            Class.forName("AmazonTestUtils");
            System.out.println("  AmazonTestUtils found!");
            
            System.out.println("3. Testing test classes...");
            Class.forName("AmazonTurkeyTest");
            System.out.println("   AmazonTurkeyTest found!");
            Class.forName("AmazonAdvancedTest");
            System.out.println("   AmazonAdvancedTest found!");
            
            System.out.println("4. Testing TestRunner class...");
            Class.forName("TestRunner");
            System.out.println(" TestRunner found!");
            
            System.out.println("\n All classes successfully compiled and found!");
            System.out.println("Amazon Turkey Test Bot ready to use!");
            
        } catch (ClassNotFoundException e) {
            System.err.println(" Error: " + e.getMessage());
            System.err.println("Please check that all files are in the correct location.");
        } catch (Exception e) {
            System.err.println(" Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 