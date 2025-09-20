package com.swiftship.logistic.backend.configs;

import com.swiftship.logistic.backend.services.JwtBlacklistService;
import com.swiftship.logistic.backend.services.JwtService;
import com.swiftship.logistic.backend.services.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig {
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;

    public WebConfig(JwtBlacklistService jwtBlacklistService, JwtService jwtService, MyUserDetailsService myUserDetailsService) {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(this.jwtService, this.myUserDetailsService, this.jwtBlacklistService);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:8080", "https://swiftship-ten.vercel.app/")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PREFLIGHT")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

