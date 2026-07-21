package org.erenaltun.countriesbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.erenaltun.countriesbe.controller.CountryController;
import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.service.interfaces.ICountryService;
import org.erenaltun.countriesbe.service.interfaces.ICurrencyService;
import org.erenaltun.countriesbe.service.interfaces.II18nMessageService;
import org.erenaltun.countriesbe.util.constants.i18n.I18nConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//servis katmanında aslında yazılan metotları test etmek gıbı sprıng boot u ayaga kaldırmaya gerek yok
//degerlerı verıyoruz ve servıs ıcındekı metotları test edıyoruz mockıto ıle manuel olarak dependency ınject uygulamıs oluyoruz
//lakın controller testı yazrken gelen ısteklerı karsılamak sonrasında json formatındakı verırın java sınıfına donusmesı
//controller katmanından servıs katmanına oradan reposıtory katmanına geçme ıslemlerı ıcın sprıng ın ayaga kaldırılması lazım
//bu sebeple controller sprıng ı ayaga kaldırır lakın bızde sadece controller ı test etmek ıstıyoruz o zaman
//servıs katmanlarını @mockbean ıle fake olusturuyoruz gerçekten kullanacaklarımızı ıse @autowired ıle dependency ınject edıyoruz


@WebMvcTest(CountryController.class) //sadece controller katmanını ayağa kaldıracağız
public class CountryControllerTest {
    @Autowired
    private MockMvc mockMvc; //http ısteklerını sımule eder

    @Autowired
    private ObjectMapper objectMapper; //nesnelerı json strıngıne cevırmek ıcın

    @MockBean
    private ICountryService countryService;

    @MockBean
    private II18nMessageService messageService;

    @MockBean
    private ICurrencyService currencyService;

