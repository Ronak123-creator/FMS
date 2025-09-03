package com.backend.foodproject.service.auth;

import com.backend.foodproject.entity.UserInfo;
import com.backend.foodproject.enums.Role;
import com.backend.foodproject.exception.CustomExceptionHandling;
import com.backend.foodproject.repository.UserInfoRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    private final UserInfoRepository userInfoRepository;
    private static final String SECRET = "!@#$asdsasasasdsasasasdsasasaqdsasascvasasasafasasardasasacflksasa";

    public JwtService(UserInfoRepository userInfoRepository){
        this.userInfoRepository = userInfoRepository;
    }

    public String generateToken(String email){
        UserInfo userInfo =userInfoRepository.findByEmail(email)
                .orElseThrow(()->new CustomExceptionHandling("Not Found",
                        HttpStatus.NOT_FOUND.value()));

        Role role = userInfo.getRole();
        Map<String, Object> claims = new HashMap<>();

        claims.put("email", email);
        claims.put("roles", role.name());
        claims.put("id",userInfo.getId());
        claims.put("phoneNumber",userInfo.getPhoneNumber());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return  extractClaims(token, Claims::getExpiration);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);

    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpire(String token){
        return  extractExpiration(token).before(new Date());

    }
    public Boolean validate (String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpire(token));
    }


}
