package org.erenaltun.countriesbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.exception.CountryAlreadyExistsException;
import org.erenaltun.countriesbe.exception.CountryNotFoundException;
import org.erenaltun.countriesbe.initializer.CountryInitializer;
import org.erenaltun.countriesbe.mapper.ICountryMapper;
import org.erenaltun.countriesbe.repository.ICountryRepository;
import org.erenaltun.countriesbe.service.interfaces.ICountryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //otomatık bır sekılde ınject edıyor yoksa ben countryrepository ı constructor olusturarak tanımlamam gerekıyordu.
public class CountryService implements ICountryService {
    private final ICountryRepository countryRepository;
    private final ICountryMapper countryMapper;


    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    //test yazıldı
    @Override
    public List<Country> insertCountries() {

        return countryRepository.saveAll(CountryInitializer.readCountry());
    }

    //test
    @Override
    public Country insertCountry(Country country) {
        //burada bızım daha once kayıtlı ulke var mı dıye bakmamız gerekıyor.
            boolean existingCountry = countryRepository.findByCode(country.getCode()).isPresent();
                if(existingCountry){
                    throw new CountryAlreadyExistsException();
                }
            return countryRepository.save(country);
    }

    //testı yazıldı
    @Override
    public Country getCountry(String code) {
        var result =countryRepository.findByCode(code);
        if(result.isEmpty()){
            throw new CountryNotFoundException();
        }
        return result.get();
    }

    //testı yazıldı
    @Override
    public Country deleteCountry(String code) {
        Country country = getCountry(code);
        if(country.getCode().isEmpty()){
            throw new CountryNotFoundException();
        }else{
            countryRepository.deleteByCode(code);
            return country;
        }
    }

    //testı yazıldı
    @Override
    public Country convertCountry(String code, String name) {
        Country country = getCountry(code);

        country.setName(name);

        return countryRepository.save(country);
    }
}
