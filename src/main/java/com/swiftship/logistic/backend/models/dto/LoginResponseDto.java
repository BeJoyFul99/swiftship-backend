package com.swiftship.logistic.backend.models.dto;

import com.swiftship.logistic.backend.models.entities.User;
import com.swiftship.logistic.backend.models.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Data
@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto extends ResponseDto {
    private UserResponseDto user;
    private String message;
    private StatusCode code;

}
