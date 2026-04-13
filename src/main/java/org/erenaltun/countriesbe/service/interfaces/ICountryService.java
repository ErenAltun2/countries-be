package org.erenaltun.countriesbe.service.interfaces;

import org.erenaltun.countriesbe.entity.Country;

import java.util.List;
import java.util.Optional;

public interface ICountryService {
    List<Country>getAllCountries();
    List<Country>insertCountries();
    Country insertCountry(Country country);
    Country getCountry(String code);
    Country deleteCountry(String code);
    Country convertCountry(String code , String name);
}
