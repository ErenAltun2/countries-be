package org.erenaltun.countriesbe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.erenaltun.countriesbe.dto.*;
import org.erenaltun.countriesbe.service.impl.AuthService;
import org.erenaltun.countriesbe.service.impl.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<String> register (@Valid @RequestBody RegisterRequest request){
        String response = authService.register(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@Valid @RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return  ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request){
        RefreshTokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String>logout(@AuthenticationPrincipal UserDetails userDetails){
        String response = authService.logout(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

//    Controller katmanında @AuthenticationPrincipal anotasyonunu kullandığında Spring'e şunu demiş oluyorsun:
//
//            "Ey Spring, filtre katmanında doğrulayıp o güvenli kutuya (SecurityContext) koyduğun aktif kullanıcının bilgilerini bana getir."
}
