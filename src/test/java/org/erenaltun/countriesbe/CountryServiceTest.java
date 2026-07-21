package org.erenaltun.countriesbe;

import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.entity.CountryLanguage;
import org.erenaltun.countriesbe.entity.Language;
import org.erenaltun.countriesbe.exception.ContinentNotFoundException;
import org.erenaltun.countriesbe.exception.CountryAlreadyExistsException;
import org.erenaltun.countriesbe.exception.CountryNotFoundException;
import org.erenaltun.countriesbe.exception.CurrencyNotFoundException;
import org.erenaltun.countriesbe.initializer.CountryInitializer;
import org.erenaltun.countriesbe.mapper.ICountryMapper;
import org.erenaltun.countriesbe.repository.ICountryRepository;
import org.erenaltun.countriesbe.repository.ILanguageRepository;
import org.erenaltun.countriesbe.service.impl.CountryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.print.DocPrintJob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // mockitodan nesne uretecegımızı COuntryServiceTest sınıfına belirtiyoruz.
public class CountryServiceTest {
    @Mock
    private ICountryRepository countryRepository;
    @Mock
    private ICountryMapper countryMapper;
    @Mock
    private ILanguageRepository languageRepository;

    @InjectMocks
    private CountryService countryService;

    private CountryDto createFakeCountryDto(String code,String name){
        CountryDto dto = new CountryDto();
        dto.setCode(code);
        dto.setName(name);
        return dto;
    }

    private Country createFakeCountry(String code , String name){
        Country country = new Country();
        country.setName(name);
        country.setCode(code);
        return country;
    }

    @Test
    void getAllCountries_Test(){
        List<Country> country = new ArrayList<>();
       country.add(createFakeCountry("TR","Türkiye"));
       country.add(createFakeCountry("ER","ALTUN"));

        List<CountryDto> countryDto=new ArrayList<>();
        countryDto.add(createFakeCountryDto("TR","Türkiye"));
        countryDto.add(createFakeCountryDto("ER","ALTUN"));

        Mockito.when(countryRepository.findAll()).thenReturn(country);

        Mockito.when(countryMapper.fromCountryList(country)).thenReturn(countryDto);

        List<CountryDto>result=countryService.getAllCountries();

        assertNotNull(result);

        assertEquals(countryDto.get(0).getName(),result.get(0).getName());
    }

    //burada ben sunu test edıyorum eger daha once kullanılmıs code varsa o ulke aynı code u kullanamaz
    // benı daha once kayıtlı var mı uyarısı calısıyor mu kontrolu
    @Test
    void insertCountry_Test_UlkeZatenVarsa_HataFirlatmali(){
        Country country = createFakeCountry("TR","Türkiye");
        CountryDto dto = createFakeCountryDto("TR","Türkiye");

        Mockito.when(countryMapper.toCountry(dto)).thenReturn(country);
        Mockito.when(countryRepository.findByCode(country.getCode())).thenReturn(Optional.of(country));

        assertThrows(CountryAlreadyExistsException.class,()->{countryService.insertCountry(dto);});

    }

    @Test
    void insertCountry_UlkeYoksa_BasariylaKaydetmeli() {
        CountryDto inputDto = createFakeCountryDto("DE", "Almanya");
        Country countryEntity = createFakeCountry("DE", "Almanya");

        Mockito.when(countryMapper.toCountry(inputDto)).thenReturn(countryEntity);

        Mockito.when(countryRepository.findByCode("DE")).thenReturn(Optional.empty());

        Mockito.when(countryMapper.fromCountry(countryEntity)).thenReturn(inputDto);

        CountryDto result = countryService.insertCountry(inputDto);

        assertNotNull(result);
        assertEquals("Almanya", result.getName());

        // MÜFETTİŞ (Verify): Kod gerçekten save() metoduna ulaştı mı?
        // Eğer hata fırlatsaydı kod buraya hiç gelemeyecekti.
        Mockito.verify(countryRepository, Mockito.times(1)).save(countryEntity);
    }

