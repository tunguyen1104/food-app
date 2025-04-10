package com.foodapp.components;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.User;
import com.foodapp.exceptions.AppException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Value("${application.security.jwt.expiration}")
    private Integer expiration;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 bytes (256 bits) for HS256");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        try {
            return Jwts.builder()
                    .claim("userId", user.getId())
                    .claim("username", user.getUsername())
                    .claim("phone", user.getPhone())
                    .claim("fullName", user.getFullName())
                    .subject(user.getPhone())
                    .expiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .issuedAt(Date.from(Instant.now()))
                    .signWith(signingKey, Jwts.SIG.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("Error generating token: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)  // Changed from decryptWith
                    .build()
                    .parseSignedClaims(cleanToken(token))
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }

    public String extractPhone(String token) {
        return extractAllClaims(token).getSubject();
    }

    public SecretKey getSignKey() {
        return signingKey;
    }

    public boolean validateToken(String token, User user) {
        String phone = extractPhone(token);
        return user.getPhone().equals(phone) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private String cleanToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}