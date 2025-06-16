public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("=== Amazon Turkey Test Bot - Derleme Testi ===");
        
        try {
            // TestConfig sınıfını test et
            System.out.println("1. TestConfig sınıfı test ediliyor...");
            System.out.println("   - AMAZON_URL: " + TestConfig.AMAZON_TURKEY_URL);
            System.out.println("   - Responsive boyutlar: " + TestConfig.RESPONSIVE_SIZES.length);
            System.out.println("   - Cihaz isimleri: " + TestConfig.RESPONSIVE_DEVICE_NAMES.length);
            System.out.println("   ✅ TestConfig başarılı!");
            
            // AmazonTestUtils sınıfını test et
            System.out.println("2. AmazonTestUtils sınıfı test ediliyor...");
            // Bu sınıf WebDriver gerektirdiği için sadece varlığını kontrol ediyoruz
            Class.forName("AmazonTestUtils");
            System.out.println("   ✅ AmazonTestUtils bulundu!");
            
            // Test sınıflarını test et
            System.out.println("3. Test sınıfları test ediliyor...");
            Class.forName("AmazonTurkeyTest");
            System.out.println("   ✅ AmazonTurkeyTest bulundu!");
            Class.forName("AmazonAdvancedTest");
            System.out.println("   ✅ AmazonAdvancedTest bulundu!");
            
            // TestRunner sınıfını test et
            System.out.println("4. TestRunner sınıfı test ediliyor...");
            Class.forName("TestRunner");
            System.out.println("   ✅ TestRunner bulundu!");
            
            System.out.println("\n🎉 Tüm sınıflar başarıyla derlendi ve bulundu!");
            System.out.println("Amazon Turkey Test Bot kullanıma hazır!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Hata: " + e.getMessage());
            System.err.println("Lütfen tüm dosyaların doğru konumda olduğunu kontrol edin.");
        } catch (Exception e) {
            System.err.println("❌ Beklenmeyen hata: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 