package org.erenaltun.countriesbe.mapper;

import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.dto.LanguageDto;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.entity.CountryLanguage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ICountryMapper {

    // 1. DTO -> Entity (Kaydederken kullanıyorsun)
    @Mapping(source = "languages", target = "countryLanguages")   //burada mapleme ıslemı yapıyoruz cunku dtodakı languageler ıle entıty dekı langueler turlerı farklı ve ısımlendırmelerı farklı
    Country toCountry(CountryDto countryDto);

    // 2. Entity -> DTO (Ekrana basarken kullanıyorsun)
    @Mapping(source = "countryLanguages", target = "languages")
    CountryDto fromCountry(Country country);

    List<CountryDto> fromCountryList(List<Country> countries);

    // --- LİSTE ELEMANLARININ DÖNÜŞÜMÜ ---

    // DTO'daki LanguageDto'yu alıp Kavşak tablosuna (CountryLanguage) çevirme kuralı
    @Mapping(source = "code", target = "language.code")
    CountryLanguage toCountryLanguage(LanguageDto languageDto);

    // Kavşak tablosunu alıp DTO'ya çevirme kuralı
    @Mapping(source = "language.code", target = "code")
    LanguageDto fromCountryLanguage(CountryLanguage countryLanguage);

    //bu ıkısını elle kendım kullanmıyorum yukarıda dedıgım tocountry ve fromcountry ıcın hazırladım mapper otomatık kullanıyor.

}