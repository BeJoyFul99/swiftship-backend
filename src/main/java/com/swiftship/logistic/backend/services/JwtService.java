package com.swiftship.logistic.backend.services;

import com.swiftship.logistic.backend.models.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

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

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token);

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
