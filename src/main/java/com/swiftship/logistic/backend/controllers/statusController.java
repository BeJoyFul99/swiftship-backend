package com.swiftship.logistic.backend.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class statusController {

    @GetMapping(path = "/")
    public String helloWorld() {
        log.info("Got a Connection");
        return "Hello World!!!";
    }

    @GetMapping(path = "/api/status")
    public HttpStatus status() {
        return HttpStatus.OK;
    }
}