    @Test
    void insertCountry_YeniDilVarsa_OnuDaKaydetmeli() {
        // 1. Arrange
        CountryDto dto = createFakeCountryDto("FR", "France");
        Country entity = createFakeCountry("FR", "France");

        Language yeniDil = new Language();
        yeniDil.setCode("fr");
        CountryLanguage araci = new CountryLanguage();
        araci.setLanguage(yeniDil);
        entity.setCountryLanguages(new ArrayList<>(List.of(araci)));

        Mockito.when(countryMapper.toCountry(dto)).thenReturn(entity);
        Mockito.when(countryRepository.findByCode("FR")).thenReturn(Optional.empty());

        // KRİTİK: Veritabanında 'fr' dili YOK (Empty dönüyoruz)
        Mockito.when(languageRepository.findByCode("fr")).thenReturn(Optional.empty());

        Mockito.when(countryMapper.fromCountry(entity)).thenReturn(dto);

        // 2. Act
        countryService.insertCountry(dto);

        // 3. Assert
        // Dil yoksa if'e girmez, ama save(country) çağrıldığında o dil de kaydedilir.
        Mockito.verify(countryRepository).save(entity);
        // Veritabanında dil olmadığı için languageRepository.findByCode çağrılmış olmalı
        Mockito.verify(languageRepository).findByCode("fr");
    }

    @Test
    void getCountry_Test(){
        String code="TR";
        Country entity =createFakeCountry(code,"Türkiye");
        CountryDto dto = createFakeCountryDto(code,"Türkiye");

        Mockito.when(countryRepository.findByCode(code)).thenReturn(Optional.of(entity));
        //bunu yazarak dıyoruzkı eğer kı findbycode kullanılırsa yukarıda yazdıgım degerı doneceksın verı tabanına gıtmene gerek yok yanı fake verı

        Mockito.when(countryMapper.fromCountry(entity)).thenReturn(dto);

        //sımdı ıse metodu cagırmada
        CountryDto result = countryService.getCountry(code);

        //burada ıse donmesı gereken degerı bız bılıyoruz bu sekılde karsılastırma yapıyoruz.
        assertEquals("Türkiye",result.getName());

    }

    @Test
    void deleteCountry_Test(){
        String code = "TR";
        CountryDto dto = createFakeCountryDto("TR","Türkiye");
        Country country = createFakeCountry("TR","Türkiye");
        //delete get country ı kullanıyor o zaman get country ı ona gore hazırlamalıyız.
        Mockito.when(countryRepository.findByCode(dto.getCode())).thenReturn(Optional.of(country));
        //gelen country ı mapper ıle dto ya gonderıyordu bırde
        Mockito.when(countryMapper.fromCountry(country)).thenReturn(dto);

        CountryDto result = countryService.deleteCountry(code);
        assertEquals(code,result.getCode());

        //country repository de deletebycode metodu code ile bir kere çağrıldı mı ona bakıyor.
        Mockito.verify(countryRepository,Mockito.times(1)).deleteByCode(code);
        //genellıkle verı tabanında degısıklık yapan kodlar ıcın kullanılır.
    }


    @Test
    void convertCountry_UlkeVarmı(){
        //ulkenın code u ıle bulup ısmını degıstırme endpoıntı ıcın yazıyoruz
        String code = "TR";
        String name="TürkiyeYeni";
        Mockito.when(countryRepository.findByCode(code)).thenReturn(Optional.empty());
        assertThrows(CountryNotFoundException.class,()->{countryService.convertCountry(code,name);});

    }

    @Test
    void convertCountry_NameUpdate_Test(){

        String code = "TR";
        String name = "TürkiyeYeni";
        Country country = createFakeCountry("TR","Türkiye");
        CountryDto dto = createFakeCountryDto(code, name);

        Mockito.when(countryRepository.findByCode(code)).thenReturn(Optional.of(country));
        Mockito.when(countryRepository.save(country)).thenReturn(country);
        Mockito.when(countryMapper.fromCountry(country)).thenReturn(dto);


        CountryDto result = countryService.convertCountry(code, name);

        assertEquals(name, result.getName());
        assertEquals(code, result.getCode());

        Mockito.verify(countryRepository).findByCode(code);
        Mockito.verify(countryRepository).save(country);

    }

