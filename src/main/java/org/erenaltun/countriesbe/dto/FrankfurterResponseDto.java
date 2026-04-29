package org.erenaltun.countriesbe.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FrankfurterResponseDto {
    private Double amount;
    private String base;
    private String date;
    private Map<String, Double> rates; // Para birimlerini ve kurlarını tutacak map
}


//tarayıcıdan https://api.frankfurter.dev/v1/latest?from=USD&to=TRY  url sıne ıstek attım ve bana donen cevap şu şekılde
//        {
//        "amount": 1,
//        "base": "USD",
//        "date": "2026-04-28",
//        "rates": {
//        "TRY": 45.055
//        }
//        }
//buna göre dto sınıfı oluşturdum.