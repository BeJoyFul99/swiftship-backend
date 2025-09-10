package com.swiftship.logistic.backend.repositories;

import com.swiftship.logistic.backend.models.entities.Role;
import com.swiftship.logistic.backend.models.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(UserRole role);
}