    @Test
    void getCountryId_Success_Test(){
        // GIVEN
        Long id = 1L;
        Country country = createFakeCountry("TR", "Türkiye");
        CountryDto dto = createFakeCountryDto("TR", "Türkiye");

        Mockito.when(countryRepository.findById(id)).thenReturn(Optional.of(country));
        Mockito.when(countryMapper.fromCountry(country)).thenReturn(dto);

        // WHEN
        CountryDto result = countryService.getCountryId(id);

        // THEN
        assertNotNull(result);
        assertEquals("Türkiye", result.getName());
        Mockito.verify(countryRepository).findById(id);
    }

    @Test
    void getCountryId_CountryNotFound_Test(){
        // GIVEN
        Long id = 1L;
        Mockito.when(countryRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(CountryNotFoundException.class, () -> {
            countryService.getCountryId(id);
        });

        // Mapper'ın hiç çağrılmadığını da doğrulayabilirsin (Güvenli sürüş)
        Mockito.verifyNoInteractions(countryMapper);
    }

    @Test
    void getCountryName_Success_Test(){
        // GIVEN
        String name = "Türkiye";
        Country country = createFakeCountry("TR", name);
        CountryDto dto = createFakeCountryDto("TR", name);

        Mockito.when(countryRepository.findByName(name)).thenReturn(Optional.of(country));
        Mockito.when(countryMapper.fromCountry(country)).thenReturn(dto);

        // WHEN
        CountryDto result = countryService.getCountryName(name);

        // THEN
        assertNotNull(result);
        assertEquals(name, result.getName());
        Mockito.verify(countryRepository).findByName(name);
    }

    @Test
    void getCountryName_NotFound_Test(){
        // GIVEN
        String name = "BilinmeyenUlke";
        Mockito.when(countryRepository.findByName(name)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(CountryNotFoundException.class, () -> countryService.getCountryName(name));
    }

    @Test
    void getAllCountriesName_Test(){
        // GIVEN
        Country country1 = createFakeCountry("TR", "Türkiye");
        Country country2 = createFakeCountry("DE", "Almanya");
        List<Country> countryList = List.of(country1, country2);

        Mockito.when(countryRepository.findAll()).thenReturn(countryList);

        // WHEN
        List<String> result = countryService.getAllCountriesName();

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.contains("Türkiye"));
        assertTrue(result.contains("Almanya"));
        Mockito.verify(countryRepository).findAll();
    }

    @Test
    void getPhoneCountry_Success_Test(){
        // GIVEN
        int phone = 90;
        Country country = createFakeCountry("TR", "Türkiye");
        List<Country> countryList = List.of(country);

        CountryDto dto = createFakeCountryDto("TR", "Türkiye");
        List<CountryDto> dtoList = List.of(dto);

        Mockito.when(countryRepository.findByPhone(phone)).thenReturn(countryList);
        Mockito.when(countryMapper.fromCountryList(countryList)).thenReturn(dtoList);

        // WHEN
        List<CountryDto> result = countryService.getPhoneCountry(phone);

        // THEN
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Türkiye", result.get(0).getName());
    }

    @Test
    void getPhoneCountry_NotFound_Test(){
        // GIVEN
        int phone = 999;
        // Liste boş döndüğünde exception fırlatmalı
        Mockito.when(countryRepository.findByPhone(phone)).thenReturn(Collections.emptyList());

        // WHEN & THEN
        assertThrows(CountryNotFoundException.class, () -> countryService.getPhoneCountry(phone));
    }

    @Test
    void getContinentCountry_Success_Test(){
        // GIVEN
        String continent = "Asia";
        List<Country> countries = List.of(createFakeCountry("TR", "Türkiye"));
        List<CountryDto> dtos = List.of(createFakeCountryDto("TR", "Türkiye"));

        Mockito.when(countryRepository.findByContinent(continent)).thenReturn(countries);
        Mockito.when(countryMapper.fromCountryList(countries)).thenReturn(dtos);

        // WHEN
        List<CountryDto> result = countryService.getContinentCountry(continent);

        // THEN
        assertEquals(1, result.size());
        Mockito.verify(countryRepository).findByContinent(continent);
    }

    @Test
    void getContinentCountry_NotFound_Test(){
        // GIVEN
        String continent = "Atlantis";
        Mockito.when(countryRepository.findByContinent(continent)).thenReturn(Collections.emptyList());

        // WHEN & THEN
        assertThrows(ContinentNotFoundException.class, () -> countryService.getContinentCountry(continent));
    }

    @Test
    void getCountryLanguage_Test(){
        // GIVEN
        String lang = "TR";
        List<Country> countries = List.of(createFakeCountry("TR", "Türkiye"));
        List<CountryDto> dtos = List.of(createFakeCountryDto("TR", "Türkiye"));

        Mockito.when(countryRepository.findCountriesByLanguageCode(lang)).thenReturn(countries);
        Mockito.when(countryMapper.fromCountryList(countries)).thenReturn(dtos);

        // WHEN
        List<CountryDto> result = countryService.getCountryLanguage(lang);

        // THEN
        assertEquals(1, result.size());
    }

    @Test
    void getCurrency_Success_Test(){
        // GIVEN
        List<String> currencies = List.of("TRY", "USD", "EUR");
        Mockito.when(countryRepository.findCurrencyAll()).thenReturn(currencies);

        // WHEN
        List<String> result = countryService.getCurrency();

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains("TRY"));
    }

