package org.erenaltun.countriesbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.entity.RefreshToken;
import org.erenaltun.countriesbe.entity.User;
import org.erenaltun.countriesbe.repository.RefreshTokenRepository;
import org.erenaltun.countriesbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public RefreshToken createRefreshToken(String username){
        User user = userRepository.findByUserName(username).orElseThrow(()->new RuntimeException("kullanıcı bulunamadı"));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .createdAt(Instant.now())
                .build();
        return refreshTokenRepository.save(refreshToken);

    }
    @Transactional(noRollbackFor = RuntimeException.class)
    public RefreshToken verifyExpiration(RefreshToken token){
            if(token.getExpiryDate().isBefore(Instant.now())){
                refreshTokenRepository.delete(token);
                throw new RuntimeException("token ın zamanı gecmıs tekrardan oturum acmanız gerekmekte.");
            }
            return token;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteByUserId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("kullanıcı bulunamadı"));
        refreshTokenRepository.deleteByUser(user);
    }


}
