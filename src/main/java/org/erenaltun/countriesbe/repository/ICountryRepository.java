package org.erenaltun.countriesbe.repository;

import jakarta.transaction.Transactional;
import org.erenaltun.countriesbe.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICountryRepository extends JpaRepository<Country,String> {

    Optional<Country> findByCode(String code);
    //Optional tanımladıgımız ıcın null degerlerıde alabılıyoruz c#da kı string? demek gibi.
    @Transactional //Her şey tamamsa işlemi bitir der.
    @Modifying //okuma ıslemlerınde kullanmaya gerek yok lakın update ve delete ıslemlerınde verı tabanında bır seyler degısecek dıye uyarmamız gerekıyor sprıng boot u
    Country deleteByCode(String code);

}