    @Test
    void getCurrency_Empty_Test(){
        // GIVEN
        Mockito.when(countryRepository.findCurrencyAll()).thenReturn(Collections.emptyList());

        // WHEN & THEN
        assertThrows(CurrencyNotFoundException.class, () -> countryService.getCurrency());
    }


    @Test
    void getPhoneAscending_Success_Test(){

        Country c1=createFakeCountry("TR","Türkiye");
        c1.setPhone(1);
        Country c2=createFakeCountry("ER","TürkiyeYeni");
        c2.setPhone(2);
        List<Country>countries=List.of(c1,c2);

        CountryDto d1 = createFakeCountryDto("TR","Türkiye");
        d1.setPhone(1);

        CountryDto d2 =createFakeCountryDto("ER","TürkiyeYeni");
        d2.setPhone(2);

        List<CountryDto> dtos = List.of(d1, d2);
        Mockito.when(countryRepository.phoneByAscending()).thenReturn(countries);
        Mockito.when(countryMapper.fromCountryList(countries)).thenReturn(dtos);

        List<CountryDto> result = countryService.getPhoneAscending();

        assertEquals(1, result.get(0).getPhone(), "İlk eleman en küçük telefon koduna sahip olmalı");
        assertEquals(2, result.get(1).getPhone(), "İkinci eleman bir sonraki kod olmalı");

    }

    @Test
    void getPhoneDescending_Success_Test(){
        Country c1=createFakeCountry("TR","Türkiye");
        c1.setPhone(2);
        Country c2=createFakeCountry("ER","TürkiyeYeni");
        c2.setPhone(1);
        List<Country>countries=List.of(c1,c2);

        CountryDto d1 = createFakeCountryDto("TR","Türkiye");
        d1.setPhone(2);

        CountryDto d2 =createFakeCountryDto("ER","TürkiyeYeni");
        d2.setPhone(1);

        List<CountryDto> dtos = List.of(d1, d2);
        Mockito.when(countryRepository.phoneByDescending()).thenReturn(countries);
        Mockito.when(countryMapper.fromCountryList(countries)).thenReturn(dtos);

        List<CountryDto> result = countryService.getPhoneDescending();

        assertEquals(2, result.get(0).getPhone(), "İlk eleman büyük telefon numarasına sahip olmalı");
        assertEquals(1, result.get(1).getPhone(), "İkinci eleman bir sonraki kod olmalı");
    }

    @Test
    void getPhone_NotFound_Test(){
        // Her iki metod da boş liste dönerse aynı hatayı fırlatıyor
        Mockito.when(countryRepository.phoneByAscending()).thenReturn(Collections.emptyList());

        assertThrows(CountryNotFoundException.class, () -> countryService.getPhoneAscending());
    }
}



