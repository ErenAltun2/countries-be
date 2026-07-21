package org.erenaltun.countriesbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.erenaltun.countriesbe.dto.*;
import org.erenaltun.countriesbe.entity.RefreshToken;
import org.erenaltun.countriesbe.entity.Role;
import org.erenaltun.countriesbe.entity.User;
import org.erenaltun.countriesbe.repository.UserRepository;
import org.erenaltun.countriesbe.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


    public String register(RegisterRequest request){
        if(userRepository.existsByUserName(request.getUsername())){
            throw new RuntimeException("Bu isim ile daha öncesinde kayıt gerçekleşmiş.");
        }
        if(userRepository.existsByEmail(request.getMail())){
            throw new RuntimeException("Bu email adresi daha önce kullanılmış");
        }

        User user = User.builder()
                .userName(request.getUsername())
                .email(request.getMail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return "Kullanını Başarılı Bir şekilde oluşturuldu.";
    }


    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByUserName(request.getUsername()).orElseThrow(()->new RuntimeException("username bulunadı"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("password veya isim yanlış");
        }
        String accessToken = jwtUtil.generateToken(user.getUserName());
        RefreshToken refreshToken=refreshTokenService.createRefreshToken(user.getUserName());

        return new LoginResponse(accessToken,refreshToken.getToken(), user.getUserName(),"başarılı giriş yapıldı.");
    }


    public List<User> topList(){
        return userRepository.findAll();
    }


    public RefreshTokenResponse refreshToken(RefreshTokenRequest request){
        String requestrefreshToken=request.getRefreshToken();

        return refreshTokenService.findByToken(requestrefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user->{String accessToken= jwtUtil.generateToken(user.getUserName());
                                  RefreshToken newRefreshToken=refreshTokenService.createRefreshToken(user.getUserName());

                                  return new RefreshTokenResponse(accessToken,newRefreshToken.getToken(),"basarıyla yenılenmıstır");
                }).orElseThrow(()->new RuntimeException("refresh token yenılenmesınde hata"));
    }


    public String logout(String username){
        User user = userRepository.findByUserName(username).orElseThrow(()->new RuntimeException("kullanıcı bulunamadı logout yaparken hata aldı"));
        refreshTokenService.deleteByUserId(user.getId());
        //kullanıcı cıkıs yaparken refresh token ı sılınmesı gerekmekte bırdaha gırıs yaptıgında yenı refresh token ı alacak zaten
        //refreh token 15 dakıkada bır yenılenıyor.
        return "Çıkış işlemi başarılı";
    }



}
