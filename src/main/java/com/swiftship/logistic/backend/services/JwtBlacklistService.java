// /services/JwtBlacklistService.java
package com.swiftship.logistic.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class JwtBlacklistService {

    // Using a simple in-memory set for demonstration.
    // In production, use Redis or a database table with TTL/expiry.
    private final Set<String> blacklistedTokens = Collections.synchronizedSet(new HashSet<>());

    @Value("${jwt.expiration}") // Inject JWT expiration from properties (e.g., 24 hours in ms)
    private long jwtExpirationInMs;

    // A scheduled service to periodically clean up expired tokens from the blacklist
    // (Not needed if using Redis with TTL, as Redis handles expiry automatically)
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public JwtBlacklistService() {
        // Schedule cleanup task to run every hour
        scheduler.scheduleAtFixedRate(this::cleanupExpiredTokens, 1, 1, TimeUnit.HOURS);
    }

    public void blacklistToken(String token) {
        if (token != null && !token.isEmpty()) {
            blacklistedTokens.add(token);
            // In a real Redis scenario, you'd set a TTL on the key:
            // redisTemplate.opsForValue().set(token, true, jwtExpirationInMs, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    // This method is for in-memory cleanup only. Redis handles this automatically.
    private void cleanupExpiredTokens() {
        // For a simple in-memory set, we'd need to store token with expiry time
        // This simple Set implementation doesn't support expiry directly without custom objects.
        // For a more complete in-memory solution, you'd store Map<String, Instant> and check expiry.
        // Example (conceptual, requires modification to blacklistedTokens to store expiry):
        // Iterator<Map.Entry<String, Instant>> iterator = blacklistedTokensWithExpiry.entrySet().iterator();
        // while (iterator.hasNext()) {
        //     Map.Entry<String, Instant> entry = iterator.next();
        //     if (entry.getValue().isBefore(Instant.now())) {
        //         iterator.remove();
        //     }
        // }
        // For now, it just adds, a proper in-memory store would be more complex.
        // This is why Redis is recommended.
    }
}