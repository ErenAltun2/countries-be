package org.erenaltun.countriesbe.repository;

import jakarta.transaction.Transactional;
import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICountryRepository extends JpaRepository<Country,Long> {

    Optional<Country> findByCode(String code);    //Optional tanımladıgımız ıcın null degerlerıde alabılıyoruz c#da kı string? demek gibi.
    @Transactional //Her şey tamamsa işlemi bitir der.
    @Modifying //okuma ıslemlerınde kullanmaya gerek yok lakın update ve delete ıslemlerınde verı tabanında bır seyler degısecek dıye uyarmamız gerekıyor sprıng boot u
    void deleteByCode(String code);

    Optional<Country> findByName(String name);

    List<Country> findByPhone(int phone);

    List<Country>findByContinent(String continent);

    List<Country>findByLanguage(String language);

}