    private CountryDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new CountryDto();
        sampleDto.setCode("TR");
        sampleDto.setName("Türkiye");
    }

    @Test
    void getAllCountries_Success_Test() throws Exception {
        Mockito.when(countryService.getAllCountries()).thenReturn(List.of(sampleDto));
        when(messageService.getMessage(Mockito.anyString(), Mockito.any(Locale.class)))
                .thenReturn("Liste başarıyla getirildi");

        // WHEN & THEN: İsteği at ve sonuçları kontrol et
        mockMvc.perform(get("/countries/all"))
                .andExpect(status().isOk()) // HTTP 200 mü?
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].code").value("TR"))
                .andExpect(jsonPath("$.message").value("Liste başarıyla getirildi"));
    }

    @Test
    void getAllCountries_Unsuccess_Test() throws Exception {
        when(countryService.getAllCountries()).thenReturn(Collections.emptyList());
        when(messageService.getMessage(Mockito.anyString(), Mockito.any(Locale.class)))
                .thenReturn("ülke bulunamadı");

        // WHEN & THEN: İsteği at ve sonuçları kontrol et
        mockMvc.perform(get("/countries/all"))
                .andExpect(status().isOk()) // HTTP 200 mü?
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty()) // Data kısmının boş olduğunu doğrula
                .andExpect(jsonPath("$.message").value("ülke bulunamadı"));
    }

    @Test
    void insertCountries_Test() throws Exception{
        when(countryService.insertCountries()).thenReturn(List.of(sampleDto));
        when(messageService.getMessage(Mockito.anyString(), Mockito.any(Locale.class))).thenReturn("ülkeler eklendi");

        mockMvc.perform(post("/countries/insertAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].code").value("TR"))
                .andExpect(jsonPath("$.message").value("ülkeler eklendi"));
    }

    @Test
    void insertCountry_Test() throws Exception{
        CountryDto inputDto = new CountryDto();
        inputDto.setCode("TR");
        inputDto.setName("Türkiye");
        when(countryService.insertCountry(Mockito.any(CountryDto.class))).thenReturn(sampleDto);
        when(messageService.getMessage(Mockito.anyString(), Mockito.any(Locale.class))).thenReturn("ülke eklendi");

        mockMvc.perform(post("/countries/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("TR"))
                .andExpect(jsonPath("$.message").value("ülke eklendi"));
    }


    @Test
    void getCountry() throws Exception {
        String countryCode = "TR";
        when(countryService.getCountry(countryCode)).thenReturn(sampleDto);
        when(messageService.getMessage(Mockito.anyString(),Mockito.any(Locale.class))).thenReturn("ülke getirildi");

        mockMvc.perform(get("/countries/getcountry/{code}",countryCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("TR"))
                .andExpect(jsonPath("$.message").value("ülke getirildi"));
    }

    @Test
    void deleteCountry() throws Exception {
        String countryCode = "TR";
        when(countryService.deleteCountry(countryCode)).thenReturn(sampleDto);
        when(messageService.getMessage(Mockito.anyString(),Mockito.any(Locale.class))).thenReturn("ülke Silindi");

        mockMvc.perform(delete("/countries/deletecountry/{code}",countryCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("TR"))
                .andExpect(jsonPath("$.message").value("ülke Silindi"));

    }

    @Test
    void convertName() throws Exception{
        String countryCode = "TR";
        String newName="TürkiyeYeni";
        sampleDto.setName(newName);
        when(countryService.convertCountry(countryCode,newName)).thenReturn(sampleDto);
        when(messageService.getMessage(Mockito.anyString(),Mockito.any(Locale.class),Mockito.anyString())).thenReturn("ülke ismi değiştirildi");

        mockMvc.perform(put("/countries/putcountry/{code}/{name}",countryCode,newName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("TürkiyeYeni"))
                .andExpect(jsonPath("$.message").value("ülke ismi değiştirildi"));
    }

    @Test
    void getCountryId() throws Exception{
        long id = 1;
        when(countryService.getCountryId(id)).thenReturn(sampleDto);
        when(messageService.getMessage(Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn("Ülke getirildi");

        mockMvc.perform(get("/countries/getcountryId/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("TR"))
                .andExpect(jsonPath("$.message").value("Ülke getirildi"));
    }

    @Test
    void getCountryName() throws Exception{
        String name = "Türkiye";
        when(countryService.getCountryName(name)).thenReturn(sampleDto);
        when(messageService.getMessage(Mockito.anyString(),Mockito.any())).thenReturn("Ülke getirildi");

        mockMvc.perform(get("/countries/getcountryName/{name}",name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Türkiye"))
                .andExpect(jsonPath("$.message").value("Ülke getirildi"));
    }


    @Test
    void getAllCountryNames() throws Exception {
        when(countryService.getAllCountriesName()).thenReturn(List.of(sampleDto.getName()));
        when(messageService.getMessage(Mockito.anyString(),Mockito.any())).thenReturn("Ülke isimleri Döndü");

        mockMvc.perform(get("/countries/all/names"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0]").value("Türkiye"))
                .andExpect(jsonPath("$.message").value("Ülke isimleri Döndü"));
    }

    @Test
    void getPhoneCountry() throws Exception{
        int phone=90;
        when(countryService.getPhoneCountry(phone)).thenReturn(List.of(sampleDto));
        when(messageService.getMessage(Mockito.anyString(),Mockito.any())).thenReturn("Ülke telefon kodu");

        mockMvc.perform(get("/countries/getPhone")
                        .param("phone",String.valueOf(phone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Türkiye"))
                .andExpect(jsonPath("$.message").value("Ülke telefon kodu"));

    }

    @Test
    void getContinentCountry() throws Exception{
        String continent = "EU";
        when(countryService.getContinentCountry(continent)).thenReturn(List.of(sampleDto));
        when(messageService.getMessage(Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn("Ülkeler kıtasına göre getirildi");

        mockMvc.perform(get("/countries/continent").param("continent",continent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Türkiye"))
                .andExpect(jsonPath("$.message").value("Ülkeler kıtasına göre getirildi"));
    }

    @Test
    void getlanguageCountry() throws Exception {
        String language = "TR";
        when(countryService.getCountryLanguage(language)).thenReturn(List.of(sampleDto));
        when(messageService.getMessage(Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn("diline göre ülkeleri getirme");

        mockMvc.perform(get("/countries/language").param("language",language))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Türkiye"))
                .andExpect(jsonPath("$.message").value("diline göre ülkeleri getirme"));
    }

    @Test
    void getCurrency() throws Exception {
        List<String>currency=List.of("TRY","EURO","USS");
        when(countryService.getCurrency()).thenReturn(currency);
        when(messageService.getMessage(Mockito.anyString(),Mockito.any())).thenReturn("tüm para birimleri");

        mockMvc.perform(get("/countries/currency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0]").value("TRY"))
                .andExpect(jsonPath("$.message").value("tüm para birimleri"));

    }

    @Test
    void getPhoneCodeAsc() throws Exception {
        String order = "asc";
        sampleDto.setPhone(1);
        when(countryService.getPhoneAscending()).thenReturn(List.of(sampleDto));
        when(messageService.getMessage(Mockito.anyString(),Mockito.any())).thenReturn("telefon numaraları kucukten buyuye sıralanmıstır.");

        mockMvc.perform(get("/countries/phoneCodes").param("order",order))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].phone").value(1))
                .andExpect(jsonPath("$.message").value("telefon numaraları kucukten buyuye sıralanmıstır."));

    }

    @Test
    void getPhoneCodeDesc() throws Exception {
        String order = "desc";
        sampleDto.setPhone(200);
        when(countryService.getPhoneDescending()).thenReturn(List.of(sampleDto));
        when(messageService.getMessage(Mockito.anyString(),Mockito.any())).thenReturn("telefon numaraları buyukten kucuge sıralanmıstır.");

        mockMvc.perform(get("/countries/phoneCodes").param("order",order))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].phone").value(200))
                .andExpect(jsonPath("$.message").value("telefon numaraları buyukten kucuge sıralanmıstır."));
    }

    @Test
    void getCurrencyRate() throws Exception {
        String code = "TR";
        Double kur = 40.0;
        when(currencyService.getRateToTry(code)).thenReturn(kur);
        when(messageService.getMessage(Mockito.anyString(),Mockito.any())).thenReturn("kur bulundu");

        mockMvc.perform(get("/countries/currency/rate").param("code",code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(40.0))
                .andExpect(jsonPath("$.message").value("kur bulundu"));
    }

    @Test
    void getTop5Currencies() throws Exception{

        Map<String, Double> top5=new HashMap<>();
        top5.put("dolar",40.0);
            when(currencyService.getTop5Currencies()).thenReturn(top5);
            when(messageService.getMessage(Mockito.anyString(),Mockito.any())).thenReturn("kurlar döndürüldü");

            mockMvc.perform(get("/countries/currency/top5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.dolar").value(40.0))
                    .andExpect(jsonPath("$.message").value("kurlar döndürüldü"));
    }
}
