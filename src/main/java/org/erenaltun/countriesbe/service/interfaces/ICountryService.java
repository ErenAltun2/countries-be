package org.erenaltun.countriesbe.service.interfaces;

import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.entity.Country;

import java.util.List;
import java.util.Optional;

public interface ICountryService {
    List<CountryDto>getAllCountries();
    List<CountryDto>insertCountries();
    CountryDto insertCountry(CountryDto country);
    CountryDto getCountry(String code);
    CountryDto deleteCountry(String code);
    CountryDto convertCountry(String code , String name);
    CountryDto getCountryId(Long id);
    CountryDto getCountryName(String name);
    List<String> getAllCountriesName();
    List<CountryDto>getPhoneCountry(int phone);
    List<CountryDto>getContinentCountry(String continent);
    List<CountryDto> getCountryLanguage(String language);
    List<String>getCurrency();
    List<String>getPhoneAscending();
    List<String>getPhoneDescending();


}
