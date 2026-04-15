package org.erenaltun.countriesbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "country_languages") // Ara tablomuzun adı
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ara katmandan ulkelere manytoone bakısı
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    // ara katmandan dıllere manytoone bakısı   burada fetchtype ı lazy dıyoruz cunku bız ıstemeden hem dıl tablosundan hem ulke tablosundan verılerı cekmesın
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

}


//En Dış Paket (Country): Sen tetiği buradan çekiyorsun (save). Bu paketin üzerinde "İçindekilere de aynı işlemi uygula" (Cascade) talimatı var.
//
//Orta Paket (CountryLanguage): Hibernate paketi açıyor, içindeki kavşak nesnesini görüyor. Ülkeye ne yapılıyorsa buna da aynısını yapıyor. Ama bakıyor ki bu paketin içinde bir tane daha paket var!
//
//En İç Paket (Language): Hibernate bu paketi de açıyor. Kavşak nesnesine ne oluyorsa (ekleme/güncelleme), içindeki Language nesnesine de aynısını uyguluyor.