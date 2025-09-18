package com.swiftship.logistic.backend.configs;

import com.swiftship.logistic.backend.models.entities.User;
import com.swiftship.logistic.backend.services.JwtBlacklistService;
import com.swiftship.logistic.backend.services.JwtService;
import com.swiftship.logistic.backend.services.MyUserDetailsService;
import com.swiftship.logistic.backend.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService tokenProvider;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtBlacklistService jwtBlacklistService; // Inject JwtBlacklistService

    public JwtAuthenticationFilter(JwtService tokenProvider,
                                   MyUserDetailsService myUserDetailsService,
                                   JwtBlacklistService jwtBlacklistService) { // Add to constructor
        this.tokenProvider = tokenProvider;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtBlacklistService = jwtBlacklistService; // Assign
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // ... (Your existing request debugging logs) ...

        try {
            String jwt = this.tokenProvider.getJwtFromRequest(request);
            if (StringUtils.hasText(jwt)) {
                // --- NEW: Check if token is blacklisted ---
                if (jwtBlacklistService.isBlacklisted(jwt)) {
                    log.warn("Attempted use of blacklisted JWT: {}", jwt);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                    return; // Stop processing this request
                }
                // --- END NEW CHECK ---

                if (tokenProvider.validateToken(jwt)) {
                    Claims claims = tokenProvider.getClaimsFromJwt(jwt);
                    String username = claims.getSubject();
                    UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }


}