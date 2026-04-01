package org.erenaltun.countriesbe.controller;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.service.interfaces.ICountryService;
import org.erenaltun.countriesbe.util.constants.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Api.Country.BASE_URL)
@RequiredArgsConstructor
public class CountryController {

    private final ICountryService countryService;

    @GetMapping
    public List<Country>getAllCountries(){

        return countryService.getAllCountries();
    }
    @PostMapping(Api.Country.INSERT_ALL)
    public List<Country>insertCountries(){
        return countryService.insertCountries();
    }
}
