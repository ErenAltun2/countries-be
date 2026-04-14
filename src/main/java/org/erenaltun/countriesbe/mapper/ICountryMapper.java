package org.erenaltun.countriesbe.mapper;

import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.entity.Country;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ICountryMapper {

    Country toCountry(CountryDto countryDto);

    CountryDto fromCountry(Country country);

    List<CountryDto> fromCountryList(List<Country> countries);


//    public Country fromCountryDto(CountryDto countryDto){
//            return Country.builder()
//                    .code(countryDto.getCode())
//                    .name(countryDto.getName())
//                    .nativeName(countryDto.getNativeName())
//                    .phone(countryDto.getPhone())
//                    .capital(countryDto.getCapital())
//                    .continent(countryDto.getContinent())
//                    .currency(countryDto.getCurrency())
//                    .language(countryDto.getLanguage())
//                    .flag(countryDto.getFlag())
//                    .build();
//
//    }
//
//    public CountryDto toCountryDto(Country country){
//        return CountryDto.builder()
//                .id(country.getId())
//                .code(country.getCode())
//                .name(country.getName())
//                .nativeName(country.getNativeName())
//                .phone(country.getPhone())
//                .capital(country.getCapital())
//                .continent(country.getName())
//                .currency(country.getCurrency())
//                .language(country.getLanguage())
//                .flag(country.getFlag())
//                .build();
//    }
}
