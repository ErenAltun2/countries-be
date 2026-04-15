package org.erenaltun.countriesbe.repository;

import org.erenaltun.countriesbe.entity.Country;
import org.erenaltun.countriesbe.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ILanguageRepository extends JpaRepository<Language,Long> {

    Optional<Language>findByCode(String language);
}
