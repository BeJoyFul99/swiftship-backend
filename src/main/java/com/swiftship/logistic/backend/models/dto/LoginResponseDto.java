package com.swiftship.logistic.backend.models.dto;

import com.swiftship.logistic.backend.models.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private Optional<String> token;
    private String message;
    private StatusCode code; // A custom success code
}
