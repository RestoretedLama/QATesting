public class TestCompilation {
    public static void main(String[] args) {
        System.out.println("TestConfig sınıfı başarıyla derlendi!");
        System.out.println("Responsive boyutlar: " + TestConfig.RESPONSIVE_SIZES.length + " adet");
        System.out.println("Cihaz isimleri: " + TestConfig.RESPONSIVE_DEVICE_NAMES.length + " adet");
        
        // Array'leri test et
        for (int i = 0; i < TestConfig.RESPONSIVE_SIZES.length; i++) {
            int[] size = TestConfig.RESPONSIVE_SIZES[i];
            String device = TestConfig.RESPONSIVE_DEVICE_NAMES[i];
            System.out.println(device + ": " + size[0] + "x" + size[1]);
        }
        
        System.out.println("Tüm testler başarılı!");
    }
} 