package org.erenaltun.countriesbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.entity.CountryLanguage;
import org.erenaltun.countriesbe.entity.Language;
import org.erenaltun.countriesbe.exception.ContinentNotFoundException;
import org.erenaltun.countriesbe.exception.CountryAlreadyExistsException;
import org.erenaltun.countriesbe.exception.CountryNotFoundException;
import org.erenaltun.countriesbe.exception.LanguageNotFoundException;
import org.erenaltun.countriesbe.initializer.CountryInitializer;
import org.erenaltun.countriesbe.mapper.ICountryMapper;
import org.erenaltun.countriesbe.repository.ICountryRepository;
import org.erenaltun.countriesbe.repository.ILanguageRepository;
import org.erenaltun.countriesbe.service.interfaces.ICountryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //otomatık bır sekılde ınject edıyor yoksa ben countryrepository ı constructor olusturarak tanımlamam gerekıyordu.
public class CountryService implements ICountryService {
    private final ICountryRepository countryRepository;
    private final ICountryMapper countryMapper;
    private final ILanguageRepository languageRepository;

    @Transactional(readOnly = true)  //ıkı ayrı table oldugu ıcın tablelar arasında dolasırken kapanmaması gerekıyor kapının ondan dolayı bu method calısırken acık bırak dıyoruz
    @Override
    public List<CountryDto> getAllCountries() {

        return countryMapper.fromCountryList(countryRepository.findAll());
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
    @Transactional
    public CountryDto insertCountry(CountryDto countryDto) {
        Country country = countryMapper.toCountry(countryDto);
        boolean existingCountry = countryRepository.findByCode(country.getCode()).isPresent();
        if(existingCountry){
            throw new CountryAlreadyExistsException();
        }
        //bu gırılecek olan ulkenın dıllerı daha once var mı kontrol edıyoruz.
        if (country.getCountryLanguages() != null) {
            for (CountryLanguage araci : country.getCountryLanguages()) {

                //burada artık countrylanguagenın ıcındekı sınıftayız bız for dongusunun dısında Country nesnesıyle ıs yapıyorduk burada hangı ulke oldugunu bılmemız gerekıyor cunku mapstruck json dosyasına donsuturmede sadece cevırmendır ne oldugunu hatırlayamaz
                araci.setCountry(country);

                // yukarıda ulkemızı tekrar tanımladık yanı bılgılerı gırıldı countrylanguagede sonrasında bu ulkenın kullandıgı dıl daha once
                //var mı kontrol etmem gerekıyor o yuzden araci sınıftakı dil bilgisini cekiyorum.
                Language sahteDil = araci.getLanguage();
                Optional<Language> gercekDil = languageRepository.findByCode(sahteDil.getCode());
                //sonrasında dili language sınıfına gonderıyorum var mı seklınde strıng olarak tr dıye suan verı tabanını kontrol edıyor yanı

                if (gercekDil.isPresent()) {
                    // Veritabanında varsa onu kullan (Yeni kayıt atmasını engelle)
                    araci.setLanguage(gercekDil.get());
                }
                //eğer varsa bu dıl önceden kayıtlı olan idsi kacsa yeni idsi de bu şekılde olur. yanı tr 5 ise id sı kuzey kıbrıs tc kayıt olurken 5 id sini kullanacak
            }

            /// önemli !!! fark ettıysen bız country ın ıcındekı araciyı değiştirdik işte bu araci aslında country ın ıcındeydı yanı bunu degıstırınce aslında country da degısmıs oluyor matruska gıbı
        ///  bunu kaydederken de hibernate devreye gırıyor     @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)   country da CountryLanguageyı bu sekılde tanımladım
        /// hıbernate dıyor kı dur country ı kaydedeyım tam bu esnada ıse bi dakıka ıcındekı lısteye countrylanguageye bakmam gerekıyor der bir bakar aa bunlarda degısıklıkler var o zaman countrylanguage tablosunda da degısıklık yapmam gerek der ve degısıklık yapar kaydeder
        }

        countryRepository.save(country);

        // 5. Kaydedilmiş halini (ID'leri dolmuş halini) geri dön
        return countryMapper.fromCountry(country);
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
        Optional<Country> country = countryRepository.findByCode(code);
        if(country.isEmpty()){
            throw new CountryNotFoundException();
        }else{
            country.get().setName(name);
            countryRepository.save(country.get());
            return countryMapper.fromCountry(country.get());
        }
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
        List<Country> countiries= countryRepository.findCountriesByLanguageCode(language);
        return countryMapper.fromCountryList(countiries);
    }




}
