package com.swiftship.logistic.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftship.logistic.backend.models.dto.LoginDto;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Log
public class AuthController {
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    //learning
    @PostMapping("/login")
    public String authenticateUser(@RequestBody LoginDto loginDto) {
        log.info("Got a Connection");
        log.info(loginDto.getEmail() + " " + loginDto.getPassword());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> msg = new HashMap<String, String>();
        msg.put("msg", "User login");
        try {
            String jString = objectMapper.writeValueAsString(msg);
            log.info(jString);
            return jString;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
