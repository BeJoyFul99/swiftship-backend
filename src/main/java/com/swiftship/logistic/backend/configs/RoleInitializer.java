package com.swiftship.logistic.backend.configs;

import com.swiftship.logistic.backend.models.entities.Role;
import com.swiftship.logistic.backend.models.enums.UserRole;
import com.swiftship.logistic.backend.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Component
public class RoleInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RoleInitializer.class);

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Checking and initializing roles...");

        Arrays.stream(UserRole.values()).forEach(roleName -> {
            // Check if the role already exists in the database
            if (roleRepository.findByRole(roleName).isEmpty()) { // Corrected method call
                // If not, create and save the new role
                Role newRole = new Role();
                newRole.setRole(roleName); // Use the setter for the 'role' field
                roleRepository.save(newRole);
                logger.info("Created role: {}", roleName);
            } else {
                logger.debug("Role {} already exists.", roleName);
            }
        });

        logger.info("Role initialization complete.");
    }
}