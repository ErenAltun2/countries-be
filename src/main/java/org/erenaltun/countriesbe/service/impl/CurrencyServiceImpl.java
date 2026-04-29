package org.erenaltun.countriesbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.dto.FrankfurterResponseDto;
import org.erenaltun.countriesbe.service.interfaces.ICurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements ICurrencyService {

    private final RestTemplate restTemplate;

    @Override
    public Double getRateToTry(String currencyCode) {
        if (currencyCode == null || currencyCode.equalsIgnoreCase("TRY")) return 1.0;

        try {
            String url = "https://api.frankfurter.app/latest?from=" + currencyCode + "&to=TRY";
            FrankfurterResponseDto response = restTemplate.getForObject(url, FrankfurterResponseDto.class);

            if (response != null && response.getRates() != null && response.getRates().containsKey("TRY")) {
                return response.getRates().get("TRY");
            }
        } catch (Exception e) {
            System.err.println("Kur bilgisi alınamadı: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Map<String, Double> getTop5Currencies() {
        try {
            String url = "https://api.frankfurter.app/latest?from=TRY";
            FrankfurterResponseDto response = restTemplate.getForObject(url, FrankfurterResponseDto.class);

            if (response == null || response.getRates() == null) return Collections.emptyMap();

            return response.getRates().entrySet().stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), 1.0 / entry.getValue()))
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
        } catch (Exception e) {
            System.err.println("Top 5 listesi çekilemedi: " + e.getMessage());
            return Collections.emptyMap();
        }
    }
}