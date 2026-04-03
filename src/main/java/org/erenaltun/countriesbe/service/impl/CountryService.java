package org.erenaltun.countriesbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.exception.CountryAlreadyExistsException;
import org.erenaltun.countriesbe.initializer.CountryInitializer;
import org.erenaltun.countriesbe.repository.ICountryRepository;
import org.erenaltun.countriesbe.service.interfaces.ICountryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //otomatık bır sekılde ınject edıyor yoksa ben countryrepository ı constructor olusturarak tanımlamam gerekıyordu.
public class CountryService implements ICountryService {
    private final ICountryRepository countryRepository;


    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public List<Country> insertCountries() {

        return countryRepository.saveAll(CountryInitializer.readCountry());
    }

    @Override
    public Country insertCountry(Country country) {
        //burada bızım daha once kayıtlı ulke var mı dıye bakmamız gerekıyor.
            boolean existingCountry = countryRepository.findByCode(country.getCode()).isPresent();
                if(existingCountry){
                    throw new CountryAlreadyExistsException();
                }
            return countryRepository.save(country);
    }

}
