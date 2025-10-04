package com.swiftship.logistic.backend.controllers;

import com.swiftship.logistic.backend.models.dto.*;
import com.swiftship.logistic.backend.models.entities.User;
import com.swiftship.logistic.backend.models.enums.StatusCode;
import com.swiftship.logistic.backend.services.AuthService;
import com.swiftship.logistic.backend.services.JwtBlacklistService;
import com.swiftship.logistic.backend.services.JwtService;
import com.swiftship.logistic.backend.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@Log
public class AuthController {
    public final AuthService authService;
    public final JwtService jwtService;
    public final UserService userService;
    public final JwtBlacklistService jwtBlacklistService;

    public AuthController(AuthService authService, JwtService jwtService, UserService userService, JwtBlacklistService jwtBlacklistService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    //learning
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> authenticateUser(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        try {
            User user = authService.authenticate(loginDto);
            // Assuming you have a JWT token generation service
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", user.getRole().getRole());
            String token = this.jwtService.generateToken(extraClaims, user);
            ResponseCookie jwtCookie = ResponseCookie.from("jwt_token", token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(this.jwtService.getJwtExpirationInMs() / 1000)
                    .sameSite("Lax")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            LoginResponseDto responseDto = new LoginResponseDto(getUserResponseDto(user), "User logged in successfully!", StatusCode.LOGIN_SUCCESS);
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

    private static UserResponseDto getUserResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getCountryCode(), user.getPhone(), user.getRole().getRole(), user.getIsAccountLocked(), user.getIsAccountVerified(), user.getCreatedAt(), user.getUpdatedAt());
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@RequestBody SignUpDto signUpDto) {
        try {
            // In a real app, you'd check if email exists before creating
            if (userService.findByEmail(signUpDto.getEmail())) {
                return new ResponseEntity<>(new ResponseDto("Email already exists.", StatusCode.EMAIL_ALREADY_EXISTS), HttpStatus.CONFLICT);
            }
            if (userService.findByUsername(signUpDto.getUsername()).isPresent()) {
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
    public ResponseEntity<ResponseWithDataDto> validateJwt(HttpServletRequest request) {
        log.info("Validating");
        String jwtToken = this.jwtService.getJwtFromRequest(request);
        Boolean isValidated = this.jwtService.validateToken(jwtToken);
        Claims claims = jwtService.getClaimsFromJwt(jwtToken);
        String userName = claims.getSubject();

        Map<String, Object> data = new HashMap<>();
        User user = this.userService.findByUsername(userName).get();
        data.put("isValidated", isValidated);
        data.put("user", getUserResponseDto(user));
        return new ResponseEntity<>(new ResponseWithDataDto(null, data, StatusCode.OPERATION_SUCCESS), HttpStatus.OK);

    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request, HttpServletResponse response) {
        String jwt = null;
        // Try to get JWT from cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) { // Match your cookie name
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        // If JWT found, blacklist it
        if (jwt != null) {
            jwtBlacklistService.blacklistToken(jwt);
            log.info("JWT blacklisted: " + jwt);
        } else {
            log.info("Logout request received but no JWT token found in cookies.");
        }

        // --- Invalidate / Clear the HTTP-only cookie ---
        ResponseCookie deleteCookie = ResponseCookie.from("jwt_token", "") // Set value to empty
                .httpOnly(true)
                .secure(false) // Should match your login cookie's secure setting
                .path("/")
                .maxAge(0) // Set max-age to 0 to instruct browser to delete it immediately
                .sameSite("Lax") // Match your login cookie's SameSite setting
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        log.info("jwt_token cookie deletion header sent.");

        return new ResponseEntity<>(new ResponseDto(
                "User logged out successfully!",
                StatusCode.LOGOUT_SUCCESS
        ), HttpStatus.OK);

    }
}
