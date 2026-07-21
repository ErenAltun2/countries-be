package org.erenaltun.countriesbe.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//jwt utıl bıze token uretmek ıcın yapıyoruz.

@Component
public class JwtUtil {
    //buraya key ı ve gecerlılık suresının @value ıle eklıyoruz.

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;


    // text olarak yazdığımız key'i, kriptografi
    //kütüphanesinin anlayacağı byte formatına çeviriyoruz
    //String → byte[] dönüşümü
    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(String username){
        Map<String, Object> claims=new HashMap<>();
        return createToken(claims,username);
    }


    //token uretır
    public String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+accessTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    //bu ıse tokenı parcalar ıcerısındekı degerlerı dogrulamak ıcın yapar bu ıslemı
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //sadece tokenı parcaladıktan sonra ısmı dondurur
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();

    }

    public Date extractExpiration(String Token){
        return extractAllClaims(Token).getExpiration();
    }

    public Boolean isTokenExpired(String Token){
        return extractAllClaims(Token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token,String username){
        final String extractedUserName=extractUsername(token);
        return (extractedUserName.equals(username) && !isTokenExpired((token)));
    }


}
