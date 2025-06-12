package com.yaseen.ChatHub.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtTokenProvider {

    SecretKey key= Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET_KEY.getBytes());

    public String generateToken(Authentication authentication){
        return Jwts.builder().setIssuer("Chat Hub")
                .claim("email",authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .signWith(key)
                .compact();
    }

    public String getEmailFromJwtToken(String jwt){
        jwt=jwt.substring(7);
        Claims claims=Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email=String.valueOf(claims.get("email"));
        return email;
    }

}
