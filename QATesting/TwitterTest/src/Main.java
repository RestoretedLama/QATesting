//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Amazon Turkey Website Test Bot ===");
        System.out.println("Bu uygulama Amazon Turkey web sitesini test eder.");
        System.out.println();
        System.out.println("🔧 Özellikler:");
        System.out.println("  • Yavaş ve görünür test yürütme");
        System.out.println("  • Sepete ekleme ve sepete gitme testleri");
        System.out.println("  • Otomatik tarayıcı kapatma");
        System.out.println("  • Detaylı test raporları");
        System.out.println();
        
        // Test çalıştırıcıyı başlat
        try {
            System.out.println("🚀 Testler başlatılıyor...");
            System.out.println("Hedef URL: https://www.amazon.com.tr/");
            System.out.println("⏱️ Testler yavaş yürütülecek (görünürlük için)");
            System.out.println("🛒 Sepet işlemleri test edilecek");
            System.out.println("🔒 Testler bitince tarayıcı otomatik kapanacak");
            System.out.println();
            
            // TestRunner'ı çalıştır
            TestRunner.runAllTestsWithReport();
            
        } catch (Exception e) {
            System.err.println("❌ Test çalıştırılırken hata oluştu: " + e.getMessage());
            System.err.println("Lütfen aşağıdakileri kontrol edin:");
            System.err.println("1. İnternet bağlantınızın aktif olduğunu");
            System.err.println("2. Chrome tarayıcısının yüklü olduğunu");
            System.err.println("3. Selenium bağımlılıklarının doğru yüklendiğini");
            e.printStackTrace();
        }
        
        System.out.println();
        System.out.println("🎉 Program tamamlandı!");
        System.out.println("📊 Test sonuçları yukarıda görüntülendi.");
        System.out.println("🔒 Tarayıcı otomatik olarak kapatıldı.");
        System.out.println();
        System.out.println("Çıkmak için bir tuşa basın...");
        
        // Kullanıcıdan giriş bekle (opsiyonel)
        try {
            System.in.read();
        } catch (Exception ignored) {
            // Kullanıcı girişi bekleme başarısız olursa program sonlanır
        }
    }
}