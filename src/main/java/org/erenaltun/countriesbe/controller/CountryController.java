package org.erenaltun.countriesbe.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.mapper.ICountryMapper;
import org.erenaltun.countriesbe.service.interfaces.ICountryService;
import org.erenaltun.countriesbe.service.interfaces.II18nMessageService;
import org.erenaltun.countriesbe.util.GenericResponse;
import org.erenaltun.countriesbe.util.constants.Api;
import org.erenaltun.countriesbe.util.constants.i18n.I18nConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {
    //TODO: message kısımları duzeltıldı aynı zamanda mapper servis katmanına taşındı.
    //dto yu burada kullanacagız ıstersek servıste de kullanabılırız bu dto yu kullanma amacımız gelen verıler sonucunda database den verıler cevap olarak verılecek
    //ben bu verılerı entıty sınıfında kı gıbı gondermek ıstemıyorum belkı gondermek ıstemedıgım verıler veya uzerınde oynama yapmak ıstedıgım verıler olabılır
    //o yuzden dto sınıfını burada yazıyoruz ıstersek servıce de de yazabılırdım.
    private final ICountryService countryService;
    private final II18nMessageService messageService;


    @GetMapping("/all")
    public GenericResponse<List<Country>>getAllCountries(Locale locale){
        var response = countryService.getAllCountries();
        String message = messageService.getMessage(I18nConstants.COUNTRY_GET_ALL_SUCCESS,locale);
        return GenericResponse.<List<Country>>builder()
                .success(true)
                .message(message)
                .data(response)
                .build();
    }

    //json dosyasında butun ulkelerı ekler
    @PostMapping("/insertAll")
    public GenericResponse <List<CountryDto>> insertCountries(Locale locale){
        List<CountryDto> response = countryService.insertCountries();
        String message=messageService.getMessage(I18nConstants.COUNTRY_INSERT_ALL_SUCCESS,locale);
        return GenericResponse.<List<CountryDto>>builder()
                .success(true)
                .message(message)
                .data(response)
                .build();
    }

    //tek tek verdıgımız bılgıler dogrultusunda ulkelerı ekler
    @PostMapping("/insert")
    public GenericResponse<CountryDto> insertCountry(@RequestBody CountryDto countryDto, Locale locale){
        CountryDto country = countryService.insertCountry(countryDto);
        String message = messageService.getMessage(I18nConstants.COUNTRY_INSERT_SUCCESS,locale);
        return GenericResponse.<CountryDto>builder().success(true).message(message).data(country).build();
    }


    //butun ulkelerı getırme vardı sımdı kullanıcıdan code alınan ulkenın bılgılerını getıren yapalım
    @GetMapping("/getcountry/{code}")
    public GenericResponse<CountryDto>getCountry(@PathVariable String code,Locale locale){
        CountryDto country= countryService.getCountry(code);
        String message = messageService.getMessage(I18nConstants.COUNTRY_GET_SUCCESS,locale);
        return GenericResponse.<CountryDto>builder().success(true).message(message).data(country).build();
    }

    @DeleteMapping("/deletecountry/{code}")
    public GenericResponse<CountryDto>deleteCountry(@PathVariable String code,Locale locale) {
       CountryDto country= countryService.deleteCountry(code);
        String message = messageService.getMessage(I18nConstants.COUNTRY_DELETE_SUCCESS,locale);
        return GenericResponse.<CountryDto>builder().success(true).message(message).data(country).build();
    }

    @PutMapping("/putcountry/{code}/{name}")
    public GenericResponse<CountryDto>convertName(@PathVariable String code ,@PathVariable String name,Locale locale){
        CountryDto country=countryService.convertCountry(code,name);
        String message = messageService.getMessage(I18nConstants.COUNTRY_UPDATE_SUCCESS,locale,name);
        return GenericResponse.<CountryDto>builder().success(true).message(message).data(country).build();
    }

    //Hüsna Hanımın istediği uç noktalar
    @GetMapping("/getcountryId/{id}")
    public GenericResponse<CountryDto>getCountryId(@PathVariable Long id,Locale locale){
        CountryDto result=countryService.getCountryId(id);
        String message= messageService.getMessage(I18nConstants.COUNTRY_GET_ID_SUCESSS,locale,id);
        return  GenericResponse.<CountryDto>builder().success(true).message(message).data(result).build();
    }

    @GetMapping("/getcountryName/{name}")
    public GenericResponse<CountryDto>getCountryName(@PathVariable String name,Locale locale){
        CountryDto result = countryService.getCountryName(name);
        String message = messageService.getMessage(I18nConstants.COUNTRY_GET_NAME_SUCCESS,locale);
        return GenericResponse.<CountryDto>builder().success(true).message(message).data(result).build();
    }

    @GetMapping("/all/names")
    public GenericResponse<List<String>>getAllCountryNames(Locale locale){
        List<String> result = countryService.getAllCountriesName();
        String message = messageService.getMessage(I18nConstants.COUNTRY_GET_ALL_NAME_COUNTRIES_SUCCESS,locale);
        return GenericResponse.<List<String>>builder().success(true).message(message).data(result).build();
    }

    @GetMapping("/getPhone")
    public GenericResponse<List<CountryDto>>getPhoneCountry(@RequestParam int phone , Locale locale){
        List<CountryDto> result = countryService.getPhoneCountry(phone);
        String message =  messageService.getMessage(I18nConstants.COUNTRY_GET_PHONE_COUNTRY_SUCCESS,locale);
        return GenericResponse.<List<CountryDto>>builder().success(true).message(message).data(result).build();
    }

    @GetMapping("/continent")
    public GenericResponse <List<CountryDto>>getContinentCountry(@RequestParam String continent,Locale local){
        List<CountryDto> result = countryService.getContinentCountry(continent);
        String message = messageService.getMessage(I18nConstants.COUNTRY_GET_CONTINENT_COUNTRY_SUCCESS,local,continent);
        return GenericResponse.<List<CountryDto>>builder().success(true).message(message).data(result).build();
    }

    @GetMapping("/language")
    public GenericResponse<List<CountryDto>> getlanguageCountry(@RequestParam String language,Locale locale){
        List<CountryDto> result = countryService.getCountryLanguage(language);
        String message = messageService.getMessage(I18nConstants.LANGUAGE_GET_COUNTRY_SUCCESS,locale,language);
        return GenericResponse.<List<CountryDto>>builder().success(true).message(message).data(result).build();
    }

}
