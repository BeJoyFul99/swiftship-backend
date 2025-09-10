package com.swiftship.logistic.backend.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
@AllArgsConstructor
public class SignUpDto {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String phone;
}
