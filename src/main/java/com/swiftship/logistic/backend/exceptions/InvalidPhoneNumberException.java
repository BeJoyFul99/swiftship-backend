package com.swiftship.logistic.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// InvalidPhoneNumberException.java
@ResponseStatus(HttpStatus.BAD_REQUEST) // Maps to HTTP 400
public class InvalidPhoneNumberException extends RuntimeException {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
