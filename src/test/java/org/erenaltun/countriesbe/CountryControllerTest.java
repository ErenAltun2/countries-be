package org.erenaltun.countriesbe;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.erenaltun.countriesbe.controller.CountryController;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.mapper.ICountryMapper;
import org.erenaltun.countriesbe.repository.ICountryRepository;
import org.erenaltun.countriesbe.service.interfaces.ICountryService;
import org.erenaltun.countriesbe.service.interfaces.II18nMessageService;
import org.erenaltun.countriesbe.util.constants.Api;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CountryController.class)
public class CountryControllerTest {
    @Autowired
    private MockMvc mockMvc; // bu bizim postmanimiz.
//    @autowired o zaman dependencyınject gıbı newlemeden olusturmamızı saglıyor gersek nesne Mockbean ıse controllerdakı mock gıbı fake
//    olarak olusturmamızı saglıyor gercek countryservıceye gıtmeye gerek kalmadan
    @MockBean
    private ICountryService countryService;
    @MockBean
    private ICountryMapper countryMapper;
    @MockBean
    private II18nMessageService i18nMessageService;
    @Autowired
    private ObjectMapper objectMapper;  //bunu kullanıyoruz suan postman vs kullanmayacagız yanı uc noktalara json formatında verecegız uzun uzun yazmamak ıcın yazdıklarımızı json formatına donusturecek

    @Test
    void getCountry_GecerliKodIle_BasariliJsonDonmeli() throws Exception {
        String ulkeKodu = "TR";
        Country fakeCountry = Country.builder().code(ulkeKodu).name("Türkiye").build();
        Mockito.when(countryService.getCountry(ulkeKodu)).thenReturn(fakeCountry);
        mockMvc.perform(get(Api.Country.BASE_URL + Api.Country.GET_COUNTRY, ulkeKodu)
                        .contentType(MediaType.APPLICATION_JSON))
                // HTTP 200 OK döndü mü?
                .andExpect(status().isOk())

                // JSON'ın içindeki "success" alanı true mu?
                .andExpect(jsonPath("$.success").value(true))

                // JSON'ın içindeki "data.name" alanı "Türkiye" mi?
                .andExpect(jsonPath("$.data.name").value("Türkiye"))

                // JSON'ın içindeki "message" alanı senin yazdığın mesajla aynı mı?
                .andExpect(jsonPath("$.message").value("başarıyla dondu"));

    }
}
