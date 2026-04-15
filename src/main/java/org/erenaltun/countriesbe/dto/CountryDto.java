package org.erenaltun.countriesbe.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
//buılder sayesınde object olustururken .name().natıvename() seklınde yazmamıza ızın verıyor constructor kullansaydık sırasını unutursak yazmamız daha zor olabılırdı.
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

    private Long id;

    private String code;

    private String name;

    private String nativeName;

    private int phone;

    private String capital;

    private String continent;

    private String currency;

    private List<LanguageDto> languages;

    private String flag;
}
