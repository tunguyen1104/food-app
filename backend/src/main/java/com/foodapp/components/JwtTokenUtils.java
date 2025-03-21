package com.foodapp.components;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.User;
import com.foodapp.exceptions.AppException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
                    .signWith(getSignKey())
                    .compact();
        } catch (Exception e) {
            logger.info("Error: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .decryptWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractPhone(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public SecretKey getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        if (bytes.length < 32) {
            throw new IllegalArgumentException("Invalid secret key: must be at least 32 bytes for HS256.");
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean validateToken(String token, User user) {
        try {
            String phone = extractPhone(token);
            return user.getPhone().equals(phone) && !isTokenExpired(token);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }
}
