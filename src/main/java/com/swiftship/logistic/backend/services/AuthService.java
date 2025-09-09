package com.swiftship.logistic.backend.services;

import com.swiftship.logistic.backend.models.dto.LoginDto;
import com.swiftship.logistic.backend.models.entities.User;
import com.swiftship.logistic.backend.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(LoginDto loginDto) {
        User user = Optional.ofNullable(userRepository.findByEmail(loginDto.getEmail())).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!this.passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return user;

    }

}
