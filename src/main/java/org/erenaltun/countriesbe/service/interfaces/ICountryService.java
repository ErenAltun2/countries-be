package org.erenaltun.countriesbe.service.interfaces;

import org.erenaltun.countriesbe.entity.Country;

import java.util.List;

public interface ICountryService {
    List<Country>getAllCountries();
    List<Country>insertCountries();
}
