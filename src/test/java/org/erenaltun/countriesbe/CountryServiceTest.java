//package org.erenaltun.countriesbe;
//
//import org.erenaltun.countriesbe.entity.Country;
//import org.erenaltun.countriesbe.initializer.CountryInitializer;
//import org.erenaltun.countriesbe.repository.ICountryRepository;
//import org.erenaltun.countriesbe.service.impl.CountryService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
////neden unıt testlerde newleyerek sınıfları alıyoruz normalde dependency ınject kullanırdık cunku
////Normalde projen çalışırken nesneleri Spring oluşturur. Ancak biz Unit Test yazarken hız kazanmak ve sadece o sınıfı test etmek için Spring'i (Context) ayağa kaldırmayız. Spring ayağa kalkmadığı için @Autowired veya @RequiredArgsConstructor (Spring tarafındaki otomatik enjeksiyon kısmı) çalışmaz.İşte bu yüzden nesneyi bir şekilde oluşturmak zorundayız.
//
//@ExtendWith(MockitoExtension.class) //mockıtoyu ayaga kaldırır bu sayede fake verılerle calısabılecegım test ederken sureklı olarak database le calısırsam hem yavas calısmıs oluruz hemde hata database de mı servıce de mı anlamak zorlasır bu unıt test entegrasyon testı degıl
//public class CountryServiceTest {
//    @Mock
//    private ICountryRepository countryRepository;  //bu sahte cunku gercek reposıtory verılerle ugrasıyordu
//
//    @InjectMocks  //new leme ıslemını gızlıce yapar mocks
//    private CountryService countryService;  //bu gercek cunku bunu test etmek ıstıyorum
//
//
//
//    @Test
//    void getCountry_UlkeVarsa_UlkeyiDonmeli() {
//        String ulkeKodu = "TR";
//        Country fakeCountry = Country.builder()
//                .code(ulkeKodu)
//                .name("Türkiye")
//                .build();
//        // Repository'ye "findByCode('TR') denirse fakeCountry dön" diyoruz
//        Mockito.when(countryRepository.findByCode(ulkeKodu)).thenReturn(Optional.of(fakeCountry));
//        // Eylem test edeceğimiz servis'i kullanıyoruz.
//        Country result = countryService.getCountry(ulkeKodu);
//        // 3. ASSERT (Doğrulama)
//        assertNotNull(result);
//        assertEquals("Türkiye", result.getName());
//        assertEquals(ulkeKodu, result.getCode());
//    }
//
//    @Test
//    void deleteCountry_UlkevarsaSilinmeli(){
//        String ulkeKodu = "TR";
//        Country fakeCountry = Country.builder()
//                .code(ulkeKodu)
//                .name("Türkiye")
//                .build();
//        Mockito.when(countryRepository.findByCode(ulkeKodu)).thenReturn(Optional.of(fakeCountry));
//        Country result = countryService.deleteCountry(ulkeKodu);
//        assertNotNull(result);
//        assertEquals("Türkiye", result.getName());
//        assertEquals(ulkeKodu, result.getCode());
//        // "countryRepository nesnesinin deleteByCode metodu, 'TR' parametresiyle tam olarak 1 KERE çağrıldı mı?"
//        Mockito.verify(countryRepository, Mockito.times(1)).deleteByCode(ulkeKodu);
//    }
//
//    @Test
//    void convertCountry_UlkevarsaAdınıDegistirme(){
//        String ulkeKodu = "TR";
//        //kısının degıstırmek ıstedıgı ulke ıcın fake verı olusturuyorum
//        Country fakeCountry = Country.builder().code(ulkeKodu).name("Türkiye").build();
//        //kısının koyacagı yenı ısımlı verı ıse deneme adında olacak
//        Country updatedFakeCountry = Country.builder().code(ulkeKodu).name("Deneme").build();
//        //fındbycode metodu calıstıgı vakıt fake verı donecek
//        Mockito.when(countryRepository.findByCode(ulkeKodu)).thenReturn(Optional.of(fakeCountry));
//        Mockito.when(countryRepository.save(Mockito.any(Country.class))).thenReturn(updatedFakeCountry);
//        //Mockito.any(Country.class)  bunu yazarak mockıtoya sana verdıgım deger onemlı degıl country classında olacak dırekt oraya fakecountry dıyebılırdık
//        //lakın bız servıste donen country ın ısmını degıstırıyoruz ya test asamasında patlar aynı nesne olmadıgı ıcın bızde nesnesı onemlı degıl calısıp calısmadıgına bakıyoruz bız.
//        Country result = countryService.convertCountry(ulkeKodu,"Deneme");
//        assertNotNull(result);
//        assertEquals("Deneme", result.getName());
//        assertEquals(ulkeKodu, result.getCode());
//        //burada da save e verdıgım deger onemlı degıl calısıyor mu dıye bakıyorum.
//        //pekı neden fake country demıyorum patlar dıyorum cunku equals dıyoruz ya burada nesnenın ıcıne gırıp degerlerıne bakmıyor tek tek adresıne bakıyor ısmını degıstırınce bız nesnenın adresı degısıyor
//        Mockito.verify(countryRepository,Mockito.times(1)).save(Mockito.any(Country.class));
//    }
//
//    @Test
//    void insertCountries_JsonOkunupVeritabaninaKaydedilmeli() {
//        // 1. ARRANGE
//        // Dosyadan okunmuş gibi davranacak sahte bir liste hazırlıyoruz
//        Country fakeCountry = Country.builder().code("TR").name("Türkiye").build();
//        List<Country> fakeCountryList = List.of(fakeCountry);
//
//        // DİKKAT: Statik metotları mocklamak için "try-with-resources" kullanmalıyız.
//        // Bu sayede mocklama işlemi sadece bu bloğun içinde geçerli olur, diğer testleri bozmaz.
//        try (MockedStatic<CountryInitializer> mockedStatic = Mockito.mockStatic(CountryInitializer.class)) {
//
//            // Statik metoda diyoruz ki: "Biri seni çağırırsa dosyaya gitme, bu listeyi dön!"
//            mockedStatic.when(CountryInitializer::readCountry).thenReturn(fakeCountryList);
//
//            // Repository'ye diyoruz ki: "Sana bu liste gelirse, aynen geri dön"
//            Mockito.when(countryRepository.saveAll(fakeCountryList)).thenReturn(fakeCountryList);
//
//            // 2. ACT
//            List<Country> result = countryService.insertCountries();
//
//            // 3. ASSERT
//            assertNotNull(result);
//            assertEquals(1, result.size());
//            assertEquals("Türkiye", result.get(0).getName());
//
//            // MÜFETTİŞ: saveAll metodu gerçekten o sahte listeyle çağrıldı mı?
//            Mockito.verify(countryRepository, Mockito.times(1)).saveAll(fakeCountryList);
//        }
//    }
//
//    @Test
//    void insertCountry_TekbirUlkeKaydetme(){
//        // 1. ARRANGE
//        Country fakeCountry = Country.builder().code("DE").name("Deneme").build();
//
//        // a) Başarılı kayıt için ülkenin veritabanında "BULUNAMAMASI" gerekir.
//        Mockito.when(countryRepository.findByCode("DE")).thenReturn(Optional.empty());
//
//        // b) Kaydetme işlemi yapıldığında bu ülkeyi geri dön.
//        Mockito.when(countryRepository.save(Mockito.any(Country.class))).thenReturn(fakeCountry);
//
//        // 2. ACT
//        Country result = countryService.insertCountry(fakeCountry);
//
//        // 3. ASSERT
//        assertNotNull(result);
//        assertEquals("DE", result.getCode());
//        assertEquals("Deneme", result.getName());
//
//        // MÜFETTİŞ: Gerçekten "save" metodu çağrıldı mı?
//        Mockito.verify(countryRepository, Mockito.times(1)).save(fakeCountry);
//    }
//
//
//    @Test
//    void getAllCountries_ButunListeyiDonme(){
//        //1- veri tabanına gıtmeyecegı ıcın fake verıler olusturacagım
//        Country fakeCountry=Country.builder().code("TR").name("TURKEY").nativeName("TÜRKİYE").build();
//        Country fakeCountry2=Country.builder().code("TRI").name("TURKEYI").nativeName("TÜRKİYE2").build();
//        List<Country>deneme=new ArrayList<>();
//        deneme.add(fakeCountry2);
//        deneme.add(fakeCountry);
//        Mockito.when(countryRepository.findAll()).thenReturn(deneme);
//
//        List<Country>result=countryService.getAllCountries();
//        assertEquals(deneme,result);
//        assertNotNull(result);
//
//
//    }
//
//}
