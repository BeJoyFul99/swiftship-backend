package com.swiftship.logistic.backend.models.dto;

import com.swiftship.logistic.backend.models.entities.Address;
import com.swiftship.logistic.backend.models.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String countryCode;
    private String phoneNumber;
    private UserRole role;

    private Boolean isAccountLocked;
    private Boolean isAccountVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
