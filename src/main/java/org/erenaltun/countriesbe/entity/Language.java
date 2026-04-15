package org.erenaltun.countriesbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="languages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment stratejisi
    private Long id;

    @Column(unique = true, nullable = false) // Tekrar edemez ve boş kalamaz her ülkenin code'u unique
    private String code;

    @OneToMany(mappedBy = "language")
    private List<CountryLanguage> countryLanguages;

}