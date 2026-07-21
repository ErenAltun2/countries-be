package org.erenaltun.countriesbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.dto.FrankfurterResponseDto;
import org.erenaltun.countriesbe.service.interfaces.ICurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements ICurrencyService {

    private final WebClient webClient; // Config'den gelen redirect ayarlı WebClient

    @Override
    public Double getRateToTry(String currencyCode) {
        if (currencyCode == null || currencyCode.trim().isEmpty() || currencyCode.equalsIgnoreCase("TRY")) return 1.0;

        // SİHİRLİ DOKUNUŞ: Virgüllü gelirse ilkini al (Örn: "USD,USN" -> "USD")
        String cleanCode = currencyCode.split(",")[0].trim();

        try {
            FrankfurterResponseDto response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/latest")
                            .queryParam("from", cleanCode) // Temizlenmiş kodu gönderiyoruz
                            .queryParam("to", "TRY")
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(FrankfurterResponseDto.class)
                    .block();

            if (response != null && response.getRates() != null && response.getRates().containsKey("TRY")) {
                return response.getRates().get("TRY").doubleValue();
            }
        } catch (Exception e) {
            // Log alırken hangi kodun hata verdiğini görmek için cleanCode'u yazdıralım
            System.err.println("Kur çekilirken hata (" + cleanCode + "): " + e.getMessage());
        }
        return null;
    }

    @Override
    public Map<String, Double> getTop5Currencies() {
        try {
            FrankfurterResponseDto response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("from", "TRY").build())
                    .retrieve()
                    .bodyToMono(FrankfurterResponseDto.class)
                    .block();

            if (response == null || response.getRates() == null) {
                return Collections.emptyMap();
            }

            return response.getRates().entrySet().stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), 1.0 / entry.getValue().doubleValue()))
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
        } catch (Exception e) {
            System.err.println("Top 5 API hatası: " + e.getMessage());
            return Collections.emptyMap();
        }
    }
}