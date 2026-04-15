package org.erenaltun.countriesbe.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {
    private String code;         // Örn: "tr", "es", "en"
}
