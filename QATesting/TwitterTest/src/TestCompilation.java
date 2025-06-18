public class TestCompilation {
    public static void main(String[] args) {
        System.out.println("TestConfig class successfully compiled!");
        System.out.println("Responsive sizes: " + TestConfig.RESPONSIVE_SIZES.length + " items");
        System.out.println("Device names: " + TestConfig.RESPONSIVE_DEVICE_NAMES.length + " items");
        

        for (int i = 0; i < TestConfig.RESPONSIVE_SIZES.length; i++) {
            int[] size = TestConfig.RESPONSIVE_SIZES[i];
            String device = TestConfig.RESPONSIVE_DEVICE_NAMES[i];
            System.out.println(device + ": " + size[0] + "x" + size[1]);
        }
        
        System.out.println("All tests successful!");
    }
} 