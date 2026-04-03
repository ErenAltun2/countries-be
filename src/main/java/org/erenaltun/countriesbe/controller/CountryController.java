package org.erenaltun.countriesbe.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.mapper.ICountryMapper;
import org.erenaltun.countriesbe.service.interfaces.ICountryService;
import org.erenaltun.countriesbe.util.GenericResponse;
import org.erenaltun.countriesbe.util.constants.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Api.Country.BASE_URL)
@RequiredArgsConstructor
public class CountryController {

    private final ICountryMapper countryMapper;

    //dto yu burada kullanacagız ıstersek servıste de kullanabılırız bu dto yu kullanma amacımız gelen verıler sonucunda database den verıler cevap olarak verılecek
    //ben bu verılerı entıty sınıfında kı gıbı gondermek ıstemıyorum belkı gondermek ıstemedıgım verıler veya uzerınde oynama yapmak ıstedıgım verıler olabılır
    //o yuzden dto sınıfını burada yazıyoruz ıstersek servıce de de yazabılırdım.
    private final ICountryService countryService;

    @GetMapping
    public GenericResponse<List<Country>>getAllCountries(){
        var response = countryService.getAllCountries();
        return GenericResponse.<List<Country>>builder()
                .success(true)
                .data(response)
                .build();
    }
    @PostMapping(Api.Country.INSERT_ALL)
    public List<Country>insertCountries(){
        return countryService.insertCountries();
    }

    @PostMapping(Api.Country.INSERT_COUNTRY)
    public ResponseEntity<CountryDto> insertCountry(@RequestBody CountryDto countryDto){
        //burada mapper yerıne CountryDto new dıyıp yenı bır nesne uretırıc bu olusan nesneeye .name dıyerek alınan verılerı onda tutarız.
        Country mappedCountry=countryMapper.toCountry(countryDto);  //ıstemcıden alınan countrydto bılgısını country sınıfına cevırıyoruz ıslemler bıttıkten sonra yıne countrydto ya cevırıp verıyorurz bılgıyı.
        Country country = countryService.insertCountry(mappedCountry);
        return ResponseEntity.ok(countryMapper.fromCountry(country));
    }
}
