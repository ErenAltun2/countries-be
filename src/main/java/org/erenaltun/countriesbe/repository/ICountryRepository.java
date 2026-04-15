package org.erenaltun.countriesbe.repository;

import jakarta.transaction.Transactional;
import org.erenaltun.countriesbe.dto.CountryDto;
import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    //jpql sorgusu   burada query yazdık countrylanguages ın ıcınde language degıskenı country de lıste seklınde oldugu ıcın where de fıltrelerken yaptıgım gıbı . koyup dıger sınıfa atlayamıyorum.
    @Query("SELECT c FROM Country c JOIN c.countryLanguages cl WHERE cl.language.code = :langCode")
    List<Country> findCountriesByLanguageCode(@Param("langCode") String langCode);

    @Query("SELECT DISTINCT c.currency FROM Country c WHERE c.currency IS NOT NULL")
    List<String>findCurrencyAll();

    @Query("SELECT c.phone FROM Country c ORDER BY c.phone")
    List<String>phoneByAscending();

    @Query("SELECT c.phone FROM Country c ORDER BY c.phone desc ")
    List<String>phoneByDescending();
}
