//package org.erenaltun.countriesbe.service.impl;
//
//import org.erenaltun.countriesbe.service.interfaces.IAiAdvisorService;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Map;
//
//@Service
//@Primary // Birden fazla implementasyon olursa öncelikli bu çalışır
//public class HuggingFaceAdvisorServiceImpl implements IAiAdvisorService {
//
//    private final WebClient webClient;
//
//    // Hugging Face'den alacağın ücretsiz Access Token
//    private final String API_TOKEN = "hf_xxxxxxxxxxxxxxxxxxxx";
//
//    public HuggingFaceAdvisorServiceImpl(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder
//                .baseUrl("https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.2")
//                .defaultHeader("Authorization", "Bearer " + API_TOKEN)
//                .build();
//    }
//
//    @Override
//    public String getAdvice(String prompt) {
//        Map<String, Object> body = Map.of("inputs", prompt);
//
//        return webClient.post()
//                .bodyValue(body)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block(); // Şimdilik senkron sonuç alalım
//    }
//}