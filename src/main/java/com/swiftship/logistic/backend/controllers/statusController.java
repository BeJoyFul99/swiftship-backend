package com.swiftship.logistic.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftship.logistic.backend.services.TestService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log
public class statusController {
    @Value("${spring.application.name}")
    private String appName;

    private TestService testService;

    public statusController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping(path = "/")
    public String helloWorld() {
        log.info("Got a Connection");
        return String.format("Welcome to %s", this.appName);
    }

    @GetMapping(path = "/test-list")
    public ResponseEntity<String> listTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jString = objectMapper.writeValueAsString(this.testService.findAllUsers());
            return new ResponseEntity<>(jString, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping(path = "/api/status")
    public HttpStatus status() {
        return HttpStatus.OK;
    }
}
