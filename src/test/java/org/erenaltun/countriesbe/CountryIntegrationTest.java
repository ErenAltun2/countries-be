package org.erenaltun.countriesbe;

import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.repository.ICountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
//
//İstek Hazırlığı (Request Definition)
//İsteğin Ateşlenmesi (Exchange)
//Durum Kontrolü (Status Assertion)
//Gövdeye Giriş (Body Focus)
//Veri Doğrulama (JsonPath Assertion)

//anladım webtestclıent daha buyuk bır yapı oldugu ıcın adım adım gıtmek gerekıyor body deyım vs seklınde belırtmek gerekıyor ama mockmvc de zatenb onemlı olan controllerın dondugu body
//o yuzden extra olarak belırtmek yerıne andexcept dıyıp ıcerısınde json dosyasının yolunu verıyoruz


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountryIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ICountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        countryRepository.deleteAll();
    }

    // --- TEMEL CRUD VE ID/NAME SORGULARI ---

    @Test
    void testFullCountryLifecycle() {
        CountryDto tr = CountryDto.builder().code("TR").name("Türkiye").continent("Asia").phone(90).build();

        // veri ekleme
        webTestClient.post().uri("/countries/insert")
                .bodyValue(tr)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data.name").isEqualTo("Türkiye");

        // isme göre getirme
        webTestClient.get().uri("/countries/getcountryName/Türkiye")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data.code").isEqualTo("TR");

        // güncelleme
        webTestClient.put().uri("/countries/putcountry/TR/YeniTurkiye")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data.name").isEqualTo("YeniTurkiye");

        // silme
        webTestClient.delete().uri("/countries/deletecountry/TR")
                .exchange()
                .expectStatus().isOk();

        assertEquals(0, countryRepository.count());
    }

    // filtreleme işlemleri

    @Test
    void testFilteringEndpoints() {
        // hazırlık evresi veri tabanına veri ekleyelim başta
        webTestClient.post().uri("/countries/insert")
                .bodyValue(CountryDto.builder().code("TR").name("Türkiye").continent("Asia").phone(90).build())
                .exchange();

        webTestClient.post().uri("/countries/insert")
                .bodyValue(CountryDto.builder().code("DE").name("Germany").continent("Europe").phone(49).build())
                .exchange();

        // kıtaya göre filtreleme
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/countries/continent").queryParam("continent", "Asia").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data.length()").isEqualTo(1)
                .jsonPath("$.data[0].code").isEqualTo("TR");

        // telefon koduna göre getirme
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/countries/getPhone").queryParam("phone", 90).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data[0].name").isEqualTo("Türkiye");

        // tüm ülke isimlerini getirme
        webTestClient.get().uri("/countries/all/names")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data").isArray()
                .jsonPath("$.data.length()").isEqualTo(2);
    }

    // sıralama işlemleri

    @Test
    void testPhoneCodeOrdering() {
        webTestClient.post().uri("/countries/insert").bodyValue(CountryDto.builder().code("A").name("A").phone(10).build()).exchange();
        webTestClient.post().uri("/countries/insert").bodyValue(CountryDto.builder().code("B").name("B").phone(50).build()).exchange();

        // küçğkten büyüge (asc)
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/countries/phoneCodes").queryParam("order", "asc").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data[0].phone").isEqualTo(10);

        // büyükten küçüge
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/countries/phoneCodes").queryParam("order", "desc").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data[0].phone").isEqualTo(50);
    }

    // para pirimi kur sistemi

    @Test
    void testCurrencyEndpoints() {
        // Para birimi listesi (GET /currency) - Mock veritabanında en az bir kayıt olmalı
        webTestClient.post().uri("/countries/insert")
                .bodyValue(CountryDto.builder().code("US").name("USA").currency("USD").build())
                .exchange();

        webTestClient.get().uri("/countries/currency")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data").isArray()
                        .jsonPath("$.data[0]").isEqualTo("USD");

        // Döviz Kuru (GET /currency/rate)
        // Not: currencyService.getRateToTry(code) gerçek bir API'ye gidiyorsa bu test internete ihtiyaç duyar.
        // Eğer entegrasyon testinde dış API'yi kapatmak istersen MockRestServiceServer kullanabilirsin.
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/countries/currency/rate").queryParam("code", "USD").build())
                .exchange()
                .expectStatus().isOk();
    }
}