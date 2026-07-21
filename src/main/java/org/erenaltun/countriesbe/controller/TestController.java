package org.erenaltun.countriesbe.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.erenaltun.countriesbe.entity.User;
import org.erenaltun.countriesbe.service.impl.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final AuthService authService;

    @PostMapping("/public")
    public ResponseEntity<String> publicEndpoint(){
        return ResponseEntity.ok("token a ihtiyacınız yok.");
    }

    @PostMapping("/protected")
    public ResponseEntity<String> protectedEndpoint(){
        return ResponseEntity.ok("başarılı protected endpıintini kullanarak gırıs yaptınız.");

    }

    @GetMapping("/user")
    public ResponseEntity<List<User>>getAlluser(){
        return ResponseEntity.ok(authService.topList());

    }

}
