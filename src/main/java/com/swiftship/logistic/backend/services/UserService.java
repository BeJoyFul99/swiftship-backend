package com.swiftship.logistic.backend.services;

import com.swiftship.logistic.backend.exceptions.EmailAlreadyExistsException;
import com.swiftship.logistic.backend.models.dto.SignUpDto;
import com.swiftship.logistic.backend.models.entities.User;
import com.swiftship.logistic.backend.models.enums.UserRole;
import com.swiftship.logistic.backend.repositories.RoleRepository;
import com.swiftship.logistic.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional // Ensures the entire operation is atomic
    public void registerNewUser(SignUpDto registerDto) throws Exception {
        // 1. Validate if user with given email already exists
        Optional<User> existingUser = userRepository.findByEmail(registerDto.getEmail());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("User with email " + registerDto.getEmail() + " already exists.");
        }

        // 3. Create a new User entity
        User newUser = new User();
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword())); // Hash the password
        newUser.setFirstName(registerDto.getFirstName());
        newUser.setUsername(registerDto.getUsername());
        newUser.setLastName(registerDto.getLastName());
        newUser.setCountryCode(registerDto.getCountryCode());
        newUser.setPhone(String.valueOf(registerDto.getPhone()));

        // 4. Assign default roles (e.g., "ROLE_USER") if you have a role management system
        newUser.setRole(this.roleRepository.findByRole(UserRole.CUSTOMER).get()); // Example

        // 5. Save the new user to the database
        userRepository.save(newUser);
    }


    public Boolean findByEmail(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }


    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
}
