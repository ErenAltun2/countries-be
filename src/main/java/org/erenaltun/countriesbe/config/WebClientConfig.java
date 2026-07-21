package org.erenaltun.countriesbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector; // BU IMPORT GEREKLİ
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        // 1. Motoru oluşturuyoruz (Yönlendirmeleri takip et)
        HttpClient httpClient = HttpClient.create().followRedirect(true);

        return builder
                .baseUrl("https://api.frankfurter.app")
                // 2. İŞTE EKSİK OLAN SATIR! Motoru WebClient'a bağlıyoruz:
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}