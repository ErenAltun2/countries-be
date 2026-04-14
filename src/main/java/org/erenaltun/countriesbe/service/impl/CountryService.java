package org.erenaltun.countriesbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.exception.ContinentNotFoundException;
import org.erenaltun.countriesbe.exception.CountryAlreadyExistsException;
import org.erenaltun.countriesbe.exception.CountryNotFoundException;
import org.erenaltun.countriesbe.exception.LanguageNotFoundException;
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

    //testi yazıldı
    @Override
    public List<CountryDto> insertCountries() {
        List<Country> result = countryRepository.saveAll(CountryInitializer.readCountry());
        return countryMapper.fromCountryList(result);
        //map struct sayesınde dırekt olarak eslestırıp kaydedıyor
    }

    //test
    @Override
    public CountryDto insertCountry (CountryDto countryDto) {
        Country country=countryMapper.toCountry(countryDto);
        //burada bızım daha once kayıtlı ulke var mı dıye bakmamız gerekıyor.
            boolean existingCountry = countryRepository.findByCode(country.getCode()).isPresent();
                if(existingCountry){
                    throw new CountryAlreadyExistsException();
                }
                countryRepository.save(country);
                return countryDto;
    }

    //testı yazıldı
    @Override
    public CountryDto getCountry(String code) {
        Optional<Country> result =countryRepository.findByCode(code);
        if(result.isEmpty()){
            throw new CountryNotFoundException();
        }
        CountryDto countryDto = countryMapper.fromCountry(result.get());
        return countryDto;
    }

    //testı yazıldı
    @Override
    public CountryDto deleteCountry(String code) {
        CountryDto countryDto = getCountry(code);
        if(countryDto.getCode().isEmpty()){
            throw new CountryNotFoundException();
        }else{
            countryRepository.deleteByCode(code);
            return countryDto;
        }
    }

    //testı yazıldı
    @Override
    public CountryDto convertCountry(String code, String name) {
        CountryDto result = getCountry(code);
        Country country = countryMapper.toCountry(result);
        country.setName(name);
        Country resultcountry=countryRepository.save(country);
        CountryDto countrydto=countryMapper.fromCountry(resultcountry);
        //save metodu yalnızca entıty sınıfını kabul eder .
        return  countrydto;
    }

    //Hüsna Hanımın istediği uç noktalar

    @Override
    public CountryDto getCountryId(Long code){
        Optional<Country> country = countryRepository.findById(code);
        if(country.isEmpty()){
            throw new CountryNotFoundException();
        }else{
            CountryDto result = countryMapper.fromCountry(country.get());
            return result;
        }
    }

    @Override
    public CountryDto getCountryName(String name){
        Optional<Country> country = countryRepository.findByName(name);
        //null olabılme ıhtımalıne karsı yaptım sımdı kontrol edecegız
        if(country.isEmpty()){
            throw new CountryNotFoundException();
        }else{
            CountryDto result = countryMapper.fromCountry(country.get()); //optıonal ıle aldıgımız zarfı get ıle acıp ıcındekı bılgıye bakıyoruz normalde get ıle guvenlı degıl lakın null olup olmadıgını kontrol edıyoruz sıkıntı olmaz.
            return result;
        }
    }

    @Override
    public List<String> getAllCountriesName(){
        List<Country> result = countryRepository.findAll();
        // map struct kullanabılırdık ama hem farklılık olması acısından hemde stream apı kullanımı sadece ısım cekmek ıcın daha efektıf
        return result.stream().map(Country::getName).toList();
    }

    @Override
    public List<CountryDto> getPhoneCountry(int phone){
        List<Country>result = countryRepository.findByPhone(phone);
        if(result.isEmpty()){
            throw new CountryNotFoundException();
        }else {
            List<CountryDto> countryDtoList=countryMapper.fromCountryList(result);
            return countryDtoList;
        }
    }

    @Override
    public List<CountryDto> getContinentCountry(String continent){
        List<Country> result = countryRepository.findByContinent(continent);
        if(result.isEmpty()){
            throw new ContinentNotFoundException();
        }else{
            List<CountryDto> countryDto=countryMapper.fromCountryList(result);
            return countryDto;
        }
    }

    @Override
    public List<CountryDto> getCountryLanguage(String language){
        List<Country> result = countryRepository.findByLanguage(language);
        if(result.isEmpty()){
            throw new LanguageNotFoundException();
        }else{
            return countryMapper.fromCountryList(result);
        }
    }




}
