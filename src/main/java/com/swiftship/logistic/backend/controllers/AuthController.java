package com.swiftship.logistic.backend.controllers;

import com.swiftship.logistic.backend.models.dto.LoginDto;
import com.swiftship.logistic.backend.models.dto.LoginResponseDto;
import com.swiftship.logistic.backend.models.entities.User;
import com.swiftship.logistic.backend.models.enums.StatusCode;
import com.swiftship.logistic.backend.services.AuthService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Log
public class AuthController {
    public final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //learning
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticateUser(@RequestBody LoginDto loginDto) {
        log.info("Got a Connection");
        log.info(loginDto.getEmail() + " " + loginDto.getPassword());
        try {
            User user = authService.authenticate(loginDto);
            // Assuming you have a JWT token generation service
            String token = "your_generated_jwt_token";
            LoginResponseDto responseDto = new LoginResponseDto(
                    Optional.of(token),
                    "User logged in successfully!",
                    StatusCode.LOGIN_SUCCESS
            );
            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        } catch (UsernameNotFoundException ex) {
            LoginResponseDto resDto = new LoginResponseDto(
                    null,
                    ex.getMessage(),
                    StatusCode.INVALID_CREDENTIALS
            );
            return new ResponseEntity<>(resDto, HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException ex) {
            LoginResponseDto resDto = new LoginResponseDto(
                    null,
                    ex.getMessage(),
                    StatusCode.INVALID_CREDENTIALS
            );
            return new ResponseEntity<>(resDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            LoginResponseDto resDto = new LoginResponseDto(
                    null,
                    ex.getMessage(),
                    StatusCode.INTERNAL_SERVER_ERROR
            );
            return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
