package com.friendbook.userservice.security;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {
    private SecretKeyProvider secretKeyProvider;

    public JwtService() {
        this(null);
    }

    @Autowired
    public JwtService(SecretKeyProvider secretKeyProvider) {
        this.secretKeyProvider = secretKeyProvider;
    }

    public String tokenFor(String login) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();

        String uuid = UUID.randomUUID().toString().replace("-", "");
        Date expiration = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(expiration);
        c.add(Calendar.MINUTE, 30);
        expiration = c.getTime();
        return Jwts.builder()
                .setId(uuid)
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String refreshTokenFor(String login) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();

        String uuid = UUID.randomUUID().toString().replace("-", "");
        Date expiration = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(expiration);
        c.add(Calendar.MONTH, 6);
        expiration = c.getTime();
        return Jwts.builder()
                .setId(uuid)
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Jws<Claims> verify(String token) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }
}
