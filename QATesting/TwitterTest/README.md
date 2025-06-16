# Amazon Turkey Website Test Bot

Bu proje, Amazon Turkey (https://www.amazon.com.tr/) web sitesini test etmek için Java ve Selenium WebDriver kullanılarak geliştirilmiş kapsamlı bir test botudur.

## 🚀 Özellikler

### Test Kapsamı
- ✅ Ana sayfa yükleme ve temel elementlerin kontrolü
- ✅ Ürün arama fonksiyonu
- ✅ Kategori navigasyonu
- ✅ Ürün detay sayfası testleri
- ✅ Sepete ekleme işlemleri
- ✅ Filtreleme ve sıralama
- ✅ Responsive tasarım testleri
- ✅ Performans testleri
- ✅ Çoklu sekme işlemleri
- ✅ Scroll ve navigasyon testleri

### Test Teknolojileri
- **Java 8+** - Ana programlama dili
- **Selenium WebDriver 4.x** - Web otomasyon
- **JUnit 5** - Test framework
- **ChromeDriver** - Tarayıcı otomasyonu
- **Parameterized Tests** - Çoklu test senaryoları

## 📁 Proje Yapısı

```
TwitterTest/
├── src/
│   ├── Main.java                 # Ana uygulama
│   ├── TestRunner.java           # Test çalıştırıcı
│   ├── TestConfig.java           # Test konfigürasyonu
│   ├── AmazonTestUtils.java      # Test yardımcı sınıfı
│   ├── AmazonTurkeyTest.java     # Temel Amazon testleri
│   ├── AmazonAdvancedTest.java   # Gelişmiş testler
│   ├── SimpleTest.java           # Derleme testi
│   └── TestCompilation.java      # Konfigürasyon testi
├── Tests/
│   └── BlueSkyTest.java          # Mevcut test (örnek)
├── TwitterTest.iml               # IntelliJ IDEA modül dosyası
└── README.md                     # Bu dosya
```

## 🛠️ Kurulum

### Gereksinimler
1. **Java 8 veya üzeri**
2. **Chrome tarayıcısı**
3. **ChromeDriver** (Selenium ile otomatik indirilir)
4. **IntelliJ IDEA** (önerilen) veya başka bir Java IDE

### Adımlar
1. Projeyi klonlayın veya indirin
2. IntelliJ IDEA'da projeyi açın
3. Selenium bağımlılıklarının yüklendiğinden emin olun
4. Chrome tarayıcısının yüklü olduğundan emin olun

## 🧪 Testleri Çalıştırma

### 1. IDE Üzerinden
```java
// Main.java'yı çalıştır (tüm testleri çalıştırır)
// SimpleTest.java'yı çalıştır (derleme testi)
// TestCompilation.java'yı çalıştır (konfigürasyon testi)
```

### 2. TestRunner ile
```java
// Tüm testleri çalıştır
TestRunner.runAllTestsWithReport();

// Belirli bir test sınıfını çalıştır
TestRunner.runTestClass(Class.forName("AmazonTurkeyTest"));

// Belirli bir test metodunu çalıştır
TestRunner.runSpecificTest(Class.forName("AmazonTurkeyTest"), "testHomePageLoad");
```

### 3. Komut Satırından
```bash
# JUnit ile testleri çalıştır
java -cp . org.junit.platform.console.ConsoleLauncher --class-path . --select-class AmazonTurkeyTest
```

## 📋 Test Senaryoları

### Temel Testler (AmazonTurkeyTest.java)
1. **Ana Sayfa Yükleme** - Sayfa açılır ve temel elementler görünür
2. **Ürün Arama** - Arama kutusu çalışır ve sonuçlar gösterilir
3. **Kategori Navigasyonu** - Kategori menüsü açılır ve navigasyon çalışır
4. **Ürün Detay Sayfası** - Ürün sayfası açılır ve bilgiler görünür
5. **Sepete Ekleme** - Ürün sepete eklenebilir
6. **Filtreleme** - Arama sonuçları filtrelenebilir
7. **Sayfa Scroll** - Sayfa scroll edilebilir
8. **Responsive Tasarım** - Mobil görünüm çalışır
9. **Performans** - Sayfa yükleme süresi makul
10. **Çoklu Sekme** - Yeni sekme açılabilir

### Gelişmiş Testler (AmazonAdvancedTest.java)
- **Parametreli Testler** - Farklı ürünler ve kategoriler için
- **Responsive Testler** - Farklı ekran boyutları için
- **Element Etkileşimleri** - Butonlar ve linkler çalışır
- **URL Doğrulama** - Doğru URL'ler kullanılır
- **Sayfa Başlığı Doğrulama** - Başlıklar doğru

## 🔧 Konfigürasyon

### Chrome Ayarları
```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--start-maximized");
options.addArguments("--disable-blink-features=AutomationControlled");
options.addArguments("--disable-extensions");
options.addArguments("--no-sandbox");
options.addArguments("--disable-dev-shm-usage");
```

### Test Ayarları
- **Bekleme Süresi**: 20 saniye
- **Sayfa Yükleme Süresi**: 10 saniye maksimum
- **Test URL**: https://www.amazon.com.tr/

## 📊 Test Raporları

Test çalıştırıldığında aşağıdaki bilgiler raporlanır:
- Toplam test sayısı
- Başarılı testler
- Başarısız testler
- Atlanan testler
- Hata detayları

## 🐛 Hata Ayıklama

### Yaygın Sorunlar
1. **ChromeDriver Bulunamadı**: Selenium otomatik olarak indirir
2. **Element Bulunamadı**: Sayfa yapısı değişmiş olabilir
3. **Timeout Hatası**: İnternet bağlantısını kontrol edin
4. **StaleElementException**: Sayfa yeniden yüklenmiş olabilir
5. **ClassNotFoundException**: Dosyaların doğru konumda olduğunu kontrol edin

### Çözümler
- Testleri tekrar çalıştırın
- İnternet bağlantınızı kontrol edin
- Chrome tarayıcısını güncelleyin
- Element seçicilerini güncelleyin
- `SimpleTest.java`'yı çalıştırarak derleme durumunu kontrol edin

## 🔄 Güncelleme

### Yeni Test Ekleme
1. `AmazonTestUtils.java`'ya yeni metod ekleyin
2. Test sınıflarına yeni test metodu ekleyin
3. `@Test` ve `@DisplayName` annotation'larını kullanın

### Mevcut Testleri Güncelleme
1. Element seçicilerini kontrol edin
2. Bekleme sürelerini ayarlayın
3. Assertion'ları güncelleyin

## 📝 Notlar

- Testler gerçek Amazon Turkey sitesini kullanır
- Testler sırasında gerçek ürünler görüntülenir
- Sepete ekleme işlemleri gerçek değildir (test amaçlı)
- Testler farklı zamanlarda farklı sonuçlar verebilir
- Tüm test dosyaları `src/` dizininde bulunur

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/AmazingFeature`)
3. Commit yapın (`git commit -m 'Add some AmazingFeature'`)
4. Push yapın (`git push origin feature/AmazingFeature`)
5. Pull Request oluşturun

## 📄 Lisans

Bu proje eğitim amaçlı geliştirilmiştir. Amazon'un ticari markaları kendi sahiplerine aittir.

## 📞 İletişim

Sorularınız için GitHub Issues kullanabilirsiniz.

---

**Amazon Turkey Test Bot** - Web sitesi test otomasyonu için geliştirilmiş kapsamlı Java test suite'i. 