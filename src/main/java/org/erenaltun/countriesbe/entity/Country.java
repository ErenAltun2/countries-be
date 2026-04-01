package org.erenaltun.countriesbe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//burada @Data da dıyebılırdık ama onun ıcınde sadece getter setter yok o yuzden performans sıkıntısı cekebılırız.
public class Country {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;

    private String code;

    private String name;

    private String nativeName;

    private int phone;

    private String capital;

    private String continent;

    private String currency;

    private String language;

    private String flag;
    //private List<Language>language;

}

