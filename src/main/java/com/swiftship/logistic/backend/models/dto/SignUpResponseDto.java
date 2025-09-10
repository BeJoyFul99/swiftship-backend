package com.swiftship.logistic.backend.models.dto;

import com.swiftship.logistic.backend.models.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class SignUpResponseDto {
    private String jwt_token;
    private String message;
    private StatusCode code;
    private UserResponseDto user;
}
