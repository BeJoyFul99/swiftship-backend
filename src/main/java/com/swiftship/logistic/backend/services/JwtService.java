package com.swiftship.logistic.backend.services;

import com.swiftship.logistic.backend.models.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Getter
    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;
    private SecretKey cachedSigningKey;

    private SecretKey getSigningKey() {
        if (this.cachedSigningKey == null) {
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            this.cachedSigningKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return cachedSigningKey;
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + this.jwtExpirationInMs);

        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .and()
                .signWith(this.getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }


    public Claims getClaimsFromJwt(String token) {
        return Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    // Your existing getJwtFromRequest method
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (
                SignatureException ex) {
            System.err.println("Invalid JWT signature: " + ex.getMessage());
        } catch (
                MalformedJwtException ex) {
            System.err.println("Invalid JWT token: " + ex.getMessage());
        } catch (
                ExpiredJwtException ex) { // <-- THIS CATCHES EXPIRED TOKENS
            System.err.println("Expired JWT token: " + ex.getMessage());
        } catch (
                UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT token: " + ex.getMessage());
        } catch (
                IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty: " + ex.getMessage());
        }
        return false; // Token is invalid for any of the above reasons
    }
}
