package org.erenaltun.countriesbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="countries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//burada @Data da dıyebılırdık ama onun ıcınde sadece getter setter yok o yuzden performans sıkıntısı cekebılırız.
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique=true)
    private String code;

    private String name;

    private String nativeName;

    private int phone;

    private String capital;

    private String continent;

    private String currency;

    private String flag;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CountryLanguage> countryLanguages;

}

