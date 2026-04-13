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
@RequestMapping("/country")
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

    //json dosyasında butun ulkelerı ekler
    @PostMapping("/insertAll")
    public List<Country>insertCountries(){
        return countryService.insertCountries();
    }

    //tek tek verdıgımız bılgıler dogrultusunda ulkelerı ekler
    @PostMapping("/insert")
    public ResponseEntity<CountryDto> insertCountry(@RequestBody CountryDto countryDto){
        //burada mapper yerıne CountryDto new dıyıp yenı bır nesne uretırıc bu olusan nesneeye .name dıyerek alınan verılerı onda tutarız.
        Country mappedCountry=countryMapper.toCountry(countryDto);  //ıstemcıden alınan countrydto bılgısını country sınıfına cevırıyoruz ıslemler bıttıkten sonra yıne countrydto ya cevırıp verıyorurz bılgıyı.
        Country country = countryService.insertCountry(mappedCountry);
        return ResponseEntity.ok(countryMapper.fromCountry(country));
    }
    //TODO: mapper kısmını servıste yap

    //butun ulkelerı getırme vardı sımdı kullanıcıdan code alınan ulkenın bılgılerını getıren yapalım
    @GetMapping("/getcountry/{code}")
    public GenericResponse<Country>getCountry(@PathVariable String code){
        var country= countryService.getCountry(code);
        return GenericResponse.<Country>builder().success(true).message("başarıyla dondu").data(country).build();  //TODO: message kısmını duzelt
    }

    @DeleteMapping("/deletecountry/{code}")
    public GenericResponse<Country>deleteCountry(@PathVariable String code) {
       var country= countryService.deleteCountry(code);
        return GenericResponse.<Country>builder().success(true).message("girilen kod bilgisindeki ülke silinmiştir").data(country).build();
    }

    @PutMapping("/putcountry/{code}/{name}")
    public GenericResponse<Country>convertName(@PathVariable String code ,@PathVariable String name){
        Country country=countryService.convertCountry(code,name);
        return GenericResponse.<Country>builder().success(true).message("isim "+name+" ile değiştirilmiştir").data(country).build();
    }
}
