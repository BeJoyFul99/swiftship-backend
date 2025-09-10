package com.swiftship.logistic.backend.controllers;

import com.swiftship.logistic.backend.models.dto.*;
import com.swiftship.logistic.backend.models.entities.User;
import com.swiftship.logistic.backend.models.enums.StatusCode;
import com.swiftship.logistic.backend.services.AuthService;
import com.swiftship.logistic.backend.services.JwtService;
import com.swiftship.logistic.backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Log
public class AuthController {
    public final AuthService authService;
    public final JwtService jwtService;
    public final UserService userService;

    public AuthController(AuthService authService, JwtService jwtService, UserService userService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    //learning
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            User user = authService.authenticate(loginDto);
            log.info(loginDto.getPassword());
            // Assuming you have a JWT token generation service
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", user.getRole().getRole());
            String token = this.jwtService.generateToken(extraClaims, user);
            LoginResponseDto responseDto = getLoginResponseDto(user, token);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        } catch (UsernameNotFoundException ex) {
            ResponseDto resDto = new ResponseDto(ex.getMessage(), StatusCode.INVALID_CREDENTIALS);
            return new ResponseEntity<>(resDto, HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException ex) {
            ResponseDto resDto = new ResponseDto(ex.getMessage(), StatusCode.INVALID_CREDENTIALS);
            return new ResponseEntity<>(resDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ResponseDto resDto = new ResponseDto("internal server error", StatusCode.INTERNAL_SERVER_ERROR);
            log.info(ex.getMessage());
            return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static LoginResponseDto getLoginResponseDto(User user, String token) {
        UserResponseDto userDto = new UserResponseDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getCountryCode(), user.getPhone(), user.getRole().getRole(), user.getIsAccountLocked(), user.getIsAccountVerified(), user.getCreatedAt(), user.getUpdatedAt());
        return new LoginResponseDto(Optional.of(token), userDto, "User logged in successfully!", StatusCode.LOGIN_SUCCESS);
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@RequestBody SignUpDto signUpDto) {
        try {
            // In a real app, you'd check if email exists before creating
            if (userService.findByEmail(signUpDto.getEmail())) {
                return new ResponseEntity<>(new ResponseDto("Email already exists.", StatusCode.EMAIL_ALREADY_EXISTS), HttpStatus.CONFLICT);
            }
            if (userService.findByUsername(signUpDto.getUsername())) {
                return new ResponseEntity<>(new ResponseDto("Username already exists.", StatusCode.USERNAME_ALREADY_EXISTS), HttpStatus.CONFLICT);
            }
            userService.registerNewUser(signUpDto); // Your service method to create user
            return new ResponseEntity<>(new ResponseDto("User registered successfully!", StatusCode.REGISTRATION_SUCCESS), HttpStatus.CREATED // Use 201 Created for new resource
            );
        } catch (Exception e) {
            // Log the exception
            return new ResponseEntity<>(new ResponseDto("Registration failed " + e.getMessage(), StatusCode.UNKOWN_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/validate-jwt")
    public ResponseEntity<ResponseWithDataDto> validateJwt(@RequestHeader("Authorization") String bearer) {
        log.info(bearer);
        Boolean isValidated = this.jwtService.validateToken(bearer.split(" ")[1]);
        Map<String, Boolean> data = new HashMap<>();
        data.put("isValidated", isValidated);
        return new ResponseEntity<>(new ResponseWithDataDto(null, data, StatusCode.OPERATION_SUCCESS), HttpStatus.OK);

    }
}
