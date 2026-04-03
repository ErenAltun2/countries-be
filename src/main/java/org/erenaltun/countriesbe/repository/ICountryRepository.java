package org.erenaltun.countriesbe.repository;

import org.erenaltun.countriesbe.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICountryRepository extends JpaRepository<Country,String> {

    Optional<Country> findByCode(String code);
    //Optional tanımladıgımız ıcın null degerlerıde alabılıyoruz c#da kı string? demek gibi.
}
