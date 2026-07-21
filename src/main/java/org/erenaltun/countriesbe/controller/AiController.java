//package org.erenaltun.countriesbe.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.erenaltun.countriesbe.service.interfaces.IAiAdvisorService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/ai")
//@RequiredArgsConstructor
//public class AiController {
//
//    private final IAiAdvisorService aiAdvisorService;
//
//    @GetMapping("/ask")
//    public ResponseEntity<String> askAi(@RequestParam String query) {
//        return ResponseEntity.ok(aiAdvisorService.getAdvice(query));
//    }
//}